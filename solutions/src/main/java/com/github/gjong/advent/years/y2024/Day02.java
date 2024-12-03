package com.github.gjong.advent.years.y2024;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Day(year = 2024, day = 2, name = "Red-Nosed Reports")
public class Day02 implements DaySolver {
    private final Logger log = LoggerFactory.getLogger(Day02.class);

    private final InputLoader inputLoader;
    private final Validator validator;

    public Day02(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void part1() {
        var safe = inputLoader.splitOnNewLine()
                .map(this::convertLine)
                .filter(this::lineIsSafe)
                .count();

        validator.part1(safe);
    }

    @Override
    public void part2() {
        var safe = inputLoader.splitOnNewLine()
                .map(this::convertLine)
                .filter(this::lineIsSafeWithTolerance)
                .count();

        validator.part2(safe);
    }

    private boolean lineIsSafeWithTolerance(List<Integer> line) {
        if (lineIsSafe(line)) {
            return true;
        }

        for (int i = 0; i < line.size(); i++) {
            // Create a copy of the list without the element at position 'i'
            var modifiedLevels = new ArrayList<>(line);
            modifiedLevels.remove(i);
            // Check if the modified list is strictly decreasing
            if (lineIsSafe(modifiedLevels)) {
                return true;
            }
        }
        return false;
    }

    private List<Integer> convertLine(String line) {
        return Arrays.stream(line.split(" +"))
                .map(Integer::parseInt)
                .toList();
    }

    private boolean lineIsSafe(List<Integer> line) {
        var previous = line.get(0);
        var increasing = previous < line.get(1);
        for (var x = 1; x < line.size(); x++) {
            if (invalidIncrease(increasing, previous, line.get(x))) {
                return false;
            }

            previous = line.get(x);
        }

        return true;
    }

    private boolean invalidIncrease(boolean increasing, int left, int right) {
        var difference = Math.abs(left - right);
        var stillIncreasing = left < right;
        return (difference < 1 || difference > 3) || (stillIncreasing != increasing);
    }
}
