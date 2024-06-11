package com.github.gjong.advent.years.y2022;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Day(day = 3, year = 2022, name = "Rucksack Reorganization")
public class Day03 implements DaySolver {
    private final InputLoader inputLoader = DayLoader.inputDay3();
    private final Validator validator = DayLoader.validatorDay3();

    @Override
    public void part1() {
        var answer = inputLoader.splitOnNewLine()
                .mapToInt(this::solveLine)
                .sum();
        validator.part1(answer);
    }

    @Override
    public void part2() {
        var input = inputLoader.splitOnNewLine().toList();
        var groups = input.size() / 3;

        var solution = 0;
        for (var group = 0; group < groups; group++) {
            solution += solveGroup(
                    input.subList(group * 3, (group + 1) *3));
        }

        validator.part2(solution);
    }

    private int solveLine(String line) {
        var compartmentSize = line.length() / 2;

        var compartmentOne = line.substring(0, compartmentSize);
        var compartmentTwo = line.substring(compartmentSize);

        return overlap(compartmentOne, compartmentTwo)
                .mapToInt(this::computeBadgeScore)
                .limit(1)
                .sum();
    }

    private int solveGroup(List<String> rucksacks) {
        var overlap = overlap(
                rucksacks.get(0),
                rucksacks.get(1)
        ).collect(Collectors.joining());

        return overlap(overlap, rucksacks.get(2))
                .mapToInt(this::computeBadgeScore)
                .limit(1)
                .sum();
    }

    private Stream<String> overlap(String rucksackOne, String rucksackTwo) {
        var rightElements = List.of(rucksackOne.split(""));

        return Stream.of(rucksackTwo.split(""))
                .filter(rightElements::contains);
    }

    private int computeBadgeScore(String item) {
        var value = item.charAt(0);

        if (value >= 'A' && value <= 'Z') {
            return value - 'A' + 27;
        }

        return value - 'a' + 1;
    }
}
