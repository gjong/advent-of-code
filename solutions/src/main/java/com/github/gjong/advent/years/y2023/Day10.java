package com.github.gjong.advent.years.y2023;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.github.gjong.advent.geo.Point;

import java.util.*;
import java.util.stream.Collectors;

@Day(day = 10, year = 2023, name = "Pipe Maze")
public class Day10 implements DaySolver {
    private final InputLoader inputLoader = new InputLoader(2023, 10);
    private final Validator validator = new Validator(2023, 10);

    record Grid(Point start, Map<Point, List<Point>> graph) {
    }

    record Path(Point start, int length) {
    }

    @Override
    public void part1() {
        var grid = parseGrid(inputLoader.string());

        var queue = new PriorityQueue<>(Comparator.comparingInt(Path::length));

        var farthest = 0L;
        queue.add(new Path(grid.start(), 0));
        var visited = new HashSet<Point>();
        while (!queue.isEmpty()) {
            var current = queue.poll();
            if (visited.contains(current.start())) {
                continue;
            }

            if (farthest < current.length()) {
                farthest = current.length();
            }

            visited.add(current.start());
            for (var neighbour : grid.graph().get(current.start())) {
                if (!visited.contains(neighbour)) {
                    queue.add(new Path(neighbour, current.length() + 1));
                }
            }
        }
        validator.part1(farthest);
    }

    @Override
    public void part2() {
        // not solved
    }

    private Grid parseGrid(String input) {
        Point start = null;
        var graph = new HashMap<Point, List<Point>>();
        var lines = input.lines().toList();
        for (int y = 0; y < lines.size(); y++) {
            var lineLength = lines.get(y).length();
            for (var x = 0; x < lineLength; x++) {
                var point = new Point(x, y);
                List<Point> connectedWith = switch (lines.get(y).charAt(x)) {
                    case '|' -> List.of(point.up(), point.down());
                    case '-' -> List.of(point.left(), point.right());
                    case 'L' -> List.of(point.up(), point.right());
                    case 'J' -> List.of(point.up(), point.left());
                    case '7' -> List.of(point.down(), point.left());
                    case 'F' -> List.of(point.down(), point.right());
                    case 'S' -> {
                        start = Point.of(x, y);
                        yield new ArrayList<>(start.neighbours());
                    }
                    case '.' -> List.of();
                    default -> throw new IllegalStateException("Unexpected value: " + lines.get(y).charAt(x));
                };
                connectedWith = connectedWith.stream()
                        .filter(p -> p.x() >= 0
                                && p.y() >= 0
                                && p.x() < lines.size()
                                && p.y() < lineLength)
                        .toList();

                if (!connectedWith.isEmpty()) {
                    graph.put(point, connectedWith);
                }
            }
        }

        if (start == null) {
            throw new IllegalStateException("No start found.");
        }

        // Remove all nodes that are not connected to any other node.
        var y = graph.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream()
                        .filter(graph::containsKey)
                        .filter(p -> graph.get(p).contains(e.getKey()))
                        .toList()));

        return new Grid(start, y);
    }
}
