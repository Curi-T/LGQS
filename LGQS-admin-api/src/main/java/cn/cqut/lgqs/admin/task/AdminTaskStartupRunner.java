package cn.cqut.lgqs.admin.task;

import cn.cqut.lgqs.core.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author CuriT
 */
@Component
public class AdminTaskStartupRunner implements ApplicationRunner {

    @Autowired
    private TaskService taskService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
    }
}