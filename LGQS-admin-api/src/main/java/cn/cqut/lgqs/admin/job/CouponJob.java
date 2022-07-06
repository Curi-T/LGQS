package cn.cqut.lgqs.admin.job;

import cn.cqut.lgqs.db.domain.LgqsCoupon;
import cn.cqut.lgqs.db.domain.LgqsCouponUser;
import cn.cqut.lgqs.db.service.LgqsCouponService;
import cn.cqut.lgqs.db.service.LgqsCouponUserService;
import cn.cqut.lgqs.db.util.CouponConstant;
import cn.cqut.lgqs.db.util.CouponUserConstant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 检测优惠券过期情况
 * @author CuriT
 */
@Component
public class CouponJob {
    private final Log logger = LogFactory.getLog(CouponJob.class);

    @Autowired
    private LgqsCouponService couponService;
    @Autowired
    private LgqsCouponUserService couponUserService;

    /**
     * 每隔一个小时检查
     * TODO
     * 注意，因为是相隔一个小时检查，因此导致优惠券真正超时时间可能比设定时间延迟1个小时
     */
    @Scheduled(fixedDelay = 60 * 60 * 1000)
    public void checkCouponExpired() {
        logger.info("系统开启任务检查优惠券是否已经过期");

        List<LgqsCoupon> couponList = couponService.queryExpired();
        for (LgqsCoupon coupon : couponList) {
            coupon.setStatus(CouponConstant.STATUS_EXPIRED);
            couponService.updateById(coupon);
        }

        List<LgqsCouponUser> couponUserList = couponUserService.queryExpired();
        for (LgqsCouponUser couponUser : couponUserList) {
            couponUser.setStatus(CouponUserConstant.STATUS_EXPIRED);
            couponUserService.update(couponUser);
        }

        logger.info("系统结束任务检查优惠券是否已经过期");
    }

}
