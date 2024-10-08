package com.github.gjong.advent;

import com.github.gjong.advent.cdi.Bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Bean
@Target({ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Day {
    int day();

    int year();

    String name() default "";
}
