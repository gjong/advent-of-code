package com.github.gjong.advent.years.y2023;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.CharGrid;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.github.gjong.advent.geo.Bounds;
import com.github.gjong.advent.geo.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Day(year = 2023, day = 14)
public class Day14 implements DaySolver {
    private final InputLoader inputLoader = new InputLoader(2023, 14);
    private final Validator validator = new Validator(2023, 14);
    private static final int MAX_RUNS = 1000000000;

    static class World extends HashMap<Point, Character> {

        private final Bounds bounds;

        public World(World other) {
            super(other);
            bounds = other.bounds;
        }

        World(int cols, int rows) {
            bounds = new Bounds(0, 0, cols, rows);
        }

        long weight() {
            return this.entrySet().stream()
                    .filter(point -> point.getValue() == 'O')
                    .map(Map.Entry::getKey)
                    .mapToLong(p -> bounds.height() - p.y())
                    .sum();
        }

        void moveSand(Point direction) {
            var moved = 0;
            do {
                moved = 0;
                for (int y = -direction.y(); y < bounds.height() - direction.y(); ++y) {
                    for (int x = -direction.x(); x < bounds.width() - direction.x(); ++x) {
                        Point p = new Point(x, y);
                        if (getOrDefault(p, '.') == 'O') {
                            var translated = p.translate(direction);
                            if (getOrDefault(translated, '.') == '.') {
                                remove(p);
                                put(translated, 'O');
                                ++moved;
                            }
                        }
                    }
                }
            } while (moved > 0);
        }

        @Override
        public String toString() {
            var grid = new CharGrid((".".repeat(bounds.width()) + "\n").repeat(bounds.height()));
            this.forEach((point, c) -> grid.set(point.x(), point.y(), c));
            return grid.print();
        }
    }

    @Override
    public void part1() {
        var world = parseWorld(inputLoader.string());
        world.moveSand(Point.of(0, -1));  // north
        validator.part1(world.weight());
    }

    @Override
    public void part2() {
        var world = parseWorld(inputLoader.string());

        var answer = 0L;
        var cache = new HashMap<World, Integer>();
        for (var runs = 0; runs < MAX_RUNS; runs++) {
            var directions = List.of(
                    Point.of(0, -1),  // north
                    Point.of(-1, 0),  // west
                    Point.of(0, 1),   // south
                    Point.of(1, 0));  // east

            for (var direction : directions) {
                world.moveSand(direction);
            }

            // manipulate the cache
            if (cache.containsKey(world)) {
                var lastSeen = cache.get(world);
                var endOfCycle = lastSeen + (MAX_RUNS - runs - 1)
                        % (runs - lastSeen);

                answer = cache.entrySet().stream()
                        .filter(e -> e.getValue() == endOfCycle)
                        .mapToLong(e -> e.getKey().weight())
                        .findFirst()
                        .orElseThrow();
                break;
            }

            cache.put(new World(world), runs);
        }

        validator.part2(answer);
    }

    private World parseWorld(String input) {
        var grid = new CharGrid(input);
        var world = new World(grid.cols(), grid.rows());
        for (var x = 0; x < grid.cols(); x++) {
            for (var y = 0; y < grid.rows(); y++) {
                var c = grid.at(x, y);
                if (c == '#' || c == 'O') {
                    world.put(new Point(x, y), c);
                }
            }
        }
        return world;
    }
}
