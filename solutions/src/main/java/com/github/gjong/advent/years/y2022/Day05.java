package com.github.gjong.advent.years.y2022;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;

@Day(day = 5, year = 2022, name = "Supply Stacks")
public class Day05 implements DaySolver {
    private final InputLoader inputLoader = new InputLoader(2022, 5);
    private final Validator validator = new Validator(2022, 5);

    private final Pattern moveExtractor = Pattern.compile("move\\s(?<count>\\d+)\\sfrom\\s(?<from>\\d+)\\sto\\s(?<to>\\d+)");
    private final List<Stack<String>> columns = new ArrayList<>();

    @Override
    public void part1() {
        boolean processing = false;
        var input = inputLoader.scanner();
        while (input.hasNextLine()) {
            var line = input.nextLine();

            if (line.isBlank()) {
                // blank line means the moves start on the next line
                processing = true;
                continue;
            }

            if (processing) {
                processMove(line);
            } else {
                processStackLine(line);
            }
        }

        validator.part1(columns.stream()
                .map(Stack::pop)
                .collect(Collectors.joining()));
    }

    @Override
    public void part2() {
        boolean processing = false;
        var input = inputLoader.scanner();
        while (input.hasNextLine()) {
            var line = input.nextLine();

            if (line.isBlank()) {
                // blank line means the moves start on the next line
                processing = true;
                continue;
            }

            if (processing) {
                processMove2(line);
            } else {
                processStackLine(line);
            }
        }

        validator.part2(columns.stream()
                .map(Stack::pop)
                .collect(Collectors.joining()));
    }

    private void processMove(String line) {
        var matcher = moveExtractor.matcher(line);
        if (matcher.find()) {
            var moveBoxes = parseInt(matcher.group("count"));
            var fromColumn = parseInt(matcher.group("from"));
            var toColumn = parseInt(matcher.group("to"));

            IntStream.range(0, moveBoxes)
                    .forEach(x -> columns.get(toColumn - 1)
                            .push(columns.get(fromColumn - 1).pop()));
        }
    }

    private void processMove2(String line) {
        var matcher = moveExtractor.matcher(line);
        if (matcher.find()) {
            var moveBoxes = parseInt(matcher.group("count"));
            var fromColumn = parseInt(matcher.group("from"));
            var toColumn = parseInt(matcher.group("to"));

            var boxesInReverse = IntStream.range(0, moveBoxes)
                    .mapToObj(x -> columns.get(fromColumn - 1).pop())
                    .toList();

            IntStream.range(0, moveBoxes)
                    .forEach(x -> columns.get(toColumn - 1).push(boxesInReverse.get(moveBoxes - 1 - x)));
        }
    }

    private void processStackLine(String line) {
        var columnIdx = 0;
        var remainder = line;
        while (!remainder.isEmpty()) {
            if (columns.size() < (columnIdx + 1)) {
                columns.add(new Stack<>());
            }

            var column = remainder.substring(0, Math.min(4, remainder.length()));
            remainder = remainder.substring(column.length());
            if (column.contains("[")) {
                columns.get(columnIdx)
                        .insertElementAt(column.substring(1,2), 0);
            }

            columnIdx++;
        }
    }
}
