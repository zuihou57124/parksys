package com.qcw.parksys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class ParksysApplication {

    public static void main(String[] args) {
        System.setProperty("user.timezone","GMT +08");
        SpringApplication.run(ParksysApplication.class, args);
    }

}
