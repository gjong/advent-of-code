package com.github.gjong.advent.geo;

public record Bounds(int x, int y, int width, int height) {

    public boolean inBounds(Point point) {
        return point.x() >= x && point.x() <= x + width && point.y() >= y && point.y() <= y + height;
    }
}
