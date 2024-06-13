package com.github.gjong.advent.common;

import com.github.gjong.advent.geo.Point;
import com.jongsoft.lang.collection.Sequence;

import java.util.List;
import java.util.function.BiFunction;

public class Grid<T> {
    private final T[][] actualGrid;

    public Grid(int width, int height) {
        actualGrid = (T[][])new Object[height][width];
    }

    public void fill(T value) {
        for (var y = 0; y < actualGrid.length; y++) {
            for (var x = 0; x < actualGrid[0].length; x++) {
                actualGrid[y][x] = value;
            }
        }
    }

    public void populate(List<String> lines, BiFunction<Point, String, T> generator) {
        for (var index = 0; index < lines.size(); index++) {
            var trimmed = lines.get(index).trim();
            for (var position = 0; position < trimmed.length(); position++) {
                actualGrid[index][position] = generator.apply(
                        new Point(position, index),
                        String.valueOf(trimmed.charAt(position)));
            }
        }
    }

    public boolean inBounds(int x, int y) {
        return x > -1 && x < width()
                && y > -1 && y < height();
    }

    public int width() {
        return actualGrid[0].length;
    }

    public int height() {
        return actualGrid.length;
    }

    public T at(int x, int y) {
        return actualGrid[y][x];
    }

    public void set(int x, int y, T value) {
        actualGrid[y][x] = value;
    }
}
