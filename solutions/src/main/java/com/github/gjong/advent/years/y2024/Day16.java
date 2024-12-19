package com.github.gjong.advent.years.y2024;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.algo.Node;
import com.github.gjong.advent.algo.PathFinding;
import com.github.gjong.advent.common.CharGrid;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.github.gjong.advent.geo.Direction;
import com.github.gjong.advent.geo.Point;
import com.github.gjong.advent.geo.Vector;

import java.util.HashMap;
import java.util.List;

@Day(year = 2024, day = 16, name = "Reindeer Maze")
public class Day16 implements DaySolver {

    private final InputLoader inputLoader;
    private final Validator validator;

    public Day16(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    private CharGrid grid;

    @Override
    public void readInput() {
        grid = inputLoader.charGrid();
    }

    @Override
    public void part1() {
        var startPos = new Step(grid.findChar('S').getFirst(), Point.east, new HashMap<>());
        var endPos = grid.findChar('E').getFirst();

        startPos.setCost(0L);
        var cost = PathFinding.dijkstra(
                List.of(startPos),
                step -> step.location.equals(endPos));

        if (cost.isEmpty()) {
            throw new IllegalStateException("No path found");
        }

        validator.part1(cost.get().cost());
    }

    @Override
    public void part2() {

    }

    private record StepDirection(Point location, Direction direction) {}

    private class Step extends Node<Step> {
        private final Point location;
        private final Direction direction;
        private final HashMap<StepDirection, Step> cache;

        private Step(Point location, Direction direction, HashMap<StepDirection, Step> cache) {
            this.location = location;
            this.direction = direction;
            this.cache = cache;
        }

        @Override
        public List<NeighbourWithCost<Step>> neighbours() {
            return location.neighbours()
                    .stream()
                    .filter(point -> grid.at(point) != '#')
                    .map(step -> {
                        var direction = Vector.direction(location, step);
                        var cost = direction.equals(this.direction) ? 1L : 1001L;
                        if (!cache.containsKey(new StepDirection(step, direction))) {
                            var nextStep = new Step(step, direction, cache);
                            cache.put(new StepDirection(step, direction), nextStep);
                            return new NeighbourWithCost<>(nextStep, cost);
                        }

                        return new NeighbourWithCost<>(cache.get(new StepDirection(step, direction)), cost);
                    })
                    .toList();
        }
    }
}
