package com.github.gjong.advent.years.y2024;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.github.gjong.advent.geo.Bounds;
import com.github.gjong.advent.geo.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Day(year = 2024, day = 6, name = "Guard Gallivant")
public class Day06 implements DaySolver {

    private final InputLoader inputLoader;
    private final Validator validator;

    private Bounds gridBounds;
    private final List<Point> blockades = new ArrayList<>();
    private Point startPoint;

    public Day06(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void readInput() {
        var row = 0;
        var rows = 0;
        for (var line : inputLoader.string().split("\n")) {
            if (rows == 0) {
                rows = line.length();
                gridBounds = new Bounds(0, 0, rows - 1, line.length() - 1);
            }

            for (var i = 0; i < line.length(); i++) {
                switch (line.charAt(i)) {
                    case '#':
                        blockades.add(new Point(i, row));
                        break;
                    case '^':
                        startPoint = new Point(i, row);
                        break;
                    default:
                        break;
                }
            }
            row++;
        }
    }

    @Override
    public void part1() {
        var currentPos = startPoint;
        var currentDirection = new Point(0, -1);

        var visited = new HashSet<Point>();
        while (gridBounds.inBounds(currentPos)) {
            visited.add(currentPos);

            var updatedPos = currentPos.translate(currentDirection);
            if (blockades.contains(updatedPos)) {
                currentDirection = currentDirection.rotateCW();
            } else {
                currentPos = updatedPos;
            }
        }

        validator.part1(visited.size());
    }

    @Override
    public void part2() {
        var currentPos = startPoint;
        var currentDirection = new Point(0, -1);

        var visited = new HashSet<PointWithDirection>();
        while (gridBounds.inBounds(currentPos)) {
            visited.add(new PointWithDirection(currentPos, currentDirection));

            var updatedPos = currentPos.translate(currentDirection);
            if (blockades.contains(updatedPos)) {
                currentDirection = currentDirection.rotateCW();
            } else {
                currentPos = updatedPos;
            }
        }

        validator.part2(visited.size());
    }

    private record PointWithDirection(Point point, Point direction) {}
}
