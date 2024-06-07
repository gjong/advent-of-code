package com.github.gjong.advent.years.y2023;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Day(day = 6, year = 2023, name = "Wait For It")
public class Day06 implements DaySolver {

    private final InputLoader inputLoader = new InputLoader(2023, 6);
    private final Validator validator = new Validator(2023, 6);

    @Override
    public void part1() {
        var parsed = parseInput(inputLoader.string());

        var totalWaysToWin = 1L;
        for (var x = 0; x < parsed.get(0).length; x++) {
            var time = parsed.get(0)[x];
            var distance = parsed.get(1)[x];

            var waysToWin = computeWaysToWin(time, distance);
            LoggerFactory.getLogger(getClass()).debug("Time: {}, Distance: {}, Ways to win: {}", time, distance, waysToWin);
            totalWaysToWin *= waysToWin;
        }

        validator.part1(totalWaysToWin);
    }

    @Override
    public void part2() {
        var lines = inputLoader.split("\n");

        var time = lines[0].substring("Time:".length()).replaceAll("\\s", "");
        var distance = lines[1].substring("Distance:".length()).replaceAll("\\s", "");

        var answer = computeWaysToWin(Long.parseLong(time), Long.parseLong(distance));
        validator.part2(answer);
    }

    private int computeWaysToWin(long time, long distance) {
        var d = Math.sqrt(time * time - 4.0 * distance);
        var min = Math.floor(.5 * (time - d)) + 1;
        var max = Math.ceil(.5 * (time + d)) - 1;
        return (int) (max - min + 1);
    }

    private List<Integer[]> parseInput(String input) {
        var pattern = Pattern.compile("(\\d+)");

        return input.lines()
                .map(pattern::matcher)
                .map(matcher -> {
                    var numbers = new ArrayList<Integer>();
                    while (matcher.find()) {
                        numbers.add(Integer.parseInt(matcher.group(1)));
                    }
                    return numbers.toArray(new Integer[0]);
                })
                .toList();
    }
}
