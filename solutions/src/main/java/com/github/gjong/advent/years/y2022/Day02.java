package com.github.gjong.advent.years.y2022;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

@Day(day = 2, year = 2022, name = "Rock Paper Scissors")
public class Day02 implements DaySolver {
    private final InputLoader inputLoader;
    private final Validator validator;

    public Day02(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void part1() {
        var answer = inputLoader.splitOnNewLine()
                .mapToInt(line -> solveLine(line.split(" ")))
                .sum();
        validator.part1(answer);
    }

    @Override
    public void part2() {
        var answer = inputLoader.splitOnNewLine()
                .mapToInt(line -> solvePart2(line.split(" ")))
                .sum();
        validator.part2(answer);
    }

    private int solveLine(String[] split) {
        var elfMove = split[0].charAt(0) - 64;
        var myMove = split[1].charAt(0) - 87;

        if (elfMove == myMove) {
            return 3 + myMove;
        }

        var winningMove = (elfMove + 1) % 4;
        if (winningMove == 0) {
            winningMove = 1;
        }

        if (winningMove == myMove) {
            return 6 + myMove;
        }

        return myMove;
    }

    private int solvePart2(String[] split) {
        var elfMove     = split[0].charAt(0) - 64;
        var matchResult = split[1].charAt(0) - 89;

        var move = computeMove(elfMove + matchResult);

        if (matchResult == 0) {
            return 3 + move;
        }
        if (matchResult == 1) {
            return 6 + move;
        }
        return move;
    }

    private int computeMove(int move) {
        return switch (move) {
            case 4 -> 1;
            case 0 -> 3;
            default -> move;
        };
    }
}
