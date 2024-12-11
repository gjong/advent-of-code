package com.github.gjong.advent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface LimitRuns {
    /**
     * For solutions that take a longer amount of time we want to limit the number of executions no matter what the
     * user inputs.
     *
     * @return maximum amount of runs for this solution.
     */
    int value() default 1;
}
