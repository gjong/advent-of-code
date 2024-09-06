package com.github.gjong.advent.years.y2023;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.HashMap;
import java.util.Optional;

@Day(day = 1, year = 2023, name = "Trebuchet?!")
public class Day01 implements DaySolver {

    private final InputLoader inputLoader;
    private final Validator validator;

    public Day01(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void part1() {
        var answer = inputLoader.splitOnNewLine()
                .map(this::getBoundInts)
                .mapToInt(Integer::parseInt)
                .sum();

        validator.part1(answer);
    }

    @Override
    public void part2() {
        var answer = inputLoader.splitOnNewLine()
                .map(this::parseLine)
                .mapToInt(Integer::parseInt)
                .sum();

        validator.part2(answer);
    }

    private String getBoundInts(String line) {
        var numbers = line.chars()
                .filter(Character::isDigit)
                .mapToObj(Character::toString)
                .toList();

        return numbers.getFirst() + numbers.getLast();
    }

    private String parseLine(String line) {
        var map = new HashMap<String, String>();
        map.put("1", "1");
        map.put("2", "2");
        map.put("3", "3");
        map.put("4", "4");
        map.put("5", "5");
        map.put("6", "6");
        map.put("7", "7");
        map.put("8", "8");
        map.put("9", "9");
        map.put("one", "1");
        map.put("two", "2");
        map.put("three", "3");
        map.put("four", "4");
        map.put("five", "5");
        map.put("six", "6");
        map.put("seven", "7");
        map.put("eight", "8");
        map.put("nine", "9");

        var firstNumber = Optional.<String>empty();
        var lastNumber = "";

        var parsing = line;
        while (!parsing.isEmpty()) {
            for (var entry : map.entrySet()) {
                if (parsing.startsWith(entry.getKey())) {
                    if (firstNumber.isEmpty()) {
                        firstNumber = Optional.of(entry.getValue());
                    } else {
                        lastNumber = entry.getValue();
                    }
                    break;
                }
            }

            parsing = parsing.substring(1);
        }

        if (lastNumber.isBlank()) {
            lastNumber = firstNumber.get();
        }

        return firstNumber.get() + lastNumber;
    }
}
