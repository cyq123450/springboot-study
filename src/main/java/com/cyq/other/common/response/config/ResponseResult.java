package com.cyq.other.common.response.config;

import java.lang.annotation.*;

/**
 * 自定义注解类
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface ResponseResult {
}
