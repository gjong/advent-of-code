package com.github.gjong.advent.years.y2024;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.github.gjong.advent.geo.Bounds;
import com.github.gjong.advent.geo.Point;

import java.util.*;
import java.util.stream.Stream;

@Day(year = 2024, day = 8, name = "Resonant Collinearity")
public class Day08 implements DaySolver {

    private final InputLoader inputLoader;
    private final Validator validator;

    private Bounds bounds;
    private final Map<String, List<Point>> parsedGrid = new HashMap<>();

    public Day08(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void readInput() {
        var grid = inputLoader.charGrid();
        for (int y = 0; y < grid.rows(); y++) {
            for (int x = 0; x < grid.cols(); x++) {
                var c = grid.at(x, y);
                if ('.' != c) {
                    parsedGrid.computeIfAbsent("" + c, k -> new ArrayList<>())
                            .add(Point.of(x, y));
                }
            }
        }

        bounds = grid.bounds();
    }

    @Override
    public void part1() {
        var answer = parsedGrid.entrySet()
                .stream()
                .flatMap(e -> getAntiNodes(e.getValue(), false))
                .distinct()
                .count();

        validator.part1(answer);
    }

    @Override
    public void part2() {
        var answer = parsedGrid.entrySet()
                .stream()
                .flatMap(e -> getAntiNodes(e.getValue(), true))
                .distinct()
                .count();

        validator.part2(answer);
    }

    public Stream<Point> getAntiNodes(List<Point> nodes, boolean isLine) {
        var antiNodes = new HashSet<Point>();
        if (isLine) {
            antiNodes.addAll(nodes);
        }

        for (int i = 0; i < nodes.size() - 1; i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                var node1 = nodes.get(i);
                var node2 = nodes.get(j);

                var diff = node1.subtract(node2);
                var antiNode1 = node1.translate(diff);
                if (!isLine && bounds.inBounds(antiNode1)) {
                    antiNodes.add(antiNode1);
                } else while (bounds.inBounds(antiNode1)) {
                    antiNodes.add(antiNode1);
                    antiNode1 = antiNode1.translate(diff);
                }

                var invertedDiff = diff.inverse();
                var antiNode2 = node2.translate(invertedDiff);
                if (!isLine && bounds.inBounds(antiNode2)) {
                    antiNodes.add(antiNode2);
                } else while (bounds.inBounds(antiNode2)) {
                    antiNodes.add(antiNode2);
                    antiNode2 = antiNode2.translate(invertedDiff);
                }
            }
        }

        return antiNodes.stream();
    }
}
