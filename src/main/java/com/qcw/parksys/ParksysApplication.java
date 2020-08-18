package com.qcw.parksys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//事务管理
@EnableTransactionManagement
//开启定时任务
@EnableScheduling
//过滤器扫描
@ServletComponentScan
public class ParksysApplication {

    public static void main(String[] args) {
        System.setProperty("user.timezone","GMT +08");
        SpringApplication.run(ParksysApplication.class, args);
    }

}
