package com.demosix.demosix.aop;


import java.lang.annotation.*;

/**
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface NoRepeat {


}
