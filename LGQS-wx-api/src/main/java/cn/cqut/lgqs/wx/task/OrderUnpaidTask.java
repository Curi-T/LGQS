package cn.cqut.lgqs.wx.task;

import cn.cqut.lgqs.core.system.SystemConfig;
import cn.cqut.lgqs.core.task.Task;
import cn.cqut.lgqs.core.util.BeanUtil;
import cn.cqut.lgqs.db.domain.LgqsOrder;
import cn.cqut.lgqs.db.domain.LgqsOrderGoods;
import cn.cqut.lgqs.db.service.LgqsGoodsProductService;
import cn.cqut.lgqs.db.service.LgqsOrderGoodsService;
import cn.cqut.lgqs.db.service.LgqsOrderService;
import cn.cqut.lgqs.db.util.OrderUtil;
import cn.cqut.lgqs.wx.service.WxOrderService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.time.LocalDateTime;
import java.util.List;

public class OrderUnpaidTask extends Task {
    private final Log logger = LogFactory.getLog(OrderUnpaidTask.class);
    private int orderId = -1;

    public OrderUnpaidTask(Integer orderId, long delayInMilliseconds){
        super("OrderUnpaidTask-" + orderId, delayInMilliseconds);
        this.orderId = orderId;
    }

    public OrderUnpaidTask(Integer orderId){
        super("OrderUnpaidTask-" + orderId, SystemConfig.getOrderUnpaid() * 60 * 1000);
        this.orderId = orderId;
    }

    @Override
    public void run() {
        logger.info("系统开始处理延时任务---订单超时未付款---" + this.orderId);

        LgqsOrderService orderService = BeanUtil.getBean(LgqsOrderService.class);
        LgqsOrderGoodsService orderGoodsService = BeanUtil.getBean(LgqsOrderGoodsService.class);
        LgqsGoodsProductService productService = BeanUtil.getBean(LgqsGoodsProductService.class);
        WxOrderService wxOrderService = BeanUtil.getBean(WxOrderService.class);

        LgqsOrder order = orderService.findById(this.orderId);
        if(order == null){
            return;
        }
        if(!OrderUtil.isCreateStatus(order)){
            return;
        }

        // 设置订单已取消状态
        order.setOrderStatus(OrderUtil.STATUS_AUTO_CANCEL);
        order.setEndTime(LocalDateTime.now());
        if (orderService.updateWithOptimisticLocker(order) == 0) {
            throw new RuntimeException("更新数据已失效");
        }

        // 餐品餐品数量增加
        Integer orderId = order.getId();
        List<LgqsOrderGoods> orderGoodsList = orderGoodsService.queryByOid(orderId);
        for (LgqsOrderGoods orderGoods : orderGoodsList) {
            Integer productId = orderGoods.getProductId();
            Short number = orderGoods.getNumber();
            if (productService.addStock(productId, number) == 0) {
                throw new RuntimeException("餐品餐品库存增加失败");
            }
        }

        //返还优惠券
        wxOrderService.releaseCoupon(orderId);

        logger.info("系统结束处理延时任务---订单超时未付款---" + this.orderId);
    }
}
