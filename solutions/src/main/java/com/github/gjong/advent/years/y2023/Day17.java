package com.github.gjong.advent.years.y2023;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.CharGrid;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.github.gjong.advent.geo.Point;

import java.util.*;

@Day(day = 17, year = 2023)
public class Day17 implements DaySolver {
    private final InputLoader inputLoader = new InputLoader(2023, 17);
    private final Validator validator = new Validator(2023, 17);

    record PathCost(Point location, char direction, int cost) implements Comparable<PathCost> {
        @Override
        public int compareTo(PathCost o) {
            return cost - o.cost;
        }

        public int hashCode() {
            return Objects.hash(location, direction);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof PathCost other) {
                return location.equals(other.location) && direction == other.direction;
            }
            return false;
        }

        public List<PathCost> nextSteps(CharGrid grid, int minSteps, int maxSteps) {
            var options = new ArrayList<PathCost>();

            var currentTranslation = Point.directionFromChar(direction);
            for (char updated : "NSWE".toCharArray()) {
                var translation = Point.directionFromChar(updated);
                if (translation.equals(currentTranslation) ||
                        translation.inverse().equals(currentTranslation)) {
                    // don't go back the way we came
                    continue;
                }

                var updatedCost = cost;
                var updatedLocation = location;
                for (int i = 1; i <= maxSteps; ++i) {
                    updatedLocation = updatedLocation.translate(translation);
                    if (grid.bounds().inBounds(updatedLocation)) {
                        updatedCost += grid.at(updatedLocation) - '0';

                        if (i >= minSteps) {
                            options.add(new PathCost(updatedLocation, updated, updatedCost));
                        }
                    } else {
                        // out of bounds
                        break;
                    }
                }
            }

            return options;
        }
    }

    @Override
    public void part1() {
        validator.part1(solve(1, 3));
    }

    @Override
    public void part2() {
        validator.part2(solve(4, 10));
    }

    private int solve(int minSteps, int maxSteps) {
        var grid = inputLoader.charGrid();
        var end = new Point(grid.cols() - 1, grid.rows() - 1);

        var visited = new HashMap<PathCost, Integer>();
        visited.put(new PathCost(new Point(0, 0), '.', 0), 0);

        var queue = new PriorityQueue<>(visited.keySet());
        while (!queue.isEmpty()) {
            var current = queue.poll();
            if (current.location().equals(end)) {
                return current.cost();
            }

            for (var next : current.nextSteps(grid, minSteps,  maxSteps)) {
                if (next.cost() < visited.getOrDefault(next, Integer.MAX_VALUE)) {
                    visited.put(next, next.cost());
                    queue.add(next);
                }
            }
        }

        throw new IllegalStateException("No path found");
    }
}
