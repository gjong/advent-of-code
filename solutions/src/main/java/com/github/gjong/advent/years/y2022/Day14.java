package com.github.gjong.advent.years.y2022;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.github.gjong.advent.geo.Point;
import com.github.gjong.advent.geo.Vector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Integer.parseInt;

@Day(day = 14, year = 2022, name = "Regolith Reservoir")
public class Day14 implements DaySolver {
    private final InputLoader inputLoader = new InputLoader(2022, 14);
    private final Validator validator = new Validator(2022, 14);

    private static final Point DROP_POINT = Point.of(500, 0);

    int lowestY;

    @Override
    public void part1() {
        lowestY = 0;
        var blockedPoints = parse();

        var sandMover = new SandMover();
        var droppedSand = 0;
        var blockedDrop = true;
        while (blockedDrop) {
            // drop sand
            var sandPos = sandMover.drop(lowestY, blockedPoints);
            if (sandPos != null) {
                blockedPoints.add(sandPos);
                droppedSand++;
            }

            blockedDrop = sandPos != null;
        }

        validator.part1(droppedSand);
    }

    @Override
    public void part2() {
        lowestY = 0;
        var blockedPoints = parse();
        int bottom = lowestY + 2;

        // bottom needs to be no wider then the triangle from drop point to bottom + 1
        var width = (int) Math.ceil(bottom * Math.tan(45)) + 1;
        for (var x = 500 - width; x < 500 + width; x++) {
            blockedPoints.add(Point.of(x, bottom));
        }

        var sandMover = new SandMover();
        var droppedSand = 0;
        while (true) {
            var sandPos = sandMover.drop(bottom, blockedPoints);

            droppedSand++;
            blockedPoints.add(sandPos);
            if (sandPos.equals(DROP_POINT)) {
                break;
            }
        }

        validator.part2(droppedSand);
    }

    private Set<Point> parse() {
        var blockedPoints = new HashSet<Point>();
        var scanner = inputLoader.scanner();
        while (scanner.hasNextLine()) {
            var points = scanner.nextLine().split(" -> ");

            Point lastPoint = null;
            for (var point : points) {
                var coords = point.split(",");
                var currentPoint = Point.of(parseInt(coords[0]), parseInt(coords[1]));

                if (lowestY < currentPoint.y()) {
                    lowestY = currentPoint.y();
                }

                if (lastPoint != null) {
                    blockedPoints.addAll(new Vector(lastPoint, currentPoint).pointsInVector());
                }
                lastPoint = currentPoint;
            }
        }
        return blockedPoints;
    }

    class SandMover {
        private static final Point DROP_POINT = Point.of(500, 0);
        private static final List<Point> TRANSLATIONS = List.of(Point.of(0, 1), Point.of(-1, 1), Point.of(1, 1));

        public Point drop(int lowestY, Set<Point> blockedPoints) {
            var sandPos = DROP_POINT;

            while (sandPos.y() <= lowestY) {
                var changed = false;
                for (var translation : TRANSLATIONS) {
                    var translated = sandPos.translate(translation);
                    if (!blockedPoints.contains(translated)) {
                        sandPos = translated;
                        changed = true;
                        break;
                    }
                }

                if (!changed) {
                    return sandPos;
                }
            }

            return null;
        }
    }
}
