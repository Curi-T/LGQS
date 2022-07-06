package cn.cqut.lgqs.wx.web;

import cn.cqut.lgqs.core.util.JacksonUtil;
import cn.cqut.lgqs.core.util.ResponseUtil;
import cn.cqut.lgqs.core.validator.Order;
import cn.cqut.lgqs.core.validator.Sort;
import cn.cqut.lgqs.db.domain.LgqsCart;
import cn.cqut.lgqs.db.domain.LgqsCoupon;
import cn.cqut.lgqs.db.domain.LgqsCouponUser;
import cn.cqut.lgqs.db.domain.LgqsGrouponRules;
import cn.cqut.lgqs.db.service.*;
import cn.cqut.lgqs.db.util.CouponConstant;
import cn.cqut.lgqs.wx.annotation.LoginUser;
import cn.cqut.lgqs.wx.util.WxResponseCode;
import cn.cqut.lgqs.wx.vo.CouponVo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 优惠券服务
 */
@RestController
@RequestMapping("/wx/coupon")
@Validated
public class WxCouponController {
    private final Log logger = LogFactory.getLog(WxCouponController.class);

    @Autowired
    private LgqsCouponService couponService;
    @Autowired
    private LgqsCouponUserService couponUserService;
    @Autowired
    private LgqsCartService cartService;
    @Autowired
    private CouponVerifyService couponVerifyService;

    /**
     * 优惠券列表
     *
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    @GetMapping("list")
    public Object list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {

        List<LgqsCoupon> couponList = couponService.queryList(page, limit, sort, order);
        return ResponseUtil.okList(couponList);
    }

    /**
     * 个人优惠券列表
     *
     * @param userId
     * @param status
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    @GetMapping("mylist")
    public Object mylist(@LoginUser Integer userId,
                       Short status,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        List<LgqsCouponUser> couponUserList = couponUserService.queryList(userId, null, status, page, limit, sort, order);
        List<CouponVo> couponVoList = change(couponUserList);
        return ResponseUtil.okList(couponVoList, couponUserList);
    }

    private List<CouponVo> change(List<LgqsCouponUser> couponList) {
        List<CouponVo> couponVoList = new ArrayList<>(couponList.size());
        for(LgqsCouponUser couponUser : couponList){
            Integer couponId = couponUser.getCouponId();
            LgqsCoupon coupon = couponService.findById(couponId);
            CouponVo couponVo = new CouponVo();
            couponVo.setId(couponUser.getId());
            couponVo.setCid(coupon.getId());
            couponVo.setName(coupon.getName());
            couponVo.setDesc(coupon.getDesc());
            couponVo.setTag(coupon.getTag());
            couponVo.setMin(coupon.getMin());
            couponVo.setDiscount(coupon.getDiscount());
            couponVo.setStartTime(couponUser.getStartTime());
            couponVo.setEndTime(couponUser.getEndTime());

            couponVoList.add(couponVo);
        }

        return couponVoList;
    }


    /**
     * 当前点餐车下单餐品订单可用优惠券
     *
     * @param userId
     * @param cartId
     * @param grouponRulesId
     * @return
     */
    @GetMapping("selectlist")
    public Object selectlist(@LoginUser Integer userId, Integer cartId, Integer grouponRulesId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        // 团购优惠
        BigDecimal grouponPrice = new BigDecimal(0.00);
        // 餐品价格
        List<LgqsCart> checkedGoodsList = null;
        if (cartId == null || cartId.equals(0)) {
            checkedGoodsList = cartService.queryByUidAndChecked(userId);
        } else {
            LgqsCart cart = cartService.findById(userId, cartId);
            if (cart == null) {
                return ResponseUtil.badArgumentValue();
            }
            checkedGoodsList = new ArrayList<>(1);
            checkedGoodsList.add(cart);
        }
        BigDecimal checkedGoodsPrice = new BigDecimal(0.00);
        for (LgqsCart cart : checkedGoodsList) {
            checkedGoodsPrice = checkedGoodsPrice.add(cart.getPrice().multiply(new BigDecimal(cart.getNumber())));
        }
        // 计算优惠券可用情况
        List<LgqsCouponUser> couponUserList = couponUserService.queryAll(userId);
        List<CouponVo> couponVoList = change(couponUserList);
        for (CouponVo cv : couponVoList) {
            LgqsCoupon coupon = couponVerifyService.checkCoupon(userId, cv.getCid(), cv.getId(), checkedGoodsPrice, checkedGoodsList);
            cv.setAvailable(coupon != null);
        }

        return ResponseUtil.okList(couponVoList);
    }

