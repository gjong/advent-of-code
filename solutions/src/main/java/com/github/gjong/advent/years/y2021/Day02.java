package com.github.gjong.advent.years.y2021;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.stream.Collectors;

@Day(day = 2, year = 2021, name = "Dive!")
public class Day02 implements DaySolver {
    private final InputLoader inputLoader = DayLoader.inputDay2();
    private final Validator validator = DayLoader.validatorDay2();

    @Override
    public void part1() {
        var answer = inputLoader.splitOnNewLine()
                .map(line -> {
                    var split = line.split(" ");
                    return new Instruction(Action.valueOf(split[0]), Integer.parseInt(split[1]));
                })
                .collect(
                        Collectors.teeing(
                                Collectors.summarizingLong(
                                        instruction -> switch (instruction.action) {
                                            case forward -> instruction.amount;
                                            default -> 0;
                                        }
                                ),
                                Collectors.summarizingLong(
                                        instruction -> switch (instruction.action) {
                                            case up, down -> (long) instruction.amount * instruction.action.getDirection();
                                            default -> 0;
                                        }),
                                (forward, upDown) -> forward.getSum() * upDown.getSum()));

        validator.part1(answer);
    }

    @Override
    public void part2() {
        var depth = 0L;
        var horizontal = 0L;
        var aim = 0L;

        for (String rawInstruction : inputLoader.splitOnNewLine().toList()) {
            var instruction = rawInstruction.split("\s");
            var action = Action.valueOf(instruction[0]);
            var amount = Integer.parseInt(instruction[1]);

            switch (action) {
                case up, down -> aim += (long) action.getDirection() * amount;
                case forward -> {
                    depth += aim * amount;
                    horizontal += amount;
                }
            }
        }

        validator.part2(depth * horizontal);
    }

    private record Instruction(Action action, int amount) {
    }

    public enum Action {
        forward(1),
        down(1),
        up(-1);

        private final int direction;

        Action(int direction) {
            this.direction = direction;
        }

        public int getDirection() {
            return direction;
        }
    }

}
