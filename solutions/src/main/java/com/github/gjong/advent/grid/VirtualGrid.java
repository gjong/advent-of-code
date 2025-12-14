package com.github.gjong.advent.grid;

import com.github.gjong.advent.geo.Bounds;
import com.github.gjong.advent.geo.Point;

public class VirtualGrid<X> implements Grid<X> {

    private final Grid<X> delegate;
    private final int width;
    private final int height;
    private Point locationInGrid;

    public VirtualGrid(Grid<X> delegate, int width, int height) {
        this.delegate = delegate;
        this.width = width;
        this.height = height;
    }

    public void position(int x, int y) {
        locationInGrid = Point.of(x, y);
    }

    public X at() {
        return delegate.at(locationInGrid.x(), locationInGrid.y());
    }

    @Override
    public X at(int x, int y) {
        Bounds bounds = bounds();
        int actualX = bounds.x() + x;
        int actualY = bounds.y() + y;
        return delegate.at(actualX, actualY);
    }

    public int count(X value) {
        int counted = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (at(x, y).equals(value)) {
                    counted++;
                }
            }
        }
        return counted;
    }

    @Override
    public int rows() {
        return height;
    }

    @Override
    public int cols() {
        return width;
    }

    @Override
    public Bounds bounds() {
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        return new Bounds(
                locationInGrid.x() - halfWidth,
                locationInGrid.y() - halfHeight,
                locationInGrid.x() + halfWidth,
                locationInGrid.y() + halfHeight);
    }
}
