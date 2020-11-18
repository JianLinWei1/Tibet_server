package com.mj.mainservice.annotation;

import java.lang.annotation.*;

/**
 * @auther JianLinWei
 * @date 2020-11-17 21:27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface SysLogInter {
        String value() default "";
}
