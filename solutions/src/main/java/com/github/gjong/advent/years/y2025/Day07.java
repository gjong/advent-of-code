package com.github.gjong.advent.years.y2025;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.github.gjong.advent.geo.Point;
import com.github.gjong.advent.grid.CharGrid;

import java.math.BigInteger;
import java.util.*;

@Day(year = 2025, day = 7, name = "Laboratories")
public class Day07 implements DaySolver {

    private InputLoader inputLoader;
    private Validator validator;

    private CharGrid grid;

    public Day07(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void part1() {
        long numberOfSplits = 0;
        Point startPosition = Point.of(0, 0);
        for (int colIdx = 0; colIdx < grid.cols(); colIdx++) {
            if (grid.at(colIdx, 0) == 'S') {
                startPosition = Point.of(colIdx, 0);
                break;
            }
        }

        Set<Point> beamPoints = new HashSet<>();
        beamPoints.add(startPosition.down());
        for (int rowIdx = 2; rowIdx < grid.rows(); rowIdx++) {
            int finalRowIdx = rowIdx;
            List<Point> lookAtPoints = beamPoints.stream()
                    .filter(point -> point.y() + 1 == finalRowIdx)
                    .map(Point::down)
                    .toList();

            for (Point pointOfInterest : lookAtPoints) {
                if (grid.at(pointOfInterest) == '^') {
                    beamPoints.add(pointOfInterest.right());
                    beamPoints.add(pointOfInterest.left());
                    numberOfSplits++;
                } else if (grid.at(pointOfInterest) == '.') {
                    beamPoints.add(pointOfInterest);
                }
            }
        }

        validator.part1(numberOfSplits);
    }

    @Override
    public void part2() {
        int startCol = -1;
        for (int colIdx = 0; colIdx < grid.cols(); colIdx++) {
            if (grid.at(colIdx, 0) == 'S') {
                startCol = colIdx;
                break;
            }
        }

        Map<Integer, BigInteger> waysByCol = new HashMap<>();
        if (startCol >= 0 && grid.rows() > 1) {
            waysByCol.put(startCol, BigInteger.ONE);
        }

        for (int y = 1; y < grid.rows() - 1; y++) {
            Map<Integer, BigInteger> next = new HashMap<>();

            for (Map.Entry<Integer, BigInteger> e : waysByCol.entrySet()) {
                int x = e.getKey();
                BigInteger ways = e.getValue();

                Point pos = Point.of(x, y + 1);
                if (!grid.bounds().inBounds(pos)) continue;

                char cellBelow = grid.at(pos);
                if (cellBelow == '.') {
                    next.merge(x, ways, BigInteger::add);
                } else if (cellBelow == '^') {
                    if (grid.bounds().inBounds(pos.left())) next.merge(x - 1, ways, BigInteger::add);
                    if (grid.bounds().inBounds(pos.right())) next.merge(x + 1, ways, BigInteger::add);
                }
            }

            waysByCol = next;
        }

        BigInteger totalTimelines = waysByCol.values()
                .stream()
                .reduce(BigInteger.ZERO, BigInteger::add);

        validator.part2(totalTimelines.toString());
    }

    @Override
    public void readInput() {
        grid = inputLoader.charGrid();
    }
}
