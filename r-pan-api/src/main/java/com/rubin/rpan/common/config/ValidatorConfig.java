package com.rubin.rpan.common.config;

import com.rubin.rpan.common.constant.CommonConstant;
import org.hibernate.validator.HibernateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * Validator配置类
 * 配置快速失败模式，即校验一个失败则不在校验其他参数
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@SpringBootConfiguration
public class ValidatorConfig {

    private static final Logger log = LoggerFactory.getLogger(ValidatorConfig.class);

    /**
     * 快速失败key
     */
    private static final String FAIL_FAST_KEY = "hibernate.validator.fail_fast";

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        postProcessor.setValidator(validator());
        log.debug("RPan参数验证器配置完毕！");
        return postProcessor;
    }

    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .addProperty(FAIL_FAST_KEY, CommonConstant.TRUE_STR)
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        return validator;
    }

}
