package com.github.gjong.advent.cdi;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {

    boolean singleton() default true;
}
