package cn.cqut.lgqs.wx.service;

import cn.cqut.lgqs.core.express.ExpressService;
import cn.cqut.lgqs.core.express.dao.ExpressInfo;
import cn.cqut.lgqs.core.notify.NotifyService;
import cn.cqut.lgqs.core.qcode.QCodeService;
import cn.cqut.lgqs.core.system.SystemConfig;
import cn.cqut.lgqs.core.task.TaskService;
import cn.cqut.lgqs.core.util.JacksonUtil;
import cn.cqut.lgqs.core.util.ResponseUtil;
import cn.cqut.lgqs.db.domain.*;
import cn.cqut.lgqs.db.service.*;
import cn.cqut.lgqs.db.util.CouponUserConstant;
import cn.cqut.lgqs.db.util.GrouponConstant;
import cn.cqut.lgqs.db.util.OrderHandleOption;
import cn.cqut.lgqs.db.util.OrderUtil;
import cn.cqut.lgqs.wx.task.OrderUnpaidTask;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.service.WxPayService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.cqut.lgqs.wx.util.WxResponseCode.*;

/**
 * 订单服务
 *
 * <p>
 * 订单状态：
 * 101 订单生成，未支付；102，下单后未支付用户取消；103，下单后未支付超时系统自动取消
 * 201 支付完成，商家未发货；202，订单生产，已付款未发货，但是退款取消；
 * 301 商家发货，用户未确认；
 * 401 用户确认收货； 402 用户没有确认收货超过一定时间，系统自动确认收货；
 *
 * <p>
 * 用户操作：
 * 当101用户未付款时，此时用户可以进行的操作是取消订单，或者付款操作
 * 当201支付完成而商家未发货时，此时用户可以取消订单并申请退款
 * 当301商家已发货时，此时用户可以有确认收货的操作
 * 当401用户确认收货以后，此时用户可以进行的操作是删除订单，评价餐品，申请售后，或者再次购买
 * 当402系统自动确认收货以后，此时用户可以删除订单，评价餐品，申请售后，或者再次购买
 */
@Service
public class WxOrderService {
    private final Log logger = LogFactory.getLog(WxOrderService.class);

    @Autowired
    private LgqsUserService userService;
    @Autowired
    private LgqsOrderService orderService;
    @Autowired
    private LgqsOrderGoodsService orderGoodsService;
    @Autowired
    private LgqsAddressService addressService;
    @Autowired
    private LgqsCartService cartService;
    @Autowired
    private LgqsRegionService regionService;
    @Autowired
    private LgqsGoodsProductService productService;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private QCodeService qCodeService;
    @Autowired
    private ExpressService expressService;
    @Autowired
    private LgqsCommentService commentService;
    @Autowired
    private LgqsCouponService couponService;
    @Autowired
    private LgqsCouponUserService couponUserService;
    @Autowired
    private CouponVerifyService couponVerifyService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private LgqsAftersaleService aftersaleService;

    /**
     * 订单列表
     *
     * @param userId   用户ID
     * @param showType 订单信息：
     *                 0，全部订单；
     *                 1，待付款；
     *                 2，待发货；
     *                 3，待收货；
     *                 4，待评价。
     * @param page     分页页数
     * @param limit    分页大小
     * @return 订单列表
     */
    public Object list(Integer userId, Integer showType, Integer page, Integer limit, String sort, String order) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        List<Short> orderStatus = OrderUtil.orderStatus(showType);
        List<LgqsOrder> orderList = orderService.queryByOrderStatus(userId, orderStatus, page, limit, sort, order);

