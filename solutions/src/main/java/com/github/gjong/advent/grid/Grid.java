package com.github.gjong.advent.grid;

import com.github.gjong.advent.geo.Bounds;
import com.github.gjong.advent.geo.Point;

public interface Grid<T> {

    T at(int x, int y);

    default T at(Point p) {
        return at(p.x(), p.y());
    }

    int rows();

    int cols();

    Bounds bounds();

    default VirtualGrid<T> virtual(int width, int height) {
        return new VirtualGrid<>(this, width, height);
    }
}
