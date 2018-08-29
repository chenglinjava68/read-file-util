package com.netease.readfileutil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling//后续需要使用
@EnableAsync//开启异步支持
public class ReadFileUtilApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReadFileUtilApplication.class, args);
    }

}
