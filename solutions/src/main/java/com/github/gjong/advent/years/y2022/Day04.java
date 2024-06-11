package com.github.gjong.advent.years.y2022;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

@Day(day = 4, year = 2022, name = "Camp Cleanup")
public class Day04 implements DaySolver {
    private final InputLoader inputLoader = DayLoader.inputDay4();
    private final Validator validator = DayLoader.validatorDay4();

    @Override
    public void part1() {
        var overlapped = 0;
        var input = inputLoader.scanner();
        while (input.hasNextLine()) {
            var sections = input.nextLine().split(",");
            if (sections.length != 2) continue;

            var range1 = parseRange(sections[0]);
            var range2 = parseRange(sections[1]);

            if (partialOverlap(range1, range2)) {
                overlapped++;
            }
        }

        validator.part1(overlapped);
    }

    @Override
    public void part2() {
        var overlapped = 0;
        var input = inputLoader.scanner();
        while (input.hasNextLine()) {
            var sections = input.nextLine().split(",");
            if (sections.length != 2) continue;

            var range1 = parseRange(sections[0]);
            var range2 = parseRange(sections[1]);

            if (fullOverlapped(range1, range2) || fullOverlapped(range2, range1)) {
                overlapped++;
            }
        }

        validator.part2(overlapped);
    }

    private boolean partialOverlap(int[] first, int[] second) {
        return first[0] <= second[1] && first[1] >= second[0];
    }

    private boolean fullOverlapped(int[] first, int[] second) {
        return first[1] <= second[1] && first[0] >= second[0];
    }

    private int[] parseRange(String range) {
        var rawRange = range.split("-");
        return new int[] {
                Integer.parseInt(rawRange[0]),
                Integer.parseInt(rawRange[1])
        };
    }
}