        List<Map<String, Object>> orderVoList = new ArrayList<>(orderList.size());
        for (LgqsOrder o : orderList) {
            Map<String, Object> orderVo = new HashMap<>();
            orderVo.put("id", o.getId());
            orderVo.put("orderSn", o.getOrderSn());
            orderVo.put("actualPrice", o.getActualPrice());
            orderVo.put("orderStatusText", OrderUtil.orderStatusText(o));
            orderVo.put("handleOption", OrderUtil.build(o));
            orderVo.put("aftersaleStatus", o.getAftersaleStatus());

            orderVo.put("isGroupin", false);

            List<LgqsOrderGoods> orderGoodsList = orderGoodsService.queryByOid(o.getId());
            List<Map<String, Object>> orderGoodsVoList = new ArrayList<>(orderGoodsList.size());
            for (LgqsOrderGoods orderGoods : orderGoodsList) {
                Map<String, Object> orderGoodsVo = new HashMap<>();
                orderGoodsVo.put("id", orderGoods.getId());
                orderGoodsVo.put("goodsName", orderGoods.getGoodsName());
                orderGoodsVo.put("number", orderGoods.getNumber());
                orderGoodsVo.put("picUrl", orderGoods.getPicUrl());
                orderGoodsVo.put("specifications", orderGoods.getSpecifications());
                orderGoodsVo.put("price", orderGoods.getPrice());
                orderGoodsVoList.add(orderGoodsVo);
            }
            orderVo.put("goodsList", orderGoodsVoList);

            orderVoList.add(orderVo);
        }

