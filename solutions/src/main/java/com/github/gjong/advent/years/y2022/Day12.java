package com.github.gjong.advent.years.y2022;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.github.gjong.advent.geo.Point;

import java.util.*;
import java.util.function.BiPredicate;

@Day(day = 12, year = 2022, name = "Hill Climbing Algorithm")
public class Day12 implements DaySolver {
    private final InputLoader inputLoader = new InputLoader(2022, 12);
    private final Validator validator = new Validator(2022, 12);

    @Override
    public void part1() {
        Point start = null, end = null;
        var input = inputLoader.splitOnNewLine().toList();
        var dijkstra = new Dijkstra<>(this::visitAllowed);
        for (var row = 0; row < input.size(); row++) {
            if (input.get(row).isBlank()) {
                continue;
            }
            var line = input.get(row);
            for (var column = 0; column < line.length(); column++) {
                var character = line.charAt(column);
                var location = new Point(column, row);
                if (character == 'S') {
                    start = location;
                    character = 'a';
                }
                if (character == 'E') {
                    end = location;
                    character = 'z';
                }

                dijkstra.addLocation(location, character);
            }
        }

        validator.part1(dijkstra.resolve(start, end));
    }

    @Override
    public void part2() {
        var input = inputLoader.splitOnNewLine().toList();

        List<Point> potentialStart = new ArrayList<>();
        Point end = null;

        var dijkstra = new Dijkstra<>(this::visitAllowed);
        for (var row = 0; row < input.size(); row++) {
            if (input.get(row).isBlank()) {
                continue;
            }

            var line = input.get(row);
            for (var column = 0; column < line.length(); column++) {
                var character = line.charAt(column);
                var location = new Point(column, row);
                if (character == 'S' || character == 'a') {
                    potentialStart.add(location);
                    character = 'a';
                }
                if (character == 'E') {
                    end = location;
                    character = 'z';
                }

                dijkstra.addLocation(location, character);
            }
        }

        var finalEnd = end;
        var result = potentialStart.stream()
                .mapToInt(start -> dijkstra.resolve(start, finalEnd))
                .filter(v -> v > 0)
                .min()
                .orElse(-1);
        validator.part2(result);
    }

    private boolean visitAllowed(Character from, Character to) {
        return to - from <= 1;
    }

    class Dijkstra<T> {
        private final Map<Point, T> grid;
        private final BiPredicate<T, T> visitAllowed;

        public Dijkstra(BiPredicate<T, T> visitAllowed) {
            this.grid = new HashMap<>();
            this.visitAllowed = visitAllowed;
        }

        public void addLocation(Point location, T value) {
            grid.put(location, value);
        }

        public int resolve(Point start, Point end) {
            Objects.requireNonNull(start, "Start cannot be null.");
            Objects.requireNonNull(end, "End cannot be null.");
            assert grid.containsKey(start) && grid.containsKey(end);

            var visited = new HashMap<Point, Integer>();
            visited.put(start, 0);

            var processing = new PriorityQueue<Point>(Comparator.comparingInt(visited::get));
            processing.add(start);
            while (!processing.isEmpty()) {
                var current = processing.poll();
                if (current.equals(end)) {
                    return visited.get(end);
                }

                for (var neighbour : current.neighbours()) {
                    var currentVal = grid.get(neighbour);
                    var visitedVal = visited.get(neighbour);
                    if (currentVal != null && visitedVal == null) {
                        if (visitAllowed.test(grid.get(current), currentVal)) {
                            visited.put(neighbour, visited.get(current) + 1);
                            processing.add(neighbour);
                        }
                    }
                }
            }

            return -1;
        }
    }
}
