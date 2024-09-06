package com.github.gjong.advent.years.y2023;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Day(day = 9, year = 2023, name = "Mirage Maintenance")
public class Day09 implements DaySolver {
    private final InputLoader inputLoader;
    private final Validator validator;

    public Day09(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void part1() {
        var answer = inputLoader.splitOnNewLine()
                .map(this::parseLine)
                .mapToLong(this::solveRight)
                .sum();
        validator.part1(answer);
    }

    @Override
    public void part2() {
        var answer = inputLoader.splitOnNewLine()
                .map(this::parseLine)
                .mapToLong(this::solveLeft)
                .sum();
        validator.part2(answer);
    }

    private Long solveRight(List<Long> numbers) {
        if (numbers.stream().allMatch(n -> n == 0)) {
            return 0L;
        }

        return numbers.get(numbers.size() - 1) + solveRight(getIncrements(numbers));
    }

    private Long solveLeft(List<Long> numbers) {
        if (numbers.stream().allMatch(n -> n == 0)) {
            return 0L;
        }

        return numbers.get(0) - solveLeft(getIncrements(numbers));
    }

    private List<Long> getIncrements(List<Long> increments) {
        var result = new ArrayList<Long>();
        for (int i = 0; i < increments.size() - 1; i++) {
            result.add(increments.get(i + 1) - increments.get(i));
        }
        return result;
    }

    private List<Long> parseLine(String line) {
        return Arrays.stream(line.split("\\s+"))
                .map(Long::parseLong)
                .toList();
    }
}
