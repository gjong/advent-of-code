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
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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

    private int playRound(List<Point> blockades) {
        var currentPos = startPoint;
        var currentDirection = new Point(0, -1);

        var visited = new HashSet<PointWithDirection>();
        while (gridBounds.inBounds(currentPos)) {
            if (visited.contains(new PointWithDirection(currentPos, currentDirection))) {
                return -1;
            }
            visited.add(new PointWithDirection(currentPos, currentDirection));

            var updatedPos = currentPos.translate(currentDirection);
            if (blockades.contains(updatedPos)) {
                currentDirection = currentDirection.rotateCW();
            } else {
                currentPos = updatedPos;
            }
        }

        return visited.size();
    }

    @Override
    public void part2() {
        var counter = new AtomicInteger();
        try (var executors = Executors.newVirtualThreadPerTaskExecutor()) {
            for (var x = 0; x < gridBounds.width(); x++) {
                for (var y = 0; y < gridBounds.height(); y++) {
                    var point = new Point(x, y);
                    if (!blockades.contains(point)) {
                        var updatedBlockades = new ArrayList<>(blockades);
                        updatedBlockades.add(point);

                        executors.submit(() -> {
                            if (playRound(updatedBlockades) == -1) {
                                counter.incrementAndGet();
                            }
                        });
                    }
                }
            }

            executors.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        validator.part2(counter.get());
    }

    private record PointWithDirection(Point point, Point direction) {}
}
