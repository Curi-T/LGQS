package cn.cqut.lgqs.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"cn.cqut.lgqs.db", "cn.cqut.lgqs.core",
        "cn.cqut.lgqs.admin"})
@MapperScan("cn.cqut.lgqs.db.dao")
@EnableTransactionManagement
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}