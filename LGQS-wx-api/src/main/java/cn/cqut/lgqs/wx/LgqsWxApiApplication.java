package cn.cqut.lgqs.wx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"cn.cqut.lgqs.db", "cn.cqut.lgqs.core", "cn.cqut.lgqs.wx"})
@MapperScan("cn.cqut.lgqs.db.dao")
@EnableTransactionManagement
@EnableScheduling
public class LgqsWxApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LgqsWxApiApplication.class, args);
    }

}