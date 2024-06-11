package com.github.gjong.advent.years.y2021;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

@Day(day = 1, year = 2021, name = "Sonar Sweep")
public class Day01 implements DaySolver {
    private final InputLoader inputLoader = DayLoader.inputDay1();
    private final Validator validator = DayLoader.validatorDay1();

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
        for (var idx = 4; idx < input.size(); idx++) {
            if (input.get(idx - 4) < input.get(idx)) {
                increases++;
            }
        }

        validator.part2(increases - 1);
    }
}
