package com.github.gjong.advent.years.y2024;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

@Day(year = 2024, day = 19, name = "Linen Layout")
public class Day19 implements DaySolver {

    private final InputLoader inputLoader;
    private final Validator validator;

    public Day19(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void part1() {
        var inputLines = new ArrayList<>(Arrays.asList(inputLoader.split("\n")));

        var pattern = buildRegex(inputLines.removeFirst());
        inputLines.removeFirst();

        var matchingLines = inputLines.stream()
                .filter(line -> pattern.matcher(line).matches())
                .count();

        validator.part1(matchingLines);
    }

    @Override
    public void part2() {
        var inputLines = new ArrayList<>(Arrays.asList(inputLoader.split("\n")));

        var towels = Arrays.asList(inputLines.removeFirst().split(", "));
        inputLines.removeFirst();

        var answer = inputLines.stream()
                .mapToLong(line -> countPossibilities(line, towels, new HashMap<>()))
                .sum();

        validator.part2(answer);
    }

    private Pattern buildRegex(String line) {
        var patternOptions = String.join("|", line.split(", "));
        return Pattern.compile("(" + patternOptions + ")+");
    }

    private long countPossibilities(String line, List<String> towels, HashMap<String, Long> memo) {
        if (memo.containsKey(line)) {
            return memo.get(line);
        }

        var count = 0L;
        for (var towel : towels) {
            if (line.startsWith(towel)) {
                var remaining = line.substring(towel.length());
                if (remaining.isEmpty()) {
                    count++;
                } else {
                    count += countPossibilities(remaining, towels, memo);
                }
            }
        }

        memo.put(line, count);
        return count;
    }

}
