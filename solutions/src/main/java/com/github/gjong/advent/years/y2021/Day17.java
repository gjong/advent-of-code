package com.github.gjong.advent.years.y2021;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.github.gjong.advent.geo.Point;

import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

@Day(day = 17, year = 2021, name = "Trick Shot")
public class Day17 implements DaySolver {
    private final InputLoader inputLoader = DayLoader.inputDay17();
    private final Validator validator = DayLoader.validatorDay17();

    private final Pattern AREA_PATTERN = Pattern.compile(".*x=([\\-\\d]+)..([\\-\\d]+), y=([\\-\\d]+)..([\\-\\d]+).*");

    @Override
    public void part1() {
        var targetArea = generate();
        var highestY = 0;

        var dx = 1;
        var dy = 1;
        while (dy < 200) { // random limit to avoid running forever
            var probe = new Probe(new Point(dx, dy));
            while (notOvershot(probe.getPosition(), targetArea)) {
                probe.move();
                if (targetArea.isHit(probe.getPosition()) && highestY < probe.getHighestY()) {
                    highestY = probe.getHighestY();
                    break;
                }
            }

            if (dx < targetArea.rightBottom().x()) {
                dx++;
            } else {
                dx = 0;
                dy++;
            }
        }

        validator.part1(highestY);
    }

    @Override
    public void part2() {
        var targetArea = generate();
        var hittingTrajectories = 0;

        var dx = 1;
        var dy = Math.min(targetArea.leftTop().y(), targetArea.rightBottom().y());
        while (dy < 200) { // random limit to avoid running forever
            var probe = new Probe(new Point(dx, dy));
            while (notOvershot(probe.getPosition(), targetArea)) {
                probe.move();
                if (targetArea.isHit(probe.getPosition())) {
                    hittingTrajectories++;
                    break;
                }
            }

            if (dx < targetArea.rightBottom().x()) {
                dx++;
            } else {
                dx = 0;
                dy++;
            }
        }

        validator.part2(hittingTrajectories);
    }

    private TargetArea generate() {
        var matcher = AREA_PATTERN.matcher(inputLoader.string().trim());
        if (matcher.matches()) {
            return new TargetArea(
                    new Point(
                            parseInt(matcher.group(1)),
                            parseInt(matcher.group(3))),
                    new Point(
                            parseInt(matcher.group(2)),
                            parseInt(matcher.group(4))
                    ));
        } else {
            throw new IllegalArgumentException("Invalid input");
        }
    }

    private boolean notOvershot(Point position, TargetArea targetArea) {
        var lowestY = Math.min(targetArea.leftTop().y(), targetArea.rightBottom().y());
        return position.x() <= targetArea.rightBottom().x() && position.y() >= lowestY;
    }

    static class Probe {
        private Point velocity;
        private Point position;
        private int highestY;

        public Probe(Point velocity) {
            this.velocity = velocity;
            this.position = new Point(0, 0);
            this.highestY = 0;
        }

        public void move() {
            position = position.translate(velocity);
            if (highestY < position.y()) {
                highestY = position.y();
            }

            var dx = velocity.x() > 0 ? velocity.x() - 1: 0;
            var dy = velocity.y() - 1;

            velocity = new Point(dx, dy);
        }

        public int getHighestY() {
            return highestY;
        }

        public Point getPosition() {
            return position;
        }
    }

    record TargetArea(Point leftTop, Point rightBottom) {
        public boolean isHit(Point position) {
            return isBetweenXAxis(position.x())
                    && isBetweenYAxis(position.y());
        }

        private boolean isBetweenXAxis(int x) {
            return x >= leftTop.x() && x <= rightBottom.x();
        }

        private boolean isBetweenYAxis(int y) {
            return leftTop.y() <= y && rightBottom.y() >= y;
        }
    }

}
