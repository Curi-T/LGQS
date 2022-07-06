package cn.cqut.lgqs.admin.job;

import cn.cqut.lgqs.core.system.SystemConfig;
import cn.cqut.lgqs.db.domain.LgqsOrder;
import cn.cqut.lgqs.db.domain.LgqsOrderGoods;
import cn.cqut.lgqs.db.service.LgqsOrderGoodsService;
import cn.cqut.lgqs.db.service.LgqsOrderService;
import cn.cqut.lgqs.db.util.OrderUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 检测订单状态
 * @author CuriT
 */
@Component
public class OrderJob {
    private final Log logger = LogFactory.getLog(OrderJob.class);

    @Autowired
    private LgqsOrderGoodsService orderGoodsService;
    @Autowired
    private LgqsOrderService orderService;

    /**
     * 自动确认订单
     * <p>
     * 定时检查订单未确认情况，如果超时 lgqs_ORDER_UNCONFIRM 天则自动确认订单
     * 定时时间是每天凌晨3点。
     * <p>
     * TODO
     * 注意，因为是相隔一天检查，因此导致订单真正超时时间是 [lgqs_ORDER_UNCONFIRM, 1 + lgqs_ORDER_UNCONFIRM]
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void checkOrderUnconfirm() {
        logger.info("系统开启定时任务检查订单是否已经超期自动确认收货");

        List<LgqsOrder> orderList = orderService.queryUnconfirm(SystemConfig.getOrderUnconfirm());
        for (LgqsOrder order : orderList) {

            // 设置订单已取消状态
            order.setOrderStatus(OrderUtil.STATUS_AUTO_CONFIRM);
            order.setConfirmTime(LocalDateTime.now());
            if (orderService.updateWithOptimisticLocker(order) == 0) {
                logger.info("订单 ID=" + order.getId() + " 数据已经更新，放弃自动确认收货");
            } else {
                logger.info("订单 ID=" + order.getId() + " 已经超期自动确认收货");
            }
        }

        logger.info("系统结束定时任务检查订单是否已经超期自动确认收货");
    }

    /**
     * 可评价订单餐品超期
     * <p>
     * 定时检查订单餐品评价情况，如果确认餐品超时 lgqs_ORDER_COMMENT 天则取消可评价状态
     * 定时时间是每天凌晨4点。
     * <p>
     * TODO
     * 注意，因为是相隔一天检查，因此导致订单真正超时时间是 [lgqs_ORDER_COMMENT, 1 + lgqs_ORDER_COMMENT]
     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void checkOrderComment() {
        logger.info("系统开启任务检查订单是否已经超期未评价");

        List<LgqsOrder> orderList = orderService.queryComment(SystemConfig.getOrderComment());
        for (LgqsOrder order : orderList) {
            order.setComments((short) 0);
            orderService.updateWithOptimisticLocker(order);

            List<LgqsOrderGoods> orderGoodsList = orderGoodsService.queryByOid(order.getId());
            for (LgqsOrderGoods orderGoods : orderGoodsList) {
                orderGoods.setComment(-1);
                orderGoodsService.updateById(orderGoods);
            }
        }

        logger.info("系统结束任务检查订单是否已经超期未评价");
    }
}