        return ResponseUtil.okList(orderVoList, orderList);
    }

    /**
     * 订单详情
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     * @return 订单详情
     */
    public Object detail(Integer userId, Integer orderId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        // 订单信息
        LgqsOrder order = orderService.findById(userId, orderId);
        if (null == order) {
            return ResponseUtil.fail(ORDER_UNKNOWN, "订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            return ResponseUtil.fail(ORDER_INVALID, "不是当前用户的订单");
        }
        Map<String, Object> orderVo = new HashMap<String, Object>();
        orderVo.put("id", order.getId());
        orderVo.put("orderSn", order.getOrderSn());
        orderVo.put("message", order.getMessage());
        orderVo.put("addTime", order.getAddTime());
        orderVo.put("consignee", order.getConsignee());
        orderVo.put("mobile", order.getMobile());
        orderVo.put("address", order.getAddress());
        orderVo.put("goodsPrice", order.getGoodsPrice());
        orderVo.put("couponPrice", order.getCouponPrice());
        orderVo.put("freightPrice", order.getFreightPrice());
        orderVo.put("actualPrice", order.getActualPrice());
        orderVo.put("orderStatusText", OrderUtil.orderStatusText(order));
        orderVo.put("handleOption", OrderUtil.build(order));
        orderVo.put("aftersaleStatus", order.getAftersaleStatus());
        orderVo.put("expCode", order.getShipChannel());
        orderVo.put("expName", expressService.getVendorName(order.getShipChannel()));
        orderVo.put("expNo", order.getShipSn());

        List<LgqsOrderGoods> orderGoodsList = orderGoodsService.queryByOid(order.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("orderInfo", orderVo);
        result.put("orderGoods", orderGoodsList);

        // 订单状态为已发货且配送信息不为空
        //"YTO", "800669400640887922"
        if (order.getOrderStatus().equals(OrderUtil.STATUS_SHIP)) {
            ExpressInfo ei = expressService.getExpressInfo(order.getShipChannel(), order.getShipSn());
            if (ei == null) {
                result.put("expressInfo", new ArrayList<>());
            } else {
                result.put("expressInfo", ei);
            }
        } else {
            result.put("expressInfo", new ArrayList<>());
        }

        return ResponseUtil.ok(result);

    }

    /**
     * 提交订单
     * <p>
     * 1. 创建订单表项和订单餐品表项;
     * 2. 点餐车清空;
     * 3. 优惠券设置已用;
     * 4. 餐品餐品库存减少;
     *
     * @param userId 用户ID
     * @param body   订单信息，{ cartId：xxx, addressId: xxx, couponId: xxx, message: xxx, grouponRulesId: xxx,  grouponLinkId: xxx}
     * @return 提交订单操作结果
     */
    @Transactional
    public Object submit(Integer userId, String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        if (body == null) {
            return ResponseUtil.badArgument();
        }
        Integer cartId = JacksonUtil.parseInteger(body, "cartId");
        Integer addressId = JacksonUtil.parseInteger(body, "addressId");
        Integer couponId = JacksonUtil.parseInteger(body, "couponId");
        Integer userCouponId = JacksonUtil.parseInteger(body, "userCouponId");
        String message = JacksonUtil.parseString(body, "message");
        Integer grouponRulesId = JacksonUtil.parseInteger(body, "grouponRulesId");
        Integer grouponLinkId = JacksonUtil.parseInteger(body, "grouponLinkId");


        if (cartId == null || addressId == null || couponId == null) {
            return ResponseUtil.badArgument();
        }

        // 收货地址
        LgqsAddress checkedAddress = addressService.query(userId, addressId);
        if (checkedAddress == null) {
            return ResponseUtil.badArgument();
        }

        BigDecimal grouponPrice = new BigDecimal(0);
        // 餐品价格
        List<LgqsCart> checkedGoodsList = null;
        if (cartId.equals(0)) {
            checkedGoodsList = cartService.queryByUidAndChecked(userId);
        } else {
            LgqsCart cart = cartService.findById(cartId);
            checkedGoodsList = new ArrayList<>(1);
            checkedGoodsList.add(cart);
        }
        if (checkedGoodsList.size() == 0) {
            return ResponseUtil.badArgumentValue();
        }
        BigDecimal checkedGoodsPrice = new BigDecimal(0);
        for (LgqsCart cart : checkedGoodsList) {
            checkedGoodsPrice = checkedGoodsPrice.add(cart.getPrice().multiply(new BigDecimal(cart.getNumber())));
        }
        // 获取可用的优惠券信息
        // 使用优惠券减免的金额
        BigDecimal couponPrice = new BigDecimal(0);
        // 如果couponId=0则没有优惠券，couponId=-1则不使用优惠券
        if (couponId != 0 && couponId != -1) {
            LgqsCoupon coupon = couponVerifyService.checkCoupon(userId, couponId, userCouponId, checkedGoodsPrice, checkedGoodsList);
            if (coupon == null) {
                return ResponseUtil.badArgumentValue();
            }
            couponPrice = coupon.getDiscount();
        }


        // 根据订单餐品总价计算运费，满足条件（例如88元）则免运费，否则需要支付运费（例如8元）；
        BigDecimal freightPrice = new BigDecimal(0);
        if (checkedGoodsPrice.compareTo(SystemConfig.getFreightLimit()) < 0) {
            freightPrice = SystemConfig.getFreight();
        }

        // 可以使用的其他钱，例如用户积分
        BigDecimal integralPrice = new BigDecimal(0);

        // 订单费用
        BigDecimal orderTotalPrice = checkedGoodsPrice.add(freightPrice).subtract(couponPrice).max(new BigDecimal(0));
        // 最终支付费用
        BigDecimal actualPrice = orderTotalPrice.subtract(integralPrice);

        Integer orderId = null;
        LgqsOrder order = null;
        // 订单
        order = new LgqsOrder();
        order.setUserId(userId);
        order.setOrderSn(orderService.generateOrderSn(userId));
        order.setOrderStatus(OrderUtil.STATUS_CREATE);
        order.setConsignee(checkedAddress.getName());
        order.setMobile(checkedAddress.getTel());
        order.setMessage(message);
        String detailedAddress = checkedAddress.getProvince() + checkedAddress.getCity() + checkedAddress.getCounty() + " " + checkedAddress.getAddressDetail();
        order.setAddress(detailedAddress);
        order.setGoodsPrice(checkedGoodsPrice);
        order.setFreightPrice(freightPrice);
        order.setCouponPrice(couponPrice);
        order.setIntegralPrice(integralPrice);
        order.setOrderPrice(orderTotalPrice);
        order.setActualPrice(actualPrice);
        order.setGrouponPrice(new BigDecimal(0));
        // 添加订单表项
        orderService.add(order);
        orderId = order.getId();

        // 添加订单餐品表项
        for (LgqsCart cartGoods : checkedGoodsList) {
            // 订单餐品
            LgqsOrderGoods orderGoods = new LgqsOrderGoods();
            orderGoods.setOrderId(order.getId());
            orderGoods.setGoodsId(cartGoods.getGoodsId());
            orderGoods.setGoodsSn(cartGoods.getGoodsSn());
            orderGoods.setProductId(cartGoods.getProductId());
            orderGoods.setGoodsName(cartGoods.getGoodsName());
            orderGoods.setPicUrl(cartGoods.getPicUrl());
            orderGoods.setPrice(cartGoods.getPrice());
            orderGoods.setNumber(cartGoods.getNumber());
            orderGoods.setSpecifications(cartGoods.getSpecifications());
            orderGoods.setAddTime(LocalDateTime.now());

            orderGoodsService.add(orderGoods);
        }

        // 删除点餐车里面的餐品信息
        if (cartId.equals(0)) {
            cartService.clearGoods(userId);
        } else {
            cartService.deleteById(cartId);
        }

        // 餐品餐品数量减少
        for (LgqsCart checkGoods : checkedGoodsList) {
            Integer productId = checkGoods.getProductId();
            LgqsGoodsProduct product = productService.findById(productId);

            int remainNumber = product.getNumber() - checkGoods.getNumber();
            if (remainNumber < 0) {
                return ResponseUtil.fail(GOODS_NO_STOCK, "库存不足");
            }
            if (productService.reduceStock(productId, checkGoods.getNumber()) == 0) {
                throw new RuntimeException("餐品餐品库存减少失败");
            }
        }

        // 如果使用了优惠券，设置优惠券使用状态
        if (couponId != 0 && couponId != -1) {
            LgqsCouponUser couponUser = couponUserService.findById(userCouponId);
            couponUser.setStatus(CouponUserConstant.STATUS_USED);
            couponUser.setUsedTime(LocalDateTime.now());
            couponUser.setOrderId(orderId);
            couponUserService.update(couponUser);
        }

        //如果是团购项目，添加团购信息
        if (grouponRulesId != null && grouponRulesId > 0) {
            LgqsGroupon groupon = new LgqsGroupon();
            groupon.setOrderId(orderId);
            groupon.setStatus(GrouponConstant.STATUS_NONE);
            groupon.setUserId(userId);
            groupon.setRulesId(grouponRulesId);

        }

        // NOTE: 建议开发者从业务场景核实下面代码，防止用户利用业务BUG使订单跳过支付环节。
        // 如果订单实际支付费用是0，则直接跳过支付变成待发货状态
        boolean payed = false;
        //ATT 提交订单默认支付成功
//        boolean payed = true;
        if (order.getActualPrice().equals(new BigDecimal("0.00"))) {
            payed = true;

            LgqsOrder o = new LgqsOrder();
            o.setId(orderId);
            o.setOrderStatus(OrderUtil.STATUS_PAY);
            orderService.updateSelective(o);

            //TODO 发送邮件和短信通知，这里采用异步发送
            // 订单支付成功以后，会发送短信给用户，以及发送邮件给管理员
            notifyService.notifyMail("新订单通知", order.toString());
        } else {
            // 订单支付超期任务
            taskService.addTask(new OrderUnpaidTask(orderId));
        }

        Map<String, Object> data = new HashMap<>();
        data.put("orderId", orderId);
        data.put("payed", payed);
        if (grouponRulesId != null && grouponRulesId > 0) {
            data.put("grouponLinkId", grouponLinkId);
        } else {
            data.put("grouponLinkId", 0);
        }
        return ResponseUtil.ok(data);
    }

    /**
     * 取消订单
     * <p>
     * 1. 检测当前订单是否能够取消；
     * 2. 设置订单取消状态；
     * 3. 餐品餐品库存恢复；
     * 4. 返还优惠券；
     *
     * @param userId 用户ID
     * @param body   订单信息，{ orderId：xxx }
     * @return 取消订单操作结果
     */
    @Transactional
    public Object cancel(Integer userId, String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        Integer orderId = JacksonUtil.parseInteger(body, "orderId");
        if (orderId == null) {
            return ResponseUtil.badArgument();
        }

        LgqsOrder order = orderService.findById(userId, orderId);
        if (order == null) {
            return ResponseUtil.badArgumentValue();
        }
        if (!order.getUserId().equals(userId)) {
            return ResponseUtil.badArgumentValue();
        }

        LocalDateTime preUpdateTime = order.getUpdateTime();

        // 检测是否能够取消
        OrderHandleOption handleOption = OrderUtil.build(order);
        if (!handleOption.isCancel()) {
            return ResponseUtil.fail(ORDER_INVALID_OPERATION, "订单不能取消");
        }

        // 设置订单已取消状态
        order.setOrderStatus(OrderUtil.STATUS_CANCEL);
        order.setEndTime(LocalDateTime.now());
        if (orderService.updateWithOptimisticLocker(order) == 0) {
            throw new RuntimeException("更新数据已失效");
        }

        // 餐品餐品数量增加
        List<LgqsOrderGoods> orderGoodsList = orderGoodsService.queryByOid(orderId);
        for (LgqsOrderGoods orderGoods : orderGoodsList) {
            Integer productId = orderGoods.getProductId();
            Short number = orderGoods.getNumber();
            if (productService.addStock(productId, number) == 0) {
                throw new RuntimeException("餐品餐品库存增加失败");
            }
        }

        // 返还优惠券
        releaseCoupon(orderId);

        return ResponseUtil.ok();
    }

    /**
     * 付款订单的支付会话标识
     * <p>
     * 1. 检测当前订单是否能够付款
     * 2. 微信商户平台返回支付订单ID
     * 3. 设置订单付款状态
     *
     * @param userId 用户ID
     * @param body   订单信息，{ orderId：xxx }
     * @return 支付订单ID
     */
    @Transactional
    public Object prepay1(Integer userId, String body, HttpServletRequest request) {

        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        Integer orderId = JacksonUtil.parseInteger(body, "orderId");
        if (orderId == null) {
            return ResponseUtil.badArgument();
        }

        LgqsOrder order = orderService.findById(userId, orderId);
        if (order == null) {
            return ResponseUtil.badArgumentValue();
        }
        if (!order.getUserId().equals(userId)) {
            return ResponseUtil.badArgumentValue();
        }

        // 检测是否能够
        OrderHandleOption handleOption = OrderUtil.build(order);
        if (!handleOption.isPay()) {
            return ResponseUtil.fail(ORDER_INVALID_OPERATION, "订单不能支付");
        }

        LgqsUser user = userService.findById(userId);
        String openid = user.getWeixinOpenid();
        if (openid == null) {
            return ResponseUtil.fail(AUTH_OPENID_UNACCESS, "订单不能支付");
        }
        ////////////////////////////////////////////////
        String timeStamp = System.currentTimeMillis() + "";
        String result = "{appId: 'wxa7f799ef6354b3b0',\n" +
                "        timeStamp: '" + timeStamp + "',\n" +
                "        nonceStr: 'xxx',\n" +
                "        packageValue: 'xxx',\n" +
                "        signType: 'xxx',\n" +
                "        paySign: 'xxx'}";

        String payId = MyRandomString.orderId();

        // 检查这个订单是否已经处理过
        if (OrderUtil.hasPayed(order)) {
            return WxPayNotifyResponse.success("订单已经处理成功!");
        }

        order.setPayId(payId);
        order.setPayTime(LocalDateTime.now());
        order.setOrderStatus(OrderUtil.STATUS_PAY);
        if (orderService.updateWithOptimisticLocker(order) == 0) {
            return WxPayNotifyResponse.fail("更新数据已失效");
        }


        //TODO 发送邮件，这里采用异步发送
        // 订单支付成功以后，发送邮件给管理员

        List<LgqsOrderGoods> lgqsOrderGoods = orderGoodsService.queryByOid(orderId);

        String content = order.toStringInfo();
        for (LgqsOrderGoods byId : lgqsOrderGoods) {
            content += "餐品名字：" + byId.getGoodsName() + "\n" +
                    "购买数量：" + byId.getNumber() + "\n" +
                    "餐品图片：" + byId.getPicUrl() + "\n";
        }
        content += "备注：" + order.getMessage();

        notifyService.notifyMail("新订单通知", content);

        // 取消订单超时未支付任务
        taskService.removeTask(new OrderUnpaidTask(order.getId()));

        return ResponseUtil.ok(result);

    }

    /**
     * 订单申请退款
     * <p>
     * 1. 检测当前订单是否能够退款；
     * 2. 设置订单申请退款状态。
     *
     * @param userId 用户ID
     * @param body   订单信息，{ orderId：xxx }
     * @return 订单退款操作结果
     */
    public Object refund(Integer userId, String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        Integer orderId = JacksonUtil.parseInteger(body, "orderId");
        if (orderId == null) {
            return ResponseUtil.badArgument();
        }

        LgqsOrder order = orderService.findById(userId, orderId);
        if (order == null) {
            return ResponseUtil.badArgument();
        }
        if (!order.getUserId().equals(userId)) {
            return ResponseUtil.badArgumentValue();
        }

        OrderHandleOption handleOption = OrderUtil.build(order);
        if (!handleOption.isRefund()) {
            return ResponseUtil.fail(ORDER_INVALID_OPERATION, "订单不能取消");
        }

        // 设置订单申请退款状态
        order.setOrderStatus(OrderUtil.STATUS_REFUND);
        if (orderService.updateWithOptimisticLocker(order) == 0) {
            return ResponseUtil.updatedDateExpired();
        }

        //TODO 发送邮件和短信通知，这里采用异步发送
        // 有用户申请退款，邮件通知运营人员
        notifyService.notifyMail("退款申请", order.toString());

        return ResponseUtil.ok();
    }

    /**
     * 确认收货
     * <p>
     * 1. 检测当前订单是否能够确认收货；
     * 2. 设置订单确认收货状态。
     *
     * @param userId 用户ID
     * @param body   订单信息，{ orderId：xxx }
     * @return 订单操作结果
     */
    public Object confirm(Integer userId, String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        Integer orderId = JacksonUtil.parseInteger(body, "orderId");
        if (orderId == null) {
            return ResponseUtil.badArgument();
        }

        LgqsOrder order = orderService.findById(userId, orderId);
        if (order == null) {
            return ResponseUtil.badArgument();
        }
        if (!order.getUserId().equals(userId)) {
            return ResponseUtil.badArgumentValue();
        }

        OrderHandleOption handleOption = OrderUtil.build(order);
        if (!handleOption.isConfirm()) {
            return ResponseUtil.fail(ORDER_INVALID_OPERATION, "订单不能确认收货");
        }

        Short comments = orderGoodsService.getComments(orderId);
        order.setComments(comments);

        order.setOrderStatus(OrderUtil.STATUS_CONFIRM);
        order.setConfirmTime(LocalDateTime.now());
        if (orderService.updateWithOptimisticLocker(order) == 0) {
            return ResponseUtil.updatedDateExpired();
        }
        return ResponseUtil.ok();
    }

    /**
     * 删除订单
     * <p>
     * 1. 检测当前订单是否可以删除；
     * 2. 删除订单。
     *
     * @param userId 用户ID
     * @param body   订单信息，{ orderId：xxx }
     * @return 订单操作结果
     */
    public Object delete(Integer userId, String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        Integer orderId = JacksonUtil.parseInteger(body, "orderId");
        if (orderId == null) {
            return ResponseUtil.badArgument();
        }

        LgqsOrder order = orderService.findById(userId, orderId);
        if (order == null) {
            return ResponseUtil.badArgument();
        }
        if (!order.getUserId().equals(userId)) {
            return ResponseUtil.badArgumentValue();
        }

        OrderHandleOption handleOption = OrderUtil.build(order);
        if (!handleOption.isDelete()) {
            return ResponseUtil.fail(ORDER_INVALID_OPERATION, "订单不能删除");
        }

        // 订单order_status没有字段用于标识删除
        // 而是存在专门的delete字段表示是否删除
        orderService.deleteById(orderId);
        // 售后也同时删除
        aftersaleService.deleteByOrderId(userId, orderId);

        return ResponseUtil.ok();
    }

    /**
     * 待评价订单餐品信息
     *
     * @param userId 用户ID
     * @param ogid   订单餐品ID
     * @return 待评价订单餐品信息
     */
    public Object goods(Integer userId, Integer ogid) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        LgqsOrderGoods orderGoods = orderGoodsService.findById(ogid);

        if (orderGoods != null) {
            Integer orderId = orderGoods.getOrderId();
            LgqsOrder order = orderService.findById(orderId);
            if (!order.getUserId().equals(userId)) {
                return ResponseUtil.badArgument();
            }
        }
        return ResponseUtil.ok(orderGoods);
    }

    /**
     * 评价订单餐品
     * <p>
     * 确认餐品收货或者系统自动确认餐品收货后7天内可以评价，过期不能评价。
     *
     * @param userId 用户ID
     * @param body   订单信息，{ orderId：xxx }
     * @return 订单操作结果
     */
    public Object comment(Integer userId, String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        Integer orderGoodsId = JacksonUtil.parseInteger(body, "orderGoodsId");
        if (orderGoodsId == null) {
            return ResponseUtil.badArgument();
        }
        LgqsOrderGoods orderGoods = orderGoodsService.findById(orderGoodsId);
        if (orderGoods == null) {
            return ResponseUtil.badArgumentValue();
        }
        Integer orderId = orderGoods.getOrderId();
        LgqsOrder order = orderService.findById(userId, orderId);
        if (order == null) {
            return ResponseUtil.badArgumentValue();
        }
        if (!OrderUtil.isConfirmStatus(order) && !OrderUtil.isAutoConfirmStatus(order)) {
            return ResponseUtil.fail(ORDER_INVALID_OPERATION, "当前餐品不能评价");
        }
        if (!order.getUserId().equals(userId)) {
            return ResponseUtil.fail(ORDER_INVALID, "当前餐品不属于用户");
        }
        Integer commentId = orderGoods.getComment();
        if (commentId == -1) {
            return ResponseUtil.fail(ORDER_COMMENT_EXPIRED, "当前餐品评价时间已经过期");
        }
        if (commentId != 0) {
            return ResponseUtil.fail(ORDER_COMMENTED, "订单餐品已评价");
        }

        String content = JacksonUtil.parseString(body, "content");
        Integer star = JacksonUtil.parseInteger(body, "star");
        if (star == null || star < 0 || star > 5) {
            return ResponseUtil.badArgumentValue();
        }
        Boolean hasPicture = JacksonUtil.parseBoolean(body, "hasPicture");
        List<String> picUrls = JacksonUtil.parseStringList(body, "picUrls");
        if (hasPicture == null || !hasPicture) {
            picUrls = new ArrayList<>(0);
        }

        // 1. 创建评价
        LgqsComment comment = new LgqsComment();
        comment.setUserId(userId);
        comment.setType((byte) 0);
        comment.setValueId(orderGoods.getGoodsId());
        comment.setStar(star.shortValue());
        comment.setContent(content);
        comment.setHasPicture(hasPicture);
        comment.setPicUrls(picUrls.toArray(new String[]{}));
        commentService.save(comment);

        // 2. 更新订单餐品的评价列表
        orderGoods.setComment(comment.getId());
        orderGoodsService.updateById(orderGoods);

        // 3. 更新订单中未评价的订单餐品可评价数量
        Short commentCount = order.getComments();
        if (commentCount > 0) {
            commentCount--;
        }
        order.setComments(commentCount);
        orderService.updateWithOptimisticLocker(order);

        return ResponseUtil.ok();
    }

    /**
     * 取消订单/退款返还优惠券
     * <br/>
     *
     * @param orderId
     * @return void
     * @author Tyson
     * @date 2020/6/8/0008 1:41
     */
    public void releaseCoupon(Integer orderId) {
        List<LgqsCouponUser> couponUsers = couponUserService.findByOid(orderId);
        for (LgqsCouponUser couponUser : couponUsers) {
            // 优惠券状态设置为可使用
            couponUser.setStatus(CouponUserConstant.STATUS_USABLE);
            couponUser.setUpdateTime(LocalDateTime.now());
            couponUserService.update(couponUser);
        }
    }
}