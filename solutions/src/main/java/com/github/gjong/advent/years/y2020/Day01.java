package com.github.gjong.advent.years.y2020;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

@Day(year = 2020, day = 1, name = "Report Repair")
public class Day01 implements DaySolver {

    private final Validator validator;

    int[] numbers;

    Day01(InputLoader inputLoader, Validator validator) {
        this.validator = validator;

        numbers = inputLoader.splitOnNewLineToInt()
                .sorted()
                .toArray();
    }

    @Override
    public void part1() {
        // find two entries that add up to 2020
        for (int i = 0; i < numbers.length - 1; i++) {
            for (int j = i + 1; j < numbers.length; j++) {
                if (numbers[i] + numbers[j] == 2020) {
                    int product = numbers[i] * numbers[j];
                    validator.part1(product);
                    return;
                }
            }
        }

        validator.part1(1);
    }

    @Override
    public void part2() {
        // find three entries that add up to 2020
        for (int i = 0; i < numbers.length - 2; i++) {
            for (int j = i + 1; j < numbers.length - 1; j++) {
                for (int k = j + 1; k < numbers.length; k++) {
                    if (numbers[i] + numbers[j] + numbers[k] == 2020) {
                        int product = numbers[i] * numbers[j] * numbers[k];
                        validator.part2(product);
                        return;
                    }
                }
            }
        }
        validator.part1(0);
    }
}
