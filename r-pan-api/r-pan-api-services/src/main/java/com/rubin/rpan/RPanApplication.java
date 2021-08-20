package com.rubin.rpan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.rubin")
@EnableTransactionManagement
@MapperScan("com.rubin.rpan.modules.**.dao")
@ServletComponentScan(basePackages = "com.rubin")
public class RPanApplication {

    public static void main(String[] args) {
        SpringApplication.run(RPanApplication.class, args);
    }

}
