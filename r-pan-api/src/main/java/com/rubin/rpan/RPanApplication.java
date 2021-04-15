package com.rubin.rpan;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.rubin.rpan.**.dao")
@ServletComponentScan
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class RPanApplication {

    public static void main(String[] args) {
        SpringApplication.run(RPanApplication.class, args);
    }

}
