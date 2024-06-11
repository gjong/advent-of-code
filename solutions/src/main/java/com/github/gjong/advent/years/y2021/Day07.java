package com.github.gjong.advent.years.y2021;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.jongsoft.lang.Collections;
import com.jongsoft.lang.collection.Sequence;

import java.util.stream.IntStream;

@Day(day = 7, year = 2021, name = "The Treachery of Whales")
public class Day07 implements DaySolver {
    private final InputLoader inputLoader = DayLoader.inputDay7();
    private final Validator validator = DayLoader.validatorDay7();

    @Override
    public void part1() {
        var crabPositions = parseData(inputLoader.split(","));

        var median = (int) crabPositions.median();
        var totalFuelUsed = crabPositions
                .map(position -> Math.abs(position - median))
                .sum()
                .get()
                .intValue();

        validator.part1(totalFuelUsed);
    }

    @Override
    public void part2() {
        var crabPositions = parseData(inputLoader.split(","));
        var average = crabPositions.average().get();

        var answer = Math.min(
                computeFuel(crabPositions, (int) Math.floor(average)),
                computeFuel(crabPositions, (int) Math.ceil(average)));

        validator.part2((long) answer);
    }

    private double computeFuel(Sequence<Integer> crabPositions, int position) {
        return crabPositions
                .map(crabPos -> Math.abs(crabPos - position))
                .map(steps -> IntStream.rangeClosed(1, steps).sum())
                .sum()
                .get();
    }

    private Sequence<Integer> parseData(String[] splitted) {
        return Collections.List(splitted)
                .map(Integer::parseInt)
                .sorted();
    }
}
