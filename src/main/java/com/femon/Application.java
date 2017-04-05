package com.femon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 一剑
 */

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.femon")
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        System.out.println();
        System.out.println();
        System.out.println("**************************** 业务服务监控系统 FEMON STARTED **************************");

        // 启动Spring Boot项目的唯一入口
        SpringApplication.run(Application.class, args);

    }

}
