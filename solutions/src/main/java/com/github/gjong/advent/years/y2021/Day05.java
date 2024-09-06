package com.github.gjong.advent.years.y2021;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.github.gjong.advent.geo.Point;
import com.github.gjong.advent.geo.Vector;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

@Day(day = 5, year = 2021, name = "Hydrothermal Venture")
public class Day05 implements DaySolver {
    private final InputLoader inputLoader;
    private final Validator validator;

    public Day05(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void part1() {
        var vectors = inputLoader.splitOnNewLine()
                .map(Day05::toVector)
                .filter(Predicate.not(Vector::isDiagonal))
                .toList();

        var duplicateFields = vectors.stream()
                .map(Vector::pointsInVector)
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .values()
                .stream()
                .filter(count -> count > 1)
                .count();

        validator.part1(duplicateFields);
    }

    @Override
    public void part2() {
        var vectors = inputLoader.splitOnNewLine()
                .map(Day05::toVector)
                .filter(vector -> vector.isDiagonal() || vector.isHorizontal() || vector.isVertical())
                .toList();

        var grid = new Grid(1000, 1000);

        var totalCount = 0;
        var drawnGrid = grid.computeGrid(vectors);
        for (int[] ints : drawnGrid) {
            for (int anInt : ints) {
                if (anInt > 1) {
                    totalCount++;
                }
            }
        }

        validator.part2(totalCount);
    }

    private static final Pattern vectorPattern = Pattern.compile("([0-9]+),([0-9]+) -> ([0-9]+),([0-9]+)");
    static Vector toVector(String line) {
        var match = vectorPattern.matcher(line);
        if (match.matches()) {
            var start = new Point(parseInt(match.group(1)), parseInt(match.group(2)));
            var end = new Point(parseInt(match.group(3)), parseInt(match.group(4)));

            return new Vector(start, end);
        }

        throw new IllegalArgumentException("Line does not match regex: " + line);
    }

    record Grid(int width, int height) {
        int[][] computeGrid(List<Vector> vectors) {
            int[][] grid = new int[width][height];

            vectors.forEach(v -> v.pointsInVector()
                    .forEach(p -> grid[p.x()][p.y()] += 1));

            return grid;
        }
    }
}
