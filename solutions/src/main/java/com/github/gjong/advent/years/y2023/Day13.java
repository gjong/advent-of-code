package com.github.gjong.advent.years.y2023;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.CharGrid;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.List;
import java.util.stream.Stream;

@Day(day = 13, year = 2023, name = "Point of Incidence")
public class Day13 implements DaySolver {
    private final InputLoader inputLoader = new InputLoader(2023, 13);
    private final Validator validator = new Validator(2023, 13);

    private static final int FACTOR_ROWS = 100;

    @Override
    public void part1() {
        var answer = solve(parseInput(inputLoader.string()), 0);
        validator.part1(answer);
    }

    @Override
    public void part2() {
        var answer = solve(parseInput(inputLoader.string()), 1);
        validator.part2(answer);
    }

    public long solve(List<CharGrid> grids, int errors) {
        int answer = 0;
        for (var grid : grids) {
            answer += calculateReflections(grid, errors, FACTOR_ROWS);
            answer += calculateReflections(grid.transpose(), errors, 1);
        }
        return answer;
    }

    private int calculateReflections(CharGrid grid, int errors, int factor) {
        int result = 0;
        for (var i = 1; i < grid.rows(); i++) {
            if (checkReflections(grid, i, errors)) {
                result += factor * i;
            }
        }
        return result;
    }

    private boolean checkReflections(CharGrid pattern, int position, int allowedErrors) {
        var errors = 0;
        for (int i = position - 1, j = position; i >= 0 && j < pattern.rows(); i--, j++) {
            for (var column = 0; column < pattern.cols(); column++) {
                if (pattern.row(j)[column] != pattern.row(i)[column] && ++errors > allowedErrors) {
                    return false;
                }
            }
        }
        return errors == allowedErrors;
    }

    private List<CharGrid> parseInput(String input) {
        return Stream.of(input.split("\n\n"))
                .map(CharGrid::new)
                .toList();
    }
}
