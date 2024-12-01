package com.github.gjong.advent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation to mark an Advent of Code solution with.
 * The combination of the {@link #year()} and {@link #day()} are used to inject the correct input file and
 * result file.
 * <p>
 * The {@link com.github.gjong.advent.processor.DayProcessor} will automatically create an injectable
 * {@link com.github.gjong.advent.cdi.CdiBean bean} version of this exercise class.
 */
@Target({ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Day {

    /**
     * The day this AoC exercise is for.
     *
     * @return the day number
     */
    int day();

    /**
     * The year this AoC exercise is part of.
     *
     * @return the year.
     */
    int year();

    /**
     * The name of the exercise as it is described in AoC.
     *
     * @return the exercise name in AoC.
     */
    String name() default "";
}
