package com.github.gjong.advent.years.y2024;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.LimitRuns;
import com.github.gjong.advent.algo.Node;
import com.github.gjong.advent.algo.PathFinding;
import com.github.gjong.advent.common.CharGrid;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.github.gjong.advent.geo.Point;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Day(year = 2024, day = 18, name = "RAM Run")
public class Day18 implements DaySolver {

    private final Logger log = org.slf4j.LoggerFactory.getLogger(Day18.class);

    private CharGrid grid;
    private final InputLoader inputLoader;
    private final Validator validator;

    public Day18(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void readInput() {
        grid = new CharGrid(71, 71);
        if (log.isDebugEnabled()) {
            // hack since testing grid is only 6x6
            grid = new CharGrid(7, 7);
        }
        inputLoader.splitOnNewLine()
                .map(Point::of)
                .limit(1024)                        // limit of bytes to fall part1
                .forEach(point -> grid.set(point, '#'));
    }

    @Override
    public void part1() {
        var stepCache = new HashMap<Point, HistorianStep>();
        var start = new HistorianStep(Point.zero, stepCache);
        var end = Point.of(grid.cols() - 1, grid.rows() - 1);

        start.setCost(0L);
        var cheapestRoute = PathFinding.dijkstra(List.of(start), step -> step.location.equals(end));
        if (cheapestRoute.isEmpty()) {
            throw new IllegalStateException("No path found.");
        }

        validator.part1(cheapestRoute.get().cost());
    }

    @LimitRuns
    @Override
    public void part2() {
        var start = new HistorianStep(Point.zero, new HashMap<>());
        var end = Point.of(grid.cols() - 1, grid.rows() - 1);

        var allBlocks = new ArrayList<>(inputLoader.splitOnNewLine()
                .map(Point::of)
                .skip(1024)
                .toList());

        Point lastBlockAdded = null;
        start.setCost(0L);
        while (PathFinding.dijkstra(List.of(start), step -> step.location.equals(end)).isPresent()) {
            lastBlockAdded = allBlocks.removeFirst();
            grid.set(lastBlockAdded, '#');

            start = new HistorianStep(Point.zero, new HashMap<>());
            start.setCost(0L);
        }

        validator.part2(lastBlockAdded.x() + "," + lastBlockAdded.y());
    }

    private class HistorianStep extends Node<HistorianStep> {
        private final Point location;
        private final Map<Point, HistorianStep> cache;

        public HistorianStep(Point location, Map<Point, HistorianStep> cache) {
            this.location = location;
            this.cache = cache;
        }

        @Override
        public List<NeighbourWithCost<HistorianStep>> neighbours() {
            return location.neighbours()
                    .stream()
                    .filter(point -> grid.at(point) == '.')
                    .map(point -> cache.computeIfAbsent(point, key -> new HistorianStep(key, cache)))
                    .map(step -> new NeighbourWithCost<>(step, 1L))
                    .toList();
        }
    }

}
