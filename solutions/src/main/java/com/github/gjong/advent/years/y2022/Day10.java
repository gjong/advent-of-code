package com.github.gjong.advent.years.y2022;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

@Day(day = 10, year = 2022, name = "Cathode-Ray Tube")
public class Day10 implements DaySolver {
    private final InputLoader inputLoader = new InputLoader(2022, 10);
    private final Validator validator = new Validator(2022, 10);

    private static final int MAX_CYCLE = 220;
    public static final String ADD_INST = "addx ";
    private static final int CYCLES_PER_ROW = 40;

    @Override
    public void part1() {
        var answer = 0;
        var x = 1;
        var cycle = 0;
        var input = inputLoader.scanner();
        while (cycle <= MAX_CYCLE && input.hasNextLine()) {
            var instruction = input.nextLine();
            var instructionCost = 1;
            var addition = 0;

            if (instruction.startsWith(ADD_INST)) {
                addition = parseInt(instruction.substring(ADD_INST.length()));
                instructionCost = 2;
            }

            var interestedInCycle = cycleOfInterest(cycle, instructionCost);
            if (interestedInCycle > -1) {
                answer += x * interestedInCycle;
            }

            x += addition;
            cycle += instructionCost;
        }

        validator.part1(answer);
    }

    @Override
    public void part2() {
        var currentCycle = 0;
        var x = 1;

        var lastInstruction = "";
        var readNextLine = true;
        var screen = new char[6][40];
        var input = inputLoader.scanner();
        while (currentCycle <= 240 && input.hasNextLine()) {
            var row = currentCycle / CYCLES_PER_ROW;
            if (readNextLine) {
                lastInstruction = input.nextLine();
            }

            var drawPosition = currentCycle % CYCLES_PER_ROW;
            var drawSymbol = '.';
            if ((x - 1) <= drawPosition && (x + 1) >= drawPosition) {
                drawSymbol = '#';
            }
            screen[row][drawPosition] = drawSymbol;

            if (lastInstruction.startsWith(ADD_INST)) {
                if (!readNextLine) {
                    x += parseInt(lastInstruction.substring(ADD_INST.length()));
                }

                readNextLine = !readNextLine;
            }

            currentCycle++;
        }

        var response = Stream.of(screen)
                .map(line -> Stream.of(line)
                        .map(String::valueOf)
                        .collect(Collectors.joining("")))
                .collect(Collectors.joining("\n"));

        validator.part2(response);
    }

    private int cycleOfInterest(int cycle, int nextCost) {
        // interested in 20, 60, 100, 140, 180, 220
        return Stream.of(20, 60, 100, 140, 180, 220)
                .filter(x -> cycle < x && (cycle + nextCost) >= x)
                .findFirst()
                .orElse(-1);
    }
}
