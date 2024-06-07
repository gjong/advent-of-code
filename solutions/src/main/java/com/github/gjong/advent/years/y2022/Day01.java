package com.github.gjong.advent.years.y2022;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.stream.Stream;

@Day(day = 1, year = 2022, name = "Calorie Counting")
public class Day01 implements DaySolver {
    private final InputLoader inputLoader = new InputLoader(2022, 1);
    private final Validator validator = new Validator(2022, 1);

    @Override
    public void part1() {
        var answer = Stream.of(inputLoader.split("\n\n"))
                .mapToInt(calories -> Stream.of(calories.split("\n"))
                        .mapToInt(Integer::valueOf)
                        .sum())
                .max()
                .orElse(0);

        validator.part1(answer);
    }

    @Override
    public void part2() {
        var answer = Math.abs(Stream.of(inputLoader.split("\n\n"))
                .mapToInt(calories -> Stream.of(calories.split("\n"))
                        .mapToInt(Integer::valueOf)
                        .sum())
                .map(i -> -i)
                .sorted()
                .limit(3)
                .sum());

        validator.part2(answer);
    }
}