    /**
     * 优惠券领取
     *
     * @param userId 用户ID
     * @param body 请求内容， { couponId: xxx }
     * @return 操作结果
     */
    @PostMapping("receive")
    public Object receive(@LoginUser Integer userId, @RequestBody String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        Integer couponId = JacksonUtil.parseInteger(body, "couponId");
        if(couponId == null){
            return ResponseUtil.badArgument();
        }

        LgqsCoupon coupon = couponService.findById(couponId);
        if(coupon == null){
            return ResponseUtil.badArgumentValue();
        }

        // 当前已领取数量和总数量比较
        Integer total = coupon.getTotal();
        Integer totalCoupons = couponUserService.countCoupon(couponId);
        if((total != 0) && (totalCoupons >= total)){
            return ResponseUtil.fail(WxResponseCode.COUPON_EXCEED_LIMIT, "优惠券已领完");
        }

        // 当前用户已领取数量和用户限领数量比较
        Integer limit = coupon.getLimit().intValue();
        Integer userCounpons = couponUserService.countUserAndCoupon(userId, couponId);
        if((limit != 0) && (userCounpons >= limit)){
            return ResponseUtil.fail(WxResponseCode.COUPON_EXCEED_LIMIT, "优惠券已经领取过");
        }

        // 优惠券分发类型
        // 例如注册赠券类型的优惠券不能领取
        Short type = coupon.getType();
        if(type.equals(CouponConstant.TYPE_REGISTER)){
            return ResponseUtil.fail(WxResponseCode.COUPON_RECEIVE_FAIL, "新用户优惠券自动发送");
        }
        else if(type.equals(CouponConstant.TYPE_CODE)){
            return ResponseUtil.fail(WxResponseCode.COUPON_RECEIVE_FAIL, "优惠券只能兑换");
        }
        else if(!type.equals(CouponConstant.TYPE_COMMON)){
            return ResponseUtil.fail(WxResponseCode.COUPON_RECEIVE_FAIL, "优惠券类型不支持");
        }

        // 优惠券状态，已下架或者过期不能领取
        Short status = coupon.getStatus();
        if(status.equals(CouponConstant.STATUS_OUT)){
            return ResponseUtil.fail(WxResponseCode.COUPON_EXCEED_LIMIT, "优惠券已领完");
        }
        else if(status.equals(CouponConstant.STATUS_EXPIRED)){
            return ResponseUtil.fail(WxResponseCode.COUPON_RECEIVE_FAIL, "优惠券已经过期");
        }

        // 用户领券记录
        LgqsCouponUser couponUser = new LgqsCouponUser();
        couponUser.setCouponId(couponId);
        couponUser.setUserId(userId);
        Short timeType = coupon.getTimeType();
        if (timeType.equals(CouponConstant.TIME_TYPE_TIME)) {
            couponUser.setStartTime(coupon.getStartTime());
            couponUser.setEndTime(coupon.getEndTime());
        }
        else{
            LocalDateTime now = LocalDateTime.now();
            couponUser.setStartTime(now);
            couponUser.setEndTime(now.plusDays(coupon.getDays()));
        }
        couponUserService.add(couponUser);

        return ResponseUtil.ok();
    }

    /**
     * 优惠券兑换
     *
     * @param userId 用户ID
     * @param body 请求内容， { code: xxx }
     * @return 操作结果
     */
    @PostMapping("exchange")
    public Object exchange(@LoginUser Integer userId, @RequestBody String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        String code = JacksonUtil.parseString(body, "code");
        if(code == null){
            return ResponseUtil.badArgument();
        }

        LgqsCoupon coupon = couponService.findByCode(code);
        if(coupon == null){
            return ResponseUtil.fail(WxResponseCode.COUPON_CODE_INVALID, "优惠券不正确");
        }
        Integer couponId = coupon.getId();

        // 当前已领取数量和总数量比较
        Integer total = coupon.getTotal();
        Integer totalCoupons = couponUserService.countCoupon(couponId);
        if((total != 0) && (totalCoupons >= total)){
            return ResponseUtil.fail(WxResponseCode.COUPON_EXCEED_LIMIT, "优惠券已兑换");
        }

        // 当前用户已领取数量和用户限领数量比较
        Integer limit = coupon.getLimit().intValue();
        Integer userCounpons = couponUserService.countUserAndCoupon(userId, couponId);
        if((limit != 0) && (userCounpons >= limit)){
            return ResponseUtil.fail(WxResponseCode.COUPON_EXCEED_LIMIT, "优惠券已兑换");
        }

        // 优惠券分发类型
        // 例如注册赠券类型的优惠券不能领取
        Short type = coupon.getType();
        if(type.equals(CouponConstant.TYPE_REGISTER)){
            return ResponseUtil.fail(WxResponseCode.COUPON_RECEIVE_FAIL, "新用户优惠券自动发送");
        }
        else if(type.equals(CouponConstant.TYPE_COMMON)){
            return ResponseUtil.fail(WxResponseCode.COUPON_RECEIVE_FAIL, "优惠券只能领取，不能兑换");
        }
        else if(!type.equals(CouponConstant.TYPE_CODE)){
            return ResponseUtil.fail(WxResponseCode.COUPON_RECEIVE_FAIL, "优惠券类型不支持");
        }

        // 优惠券状态，已下架或者过期不能领取
        Short status = coupon.getStatus();
        if(status.equals(CouponConstant.STATUS_OUT)){
            return ResponseUtil.fail(WxResponseCode.COUPON_EXCEED_LIMIT, "优惠券已兑换");
        }
        else if(status.equals(CouponConstant.STATUS_EXPIRED)){
            return ResponseUtil.fail(WxResponseCode.COUPON_RECEIVE_FAIL, "优惠券已经过期");
        }

        // 用户领券记录
        LgqsCouponUser couponUser = new LgqsCouponUser();
        couponUser.setCouponId(couponId);
        couponUser.setUserId(userId);
        Short timeType = coupon.getTimeType();
        if (timeType.equals(CouponConstant.TIME_TYPE_TIME)) {
            couponUser.setStartTime(coupon.getStartTime());
            couponUser.setEndTime(coupon.getEndTime());
        }
        else{
            LocalDateTime now = LocalDateTime.now();
            couponUser.setStartTime(now);
            couponUser.setEndTime(now.plusDays(coupon.getDays()));
        }
        couponUserService.add(couponUser);

        return ResponseUtil.ok();
    }
}