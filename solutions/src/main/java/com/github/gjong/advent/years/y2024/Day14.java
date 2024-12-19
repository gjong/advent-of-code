package com.github.gjong.advent.years.y2024;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.github.gjong.advent.geo.Bounds;
import com.github.gjong.advent.geo.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Day(year = 2024, day = 14, name = "Restroom Redoubt")
public class Day14 implements DaySolver {

    private final Logger log = LoggerFactory.getLogger(Day14.class);

    private final InputLoader inputLoader;
    private final Validator validator;
    private Bounds bounds;

    public Day14(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    private class Robot {
        private final Point location;
        private final Point velocity;

        public Robot(Point location, Point velocity) {
            this.location = location;
            this.velocity = velocity;
        }

        public Point move(int totalSteps) {
            var x = (location.x() + velocity.x() * totalSteps) % bounds.width();
            var y = (location.y() + velocity.y() * totalSteps) % bounds.height();

            if (x < 0) {
                x += bounds.width();
            }
            if (y < 0) {
                y += bounds.height();
            }
            return Point.of(x, y);
        }
    }

    @Override
    public void part1() {
        bounds = new Bounds(0, 0, 101, 103);
        if (log.isDebugEnabled()) {
            bounds = new Bounds(0, 0, 11, 7);
        }

        var robots = parseRobots()
                .map(robot -> robot.move(100))
                .toList();

        validator.part1(countRobotsInQuadrants(robots));
    }

    @Override
    public void part2() {
        bounds = new Bounds(0, 0, 101, 103);
        if (log.isDebugEnabled()) {
            bounds = new Bounds(0, 0, 11, 7);
        }

        var i = 0;
        List<Point> newPositions;
        var uniquePositions = new HashSet<Point>();
        var robots = parseRobots().toList();
        do {
            final var finalI = ++i;
            newPositions = robots.stream()
                    .map(robot -> robot.move(finalI))
                    .toList();

            uniquePositions = new HashSet<>(newPositions);
        } while (uniquePositions.size() != newPositions.size());

        validator.part2(i);
    }

    private Stream<Robot> parseRobots() {
        var pattern = Pattern.compile("p=(?<X>-?\\d+),(?<Y>-?\\d+) v=(?<vX>-?\\d+),(?<vY>-?\\d+)");
        return inputLoader.splitOnNewLine()
                .map(pattern::matcher)
                .filter(Matcher::matches)
                .map(matcher -> new Robot(
                        Point.of(Integer.parseInt(matcher.group("X")), Integer.parseInt(matcher.group("Y"))),
                        Point.of(Integer.parseInt(matcher.group("vX")), Integer.parseInt(matcher.group("vY")))));
    }

    private long countRobotsInQuadrants(List<Point> robots) {
        var middleWidth = bounds.width() / 2;
        var middleHeight = bounds.height() / 2;

        var bottomTop = middleHeight - 1;
        var topBottom = middleHeight + 1;
        var leftRight = middleWidth - 1;
        var rightLeft = middleWidth + 1;

        var leftBottom = new Bounds(0, 0, leftRight, bottomTop);
        var rightBottom = new Bounds(rightLeft, 0, bounds.width(), bottomTop);

        var leftTop = new Bounds(0, topBottom, leftRight, bounds.height());
        var rightTop = new Bounds(rightLeft, topBottom, bounds.width(), bounds.height());

        return robots.stream()
                .collect(
                        Collectors.teeing(
                                Collectors.teeing(
                                        Collectors.filtering(leftTop::inBounds, Collectors.counting()),
                                        Collectors.filtering(rightTop::inBounds, Collectors.counting()),
                                        (left, right) -> left * right),
                                Collectors.teeing(
                                        Collectors.filtering(leftBottom::inBounds, Collectors.counting()),
                                        Collectors.filtering(rightBottom::inBounds, Collectors.counting()),
                                        (left, right) -> left * right),
                                (left, right) -> left * right
                        )
                );
    }
}
