package cn.cqut.lgqs.wx.task;

import cn.cqut.lgqs.core.system.SystemConfig;
import cn.cqut.lgqs.core.task.TaskService;
import cn.cqut.lgqs.db.domain.LgqsOrder;
import cn.cqut.lgqs.db.service.LgqsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class TaskStartupRunner implements ApplicationRunner {

    @Autowired
    private LgqsOrderService orderService;
    @Autowired
    private TaskService taskService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<LgqsOrder> orderList = orderService.queryUnpaid(SystemConfig.getOrderUnpaid());
        for(LgqsOrder order : orderList){
            LocalDateTime add = order.getAddTime();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expire =  add.plusMinutes(SystemConfig.getOrderUnpaid());
            if(expire.isBefore(now)) {
                // 已经过期，则加入延迟队列
                taskService.addTask(new OrderUnpaidTask(order.getId(), 0));
            }
            else{
                // 还没过期，则加入延迟队列
                long delay = ChronoUnit.MILLIS.between(now, expire);
                taskService.addTask(new OrderUnpaidTask(order.getId(), delay));
            }
        }
    }
}