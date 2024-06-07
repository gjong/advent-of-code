package com.github.gjong.advent.years.y2022;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.LinkedList;

@Day(day = 6, year = 2022, name = "Tuning Trouble")
public class Day06 implements DaySolver {
    private final InputLoader inputLoader = new InputLoader(2022, 6);
    private final Validator validator = new Validator(2022, 6);

    @Override
    public void part1() {
        validator.part1(solve(4));
    }

    @Override
    public void part2() {
        validator.part2(solve(14));
    }

    private int solve(int uniqueCharsRequired) {
        var investigate = new LinkedList<Character>();

        var letterIdx = 1;
        for (var letter : inputLoader.chars()) {
            investigate.add(letter);
            if (investigate.size() > uniqueCharsRequired) {
                investigate.poll();
            }

            if (isAllUnique(investigate, uniqueCharsRequired)) {
                return letterIdx;
            }
            letterIdx++;
        }
        return -1;
    }

    private boolean isAllUnique(LinkedList<?> entries, int uniqueCharsRequired) {
        return entries.stream().distinct().count() == uniqueCharsRequired;
    }
}
