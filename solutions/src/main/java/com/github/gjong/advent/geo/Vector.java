package com.github.gjong.advent.geo;

import java.util.ArrayList;
import java.util.List;

public record Vector(Point start, Point end) {

    public List<Point> pointsInVector() {
        var direction = new Point(
                Integer.compare(end.x(), start.x()),
                Integer.compare(end.y(), start.y()));

        var x = start.x();
        var y = start.y();

        var points = new ArrayList<Point>();
        while (x != end.x() || y != end.y()) {
            points.add(new Point(x, y));
            x += direction.x();
            y += direction.y();
        }
        points.add(end);

        return points;
    }

    public Point intersectY(int y) {
        var dy = y - start.y();
        var dx = (end.x() - start.x()) / (end.y() - start.y());

        return Point.of(start.x() + (dy / dx), y);
    }

    public Vector translate(Point alternateStart) {
        var deltaX = start.x() - alternateStart.x();
        var deltaY = start.y() - alternateStart.y();

        return new Vector(
                alternateStart,
                end.translate(new Point(deltaX, deltaY)));
    }

    /**
     * Is a line in a 45 degree angle.
     */
    public boolean isDiagonal() {
        return !isVertical() && !isHorizontal()
                && Math.abs(end.x() - start.x()) == Math.abs(end.y() - start.y());
    }

    public boolean isHorizontal() {
        return start.x() != end.x() && start.y() == end.y();
    }

    public boolean isVertical() {
        return start.x() == end.x() && start.y() != end.y();
    }

    public Direction direction() {
        return new Point(
                (int) Math.signum(end.x() - start.x()),
                (int) Math.signum(end.y() - start.y()));
    }

    public static int stepsInVector(Point start, Point end) {
        return Math.abs(end.x() - start.x()) + Math.abs(end.y() - start.y());
    }

    public static Direction direction(Point start, Point end) {
        var x = (int) Math.signum(end.x() - start.x());
        var y = (int) Math.signum(end.y() - start.y());

        if (x == 0 && y == 1) {
            return Point.north;
        } else if (x == 0 && y == -1) {
            return Point.south;
        } else if (x == 1 && y == 0) {
            return Point.east;
        } else if (x == -1 && y == 0) {
            return Point.west;
        } else {
            throw new IllegalArgumentException("Invalid vector: " + start + " -> " + end);
        }
    }
}
