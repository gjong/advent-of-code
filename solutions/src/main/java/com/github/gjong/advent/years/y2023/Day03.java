package com.github.gjong.advent.years.y2023;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.*;

import static java.util.stream.Collectors.toSet;

@Day(year = 2023, day = 3)
public class Day03 implements DaySolver {
    private sealed interface Position permits NumberWithPos, SymbolPos {
    }

    record NumberWithPos(int number, int row, int startPos, int endPos) implements Position {
    }

    record SymbolPos(char c, int position) implements Position {
        boolean isGear() {
            return c == '*';
        }
    }

    record GearRatio(SymbolPos gear, NumberWithPos left, NumberWithPos right) {
    }

    private static final Set<Character> SYMBOLS = Set.of('!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+', '=', '|', '~', '`', '{', '}', '[', ']', ':', ';', '"', '\'', '<', '>', ',', '?', '/', '\\');
    private final InputLoader inputLoader = new InputLoader(2023, 3);
    private final Validator validator = new Validator(2023, 3);

    @Override
    public void part1() {
        int rowNr = 0;
        Scanner scanner = inputLoader.scanner();
        Queue<List<Position>> queue = new LinkedList<>();
        Set<NumberWithPos> relevantNumbers = new HashSet<>();
        while (scanner.hasNext()) {
            var currentRow = parseLine(rowNr, scanner.nextLine());
            queue.add(currentRow);

            // find the relevant numbers
            var iterationList = queue.stream().toList();
            for (var i = 0; i < iterationList.size(); i++) {
                var currentIdx = i;
                iterationList.get(i)
                        .stream()
                        .filter(pos -> pos instanceof SymbolPos)
                        .map(pos -> (SymbolPos) pos)
                        .forEach(pos -> {
                            if (currentIdx > 0) {
                                relevantNumbers.addAll(
                                        touching(iterationList.get(currentIdx - 1), pos.position()));
                            }
                            if (currentIdx < iterationList.size() - 1) {
                                relevantNumbers.addAll(
                                        touching(iterationList.get(currentIdx + 1), pos.position()));
                            }

                            relevantNumbers.addAll(
                                    touching(iterationList.get(currentIdx), pos.position()));
                        });
            }

            // keep at most 3 rows in the cache
            if (queue.size() > 3) {
                queue.poll();
            }
            rowNr++;
        }

        var answer = relevantNumbers.stream()
                .mapToLong(NumberWithPos::number)
                .sum();
        validator.part1(answer);
    }

    @Override
    public void part2() {
        var rowNr = 0;
        var scanner = inputLoader.scanner();
        var queue = new LinkedList<List<Position>>();
        var gears = new HashSet<GearRatio>();
        while (scanner.hasNext()) {
            var currentRow = parseLine(rowNr, scanner.nextLine());
            queue.add(currentRow);

            // The missing part wasn't the only issue - one of the gears in the engine is wrong.
            // A gear is any * symbol that is adjacent to exactly two part numbers. Its gear
            // ratio is the result of multiplying those two numbers together.
            // This time, you need to find the gear ratio of every gear and add them all up
            // so that the engineer can figure out which gear needs to be replaced.
            var iterationList = queue.stream().toList();
            for (var i = 0; i < iterationList.size(); i++) {
                var currentIdx = i;
                iterationList.get(i)
                        .stream()
                        .filter(pos -> pos instanceof SymbolPos)
                        .map(SymbolPos.class::cast)
                        .filter(SymbolPos::isGear)
                        .forEach(pos -> {
                            var touching = new ArrayList<>(touching(iterationList.get(currentIdx), pos.position()));
                            if (currentIdx < iterationList.size() - 1) {
                                touching.addAll(touching(iterationList.get(currentIdx + 1), pos.position()));
                            }
                            if (currentIdx > 0) {
                                touching.addAll(touching(iterationList.get(currentIdx - 1), pos.position()));
                            }

                            if (touching.size() == 2) {
                                gears.add(
                                        new GearRatio(pos, touching.get(0), touching.get(1)));
                            }
                        });
            }

            if (queue.size() > 3) {
                queue.poll();
            }

            rowNr++;
        }

        var answer = gears.stream()
                .mapToLong(gear -> (long) gear.left().number() * gear.right().number())
                .sum();

        validator.part2(answer);
    }

    private Set<NumberWithPos> touching(List<Position> inputRow, int position) {
        return inputRow.stream()
                .filter(pos -> pos instanceof NumberWithPos)
                .map(NumberWithPos.class::cast)
                .filter(pos -> pos.endPos() >= position && pos.startPos() <= position)
                .collect(toSet());
    }

    private List<Position> parseLine(int rowNr, String line) {
        var currentRow = new ArrayList<Position>();

        var digit = "";
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (Character.isDigit(c)) {
                digit += c;
            } else {
                if (!digit.isEmpty()) {
                    currentRow.add(new NumberWithPos(
                            Integer.parseInt(digit),
                            rowNr,
                            Math.max(0, i - digit.length() - 1),
                            i));
                    digit = "";
                }
                if (SYMBOLS.contains(c)) {
                    currentRow.add(new SymbolPos(c, i));
                }
            }
        }

        if (!digit.isEmpty()) {
            currentRow.add(new NumberWithPos(
                    Integer.parseInt(digit),
                    rowNr,
                    Math.max(0, line.length() - digit.length() - 1),
                    line.length()));
        }

        return currentRow;
    }
}
