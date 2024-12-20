package com.github.gjong.advent.common;

import com.github.gjong.advent.geo.Bounds;
import com.github.gjong.advent.geo.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class Grid<T> {
    private final HashMap<Point, T> actualGridMap = new HashMap<>();
    private final Bounds gridBounds;

    public Grid(int width, int height) {
        gridBounds = new Bounds(0, 0, width, height);
    }

    @Deprecated(forRemoval = true)
    public void populate(List<String> lines, BiFunction<Point, String, T> generator) {
        for (var index = 0; index < lines.size(); index++) {
            var trimmed = lines.get(index).trim();
            for (var position = 0; position < trimmed.length(); position++) {
                actualGridMap.put(
                        new Point(position, index),
                        generator.apply(new Point(position, index), trimmed.substring(position, position + 1)));
            }
        }
    }

    public List<Point> find(Predicate<T> predicate) {
        return actualGridMap.entrySet()
                .stream()
                .filter(e -> predicate.test(e.getValue()))
                .map(Map.Entry::getKey)
                .toList();
    }

    public boolean inBounds(Point p) {
        return inBounds(p.x(), p.y());
    }

    public boolean inBounds(int x, int y) {
        return x > -1 && x < width()
                && y > -1 && y < height();
    }

    public int width() {
        return gridBounds.width();
    }

    public int height() {
        return gridBounds.height();
    }

    public T at(int x, int y) {
        return actualGridMap.get(Point.of(x, y));
    }

    public T at(Point p) {
        return actualGridMap.get(p);
    }

    public void set(int x, int y, T value) {
        actualGridMap.put(Point.of(x, y), value);
    }

    public static <T> Grid<T> of(List<String> lines, BiFunction<Point, Character, T> generator) {
        var grid = new Grid<T>(lines.getFirst().length(), lines.size());
        for (var index = 0; index < lines.size(); index++) {
            var trimmed = lines.get(index).trim();
            for (var position = 0; position < trimmed.length(); position++) {
                grid.set(position, index, generator.apply(new Point(position, index), trimmed.charAt(position)));
            }
        }
        return grid;
    }
}
