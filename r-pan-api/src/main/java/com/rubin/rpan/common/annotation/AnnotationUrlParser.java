package com.rubin.rpan.common.annotation;

import java.util.List;

/**
 * URL解析器
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public interface AnnotationUrlParser {

    /**
     * 获取注解的所有URL
     *
     * @return
     */
    List<String> getAnnotationUrls();

}
