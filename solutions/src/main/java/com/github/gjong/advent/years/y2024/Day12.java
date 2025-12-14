package com.github.gjong.advent.years.y2024;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.grid.CharGrid;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.github.gjong.advent.geo.Point;
import org.slf4j.Logger;

import java.util.*;

@Day(day = 12, year = 2024, name = "Garden Groups")
public class Day12 implements DaySolver {

    private final Logger log = org.slf4j.LoggerFactory.getLogger(Day12.class);

    private final InputLoader inputLoader;
    private final Validator validator;

    public Day12(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    record Price(int area, int perimeter) {
        public int compute() {
            return area * perimeter;
        }
    }

    @Override
    public void part1() {
        validator.part1(computeTotalPrice(false));
    }

    @Override
    public void part2() {
        validator.part2(computeTotalPrice(true));
    }

    private int computeTotalPrice(boolean trackSimpleBorders) {
        var grid = inputLoader.charGrid();
        var processed = new HashSet<Point>();

        var prices = new ArrayList<Price>();
        for (var x = 0; x < grid.cols(); x++) {
            for (var y = 0; y < grid.rows(); y++) {
                if (processed.contains(new Point(x, y))) {
                    continue;
                }

                prices.add(computePrice(grid, new Point(x, y), processed, trackSimpleBorders));
            }
        }

        return prices.stream().mapToInt(Price::compute).sum();
    }

    private Price computePrice(CharGrid grid, Point start, Set<Point> processed, boolean trackSimpleBorders) {
        var processQueue = new LinkedHashSet<Point>();
        var shapePoints = new HashSet<Point>();
        processQueue.add(start);

        var borders = 0;
        while (!processQueue.isEmpty()) {
            var current = processQueue.removeFirst();
            processed.add(current);
            shapePoints.add(current);

            for (var neighbour : current.neighbours()) {
                if (processed.contains(neighbour)) {
                    if (grid.at(neighbour) != grid.at(current)) {
                        borders++;
                    }
                    continue;
                }

                if (grid.at(neighbour) == grid.at(current)) {
                    processQueue.add(neighbour);
                } else {
                    borders++;
                }
            }
        }

        if (trackSimpleBorders) {
            borders = computeCorners(shapePoints);
        }

        log.debug("Processed at {} for character {}, with {} borders.", start, grid.at(start), borders);
        return new Price(shapePoints.size(), borders);
    }

    private int computeCorners(Set<Point> shapePoints) {
        var corners = 0;
        for (var point : shapePoints) {
            // inward corners
            if (shapePoints.contains(point.up()) && shapePoints.contains(point.up().right()) && !shapePoints.contains(point.right())) {
                corners++;
            }
            if (shapePoints.contains(point.up()) && shapePoints.contains(point.up().left()) && !shapePoints.contains(point.left())) {
                corners++;
            }
            if (shapePoints.contains(point.down()) && shapePoints.contains(point.down().left()) && !shapePoints.contains(point.left())) {
                corners++;
            }
            if (shapePoints.contains(point.down()) && shapePoints.contains(point.down().right()) && !shapePoints.contains(point.right())) {
                corners++;
            }

            // outer corners
            if (!shapePoints.contains(point.up()) && !shapePoints.contains(point.left())) {
                corners++;
            }
            if (!shapePoints.contains(point.up()) && !shapePoints.contains(point.right())) {
                corners++;
            }
            if (!shapePoints.contains(point.down()) && !shapePoints.contains(point.left())) {
                corners++;
            }
            if (!shapePoints.contains(point.down()) && !shapePoints.contains(point.right())) {
                corners++;
            }
        }
        return corners;
    }

}
