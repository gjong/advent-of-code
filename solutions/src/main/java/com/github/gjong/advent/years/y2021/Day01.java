package com.github.gjong.advent.years.y2021;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.jongsoft.lang.Collections;

@Day(day = 1, year = 2021, name = "Sonar Sweep")
public class Day01 implements DaySolver {
    private final InputLoader inputLoader;
    private final Validator validator;

    Day01(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void part1() {
        int totalIncreased = 0;
        var input = inputLoader.splitOnNewLineToInt().boxed().toList();
        for (int counter = 1; counter < input.size(); counter++) {
            if (input.get(counter - 1) < input.get(counter)) {
                totalIncreased++;
            }
        }

        validator.part1(totalIncreased);
    }

    @Override
    public void part2() {
        var increases = 0;
        var input = inputLoader.splitOnNewLineToInt().boxed().toList();
        for (var idx = 2; idx < input.size(); idx++) {
            var first = Collections.List(input.subList(Math.max(idx - 4, 0), idx - 1)).sum().get();
            var second = Collections.List(input.subList(Math.max(idx - 3, 0), idx)).sum().get();
            if (first < second) {
                increases++;
            }
        }

        validator.part2(increases - 1);
    }
}
