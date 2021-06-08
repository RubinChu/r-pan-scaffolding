package com.rubin.rpan.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 接口文档配置类
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@SpringBootConfiguration
public class Swagger2Config {

    private static final Logger log = LoggerFactory.getLogger(Swagger2Config.class);

    @Value("${swagger2.show}")
    private Boolean show;

    @Value("${swagger2.group-name}")
    private String groupName;

    @Value("${swagger2.base-package}")
    private String basePackage;

    @Value("${swagger2.title}")
    private String title;

    @Value("${swagger2.description}")
    private String description;

    @Value("${swagger2.terms-of-service-url}")
    private String termsOfServiceUrl;


    @Value("${swagger2.contact-name}")
    private String contactName;

    @Value("${swagger2.contact-url}")
    private String contactUrl;

    @Value("${swagger2.contact-email}")
    private String contactEmail;

    @Value("${swagger2.version}")
    private String version;

    @Bean
    public Docket orderRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .enable(show)
                .groupName(groupName)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build();
        log.debug("R Pan接口文档配置完毕！");
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .termsOfServiceUrl(termsOfServiceUrl)
                .contact(new Contact(contactName, contactUrl, contactEmail))
                .version(version)
                .build();
    }

}
