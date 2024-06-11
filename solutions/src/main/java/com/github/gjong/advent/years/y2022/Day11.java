package com.github.gjong.advent.years.y2022;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

@Day(day = 11, year = 2022, name = "Monkey in the Middle")
public class Day11 implements DaySolver {
    private final InputLoader inputLoader = DayLoader.inputDay11();
    private final Validator validator = DayLoader.validatorDay11();

    @Override
    public void part1() {
        validator.part1(solve(b -> (long) Math.floor(b / 3.0), 20));
    }

    @Override
    public void part2() {
        validator.part2(solve(b -> b, 10000));
    }

    private long solve(Function<Long, Long> worryReducer, int numberOfRounds) {
        var monkeys = Stream.of(inputLoader.split("\n\n"))
                .map(raw -> Monkey.of(raw, worryReducer))
                .toList();

        var maxNumber = monkeys.stream()
                .mapToLong(m -> m.getResolution().divisible())
                .reduce((left, right) -> left * right)
                .orElse(1);

        for (var round = 0; round < numberOfRounds; round++) {
            for (var monkey : monkeys) {
                monkey.processRound()
                        .forEach(instruction ->
                                monkeys.get(instruction.toMonkey())
                                        .addWorry(instruction.worryLevel() % maxNumber));
            }
        }

        return monkeys.stream()
                .map(Monkey::getNumberOfInspections)
                .map(x -> -x)
                .sorted()
                .limit(2)
                .map(Math::abs)
                .reduce((left, right) -> left * right)
                .orElse(0L);
    }

    static class Monkey {

        record Resolution(long divisible, int throwIfTrue, int throwIfFalse) {
            Instruction resolve(long worry) {
                var target = worry % divisible == 0 ? throwIfTrue : throwIfFalse;
                return new Instruction(target, worry);
            }
        }

        record Instruction(int toMonkey, long worryLevel) {
        }

        private List<Long> items;

        private Resolution resolution;

        private Function<Long, Long> operation;

        private Function<Long, Long> worryReducer;

        private long numberOfInspections;

        public Monkey() {
            items = new ArrayList<>();
            numberOfInspections = 0;
        }

        public void addWorry(long worry) {
            this.items.add(worry);
        }

        public List<Instruction> processRound() {
            numberOfInspections += items.size();
            var instructions = items.stream()
                    .map(x -> operation.apply(x))
                    .map(worryReducer)
                    .map(x -> resolution.resolve(x))
                    .toList();
            items.clear();
            return instructions;
        }

        public long getNumberOfInspections() {
            return numberOfInspections;
        }

        public Resolution getResolution() {
            return resolution;
        }

        static Monkey of(String monkeyString, Function<Long, Long> worryReducer) {
            var parts = monkeyString.split("\n");
            var monkey = new Monkey();

            monkey.worryReducer = worryReducer;
            monkey.items = new ArrayList<>(Stream.of(parts[1].substring(18).split(", "))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .toList());

            var operation = parts[2].substring(19).split(" ");
            monkey.operation = value -> {
                var left = "old".equals(operation[0]) ? value : parseLong(operation[0]);
                var right = "old".equals(operation[2]) ? value : parseLong(operation[2]);

                return switch (operation[1]) {
                    case "+" -> left + right;
                    case "-" -> left - right;
                    case "*" -> left * right;
                    default -> throw new IllegalArgumentException("Cannot understand " + operation[1]);
                };
            };

            var division = parseInt(parts[3].substring(21));
            var trueMonkey = parseInt(parts[4].substring(29));
            var falseMonkey = parseInt(parts[5].substring(30));
            monkey.resolution = new Resolution(division, trueMonkey, falseMonkey);

            return monkey;
        }
    }
}
