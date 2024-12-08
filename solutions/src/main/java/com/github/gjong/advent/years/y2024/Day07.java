package com.github.gjong.advent.years.y2024;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Day(year = 2024, day = 7, name = "Bridge Repair")
public class Day07 implements DaySolver {

    private final Logger log = org.slf4j.LoggerFactory.getLogger(Day07.class);

    private final InputLoader inputLoader;
    private final Validator validator;

    public Day07(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void part1() {
        var totalCalibrationResult = inputLoader.splitOnNewLine()
                .mapToLong(line -> computeLine(line, this::withPlusAndMultiply))
                .sum();

        validator.part1(totalCalibrationResult);
    }

    @Override
    public void part2() {
        var totalCalibrationResult = inputLoader.splitOnNewLine()
                .mapToLong(line -> computeLine(line, this::withPlusAndMultiplyAndAppend))
                .sum();

        validator.part2(totalCalibrationResult);
    }

    private long computeLine(String line, Function<List<Instruction>, List<Instruction>> computeFunction) {
        var split = line.split(": ");
        var answer = Long.parseLong(split[0]);
        var numbers = Arrays.asList(split[1].split(" "));
        Collections.reverse(numbers);

        return filteredPossibilities(answer, numbers, computeFunction);
    }

    interface Instruction {
        long solve();
    }

    record LiteralValue(String number) implements Instruction {
        @Override
        public long solve() {
            return Long.parseLong(number);
        }

        @Override
        public String toString() {
            return number;
        }
    }

    record Addition(Instruction left, Instruction right) implements Instruction {
        @Override
        public long solve() {
            return left.solve() + right.solve();
        }

        @Override
        public String toString() {
            return left.toString() + "+" + right.toString();
        }
    }

    record Multiplication(Instruction left, Instruction right) implements Instruction {
        @Override
        public long solve() {
            return left.solve() * right.solve();
        }

        @Override
        public String toString() {
            return left.toString() + "*" + right.toString();
        }
    }

    record Append(Instruction left, Instruction right) implements Instruction {
        @Override
        public long solve() {
            return Long.parseLong("%d%d".formatted(right.solve(), left.solve()));
        }

        @Override
        public String toString() {
            return right.toString() + "||" + left.toString();
        }
    }

    private long filteredPossibilities(long answer, List<String> numbers, Function<List<Instruction>, List<Instruction>> computeFunction) {
        var literalsInOrder = numbers.stream()
                .<Instruction>map(LiteralValue::new)
                .toList();

        var posibilities = computeFunction.apply(literalsInOrder);
        log.trace("Found solutions for {}: {}", answer, posibilities);
        for (var possibility : posibilities) {
            if (possibility.solve() == answer) {
                log.debug("Found match for {}: {}", answer, possibility);
                return answer;
            }
        }

        return 0;
    }

    private List<Instruction> withPlusAndMultiply(List<Instruction> numbers) {
        if (numbers.size() == 1) {
            return List.of(numbers.getFirst());
        }

        var calculations = new ArrayList<Instruction>();
        for (var partial : withPlusAndMultiply(numbers.subList(1, numbers.size()))) {
            calculations.add(new Addition(numbers.getFirst(), partial));
            calculations.add(new Multiplication(numbers.getFirst(), partial));
        }
        return calculations;
    }

    private List<Instruction> withPlusAndMultiplyAndAppend(List<Instruction> numbers) {
        if (numbers.size() == 1) {
            return List.of(numbers.getFirst());
        }

        var calculations = new ArrayList<Instruction>();
        for (var partial : withPlusAndMultiplyAndAppend(numbers.subList(1, numbers.size()))) {
            calculations.add(new Addition(numbers.getFirst(), partial));
            calculations.add(new Multiplication(numbers.getFirst(), partial));
            calculations.add(new Append(numbers.getFirst(), partial));
        }
        return calculations;
    }

}
