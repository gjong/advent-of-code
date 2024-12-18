package com.github.gjong.advent.years.y2024;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.github.gjong.advent.geo.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

@Day(year = 2024, day = 16, name = "Reindeer Maze")
public class Day16 implements DaySolver {

    private final Logger log = LoggerFactory.getLogger(Day16.class);

    private final InputLoader inputLoader;
    private final Validator validator;

    public Day16(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    record PathCost(Point previous, int cost) {}
    record Path(Point previous, Point pos, Point direction, int cost) {
        boolean sameDirection(Point other) {
            return pos.translate(direction).equals(other);
        }
    }

    @Override
    public void part1() {
        var grid = inputLoader.charGrid();
        var startPos = grid.findChar('S').getFirst();
        var endPos = grid.findChar('E').getFirst();

        // find paths
        var visited = new HashMap<Point, PathCost>(); // point to cost mapping
        var queue = new PriorityQueue<>(Comparator.comparingInt(Path::cost));
        queue.add(new Path(null, startPos, Point.of(1, 0), 0));
        while (!queue.isEmpty()) {
            var currentPos = queue.poll();
            visited.putIfAbsent(currentPos.pos(), new PathCost(currentPos.previous, currentPos.cost()));

            if (currentPos.pos().equals(endPos)) {
                // found cheapest way out
                break;
            }

            for (var neighbour : currentPos.pos().neighbours()) {
                if (grid.at(neighbour) == '#' || visited.containsKey(neighbour)) {
                    // skip walls, or already visited
                    continue;
                }

                queue.add(new Path(
                        currentPos.pos,
                        neighbour,
                        neighbour.subtract(currentPos.pos()),
                        currentPos.cost() + (currentPos.sameDirection(neighbour) ? 1 : 1001)));
            }
        }

        if (log.isDebugEnabled()) {
            var updating = visited.get(endPos).previous();
            while (updating != null) {
                grid.set(updating.x(), updating.y(), '0');
                updating = visited.get(updating).previous();
            }

            log.debug("Grid after travel is:\n{}", grid.print());
        }

        validator.part1(visited.get(endPos).cost());
    }

    @Override
    public void part2() {

    }
}
