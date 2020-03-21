package com.rubin.rpan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.rubin.rpan.dao")
public class RPanApplication {

    public static void main(String[] args) {
        SpringApplication.run(RPanApplication.class, args);
    }

}
