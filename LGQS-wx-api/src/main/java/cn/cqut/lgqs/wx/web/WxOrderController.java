package cn.cqut.lgqs.wx.web;

import cn.cqut.lgqs.core.validator.Order;
import cn.cqut.lgqs.core.validator.Sort;
import cn.cqut.lgqs.wx.annotation.LoginUser;
import cn.cqut.lgqs.wx.service.WxOrderService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/wx/order")
@Validated
public class WxOrderController {
    private final Log logger = LogFactory.getLog(WxOrderController.class);

    @Autowired
    private WxOrderService wxOrderService;

    /**
     * 订单列表
     *
     * @param userId   用户ID
     * @param showType 显示类型，如果是0则是全部订单
     * @param page     分页页数
     * @param limit    分页大小
     * @param sort     排序字段
     * @param order    排序方式
     * @return 订单列表
     */
    @GetMapping("list")
    public Object list(@LoginUser Integer userId,
                       @RequestParam(defaultValue = "0") Integer showType,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        return wxOrderService.list(userId, showType, page, limit, sort, order);
    }

    /**
     * 订单详情
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     * @return 订单详情
     */
    @GetMapping("detail")
    public Object detail(@LoginUser Integer userId, @NotNull Integer orderId) {
        return wxOrderService.detail(userId, orderId);
    }

    /**
     * 提交订单
     *
     * @param userId 用户ID
     * @param body   订单信息，{ cartId：xxx, addressId: xxx, couponId: xxx, message: xxx, grouponRulesId: xxx,  grouponLinkId: xxx}
     * @return 提交订单操作结果
     */
    @PostMapping("submit")
    public Object submit(@LoginUser Integer userId, @RequestBody String body) {
        return wxOrderService.submit(userId, body);
    }

    /**
     * 取消订单
     *
     * @param userId 用户ID
     * @param body   订单信息，{ orderId：xxx }
     * @return 取消订单操作结果
     */
    @PostMapping("cancel")
    public Object cancel(@LoginUser Integer userId, @RequestBody String body) {
        return wxOrderService.cancel(userId, body);
    }

    /**
     * 付款订单的预支付会话标识
     *
     * @param userId 用户ID
     * @param body   订单信息，{ orderId：xxx }
     * @return 支付订单ID
     */
    @PostMapping("prepay")
    public Object prepay(@LoginUser Integer userId, @RequestBody String body, HttpServletRequest request) {
        return wxOrderService.prepay1(userId, body, request);
    }

    /**
     * 订单申请退款
     *
     * @param userId 用户ID
     * @param body   订单信息，{ orderId：xxx }
     * @return 订单退款操作结果
     */
    @PostMapping("refund")
    public Object refund(@LoginUser Integer userId, @RequestBody String body) {
        return wxOrderService.refund(userId, body);
    }

    /**
     * 确认收货
     *
     * @param userId 用户ID
     * @param body   订单信息，{ orderId：xxx }
     * @return 订单操作结果
     */
    @PostMapping("confirm")
    public Object confirm(@LoginUser Integer userId, @RequestBody String body) {
        return wxOrderService.confirm(userId, body);
    }

    /**
     * 删除订单
     *
     * @param userId 用户ID
     * @param body   订单信息，{ orderId：xxx }
     * @return 订单操作结果
     */
    @PostMapping("delete")
    public Object delete(@LoginUser Integer userId, @RequestBody String body) {
        return wxOrderService.delete(userId, body);
    }

    /**
     * 待评价订单餐品信息
     *
     * @param userId 用户ID
     * @param ogid   订单餐品ID
     * @return 待评价订单餐品信息
     */
    @GetMapping("goods")
    public Object goods(@LoginUser Integer userId,
                        @NotNull Integer ogid) {
        return wxOrderService.goods(userId, ogid);
    }

    /**
     * 评价订单餐品
     *
     * @param userId 用户ID
     * @param body   订单信息，{ orderId：xxx }
     * @return 订单操作结果
     */
    @PostMapping("comment")
    public Object comment(@LoginUser Integer userId, @RequestBody String body) {
        return wxOrderService.comment(userId, body);
    }

}