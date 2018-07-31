package com.netease.readfileutil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableScheduling
public class ReadFileUtilApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReadFileUtilApplication.class, args);
    }

}
