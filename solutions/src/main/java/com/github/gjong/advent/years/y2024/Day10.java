package com.github.gjong.advent.years.y2024;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.CharGrid;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.github.gjong.advent.geo.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

@Day(year = 2024, day = 10, name = "Hoof it")
public class Day10 implements DaySolver {

    private final InputLoader inputLoader;
    private final Validator validator;

    public Day10(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void part1() {
        var grid = inputLoader.charGrid();
        var tailheads = grid.findChar('0');

        var paths = tailheads.stream()
                .mapToLong(tailhead -> resolvablePaths(tailhead, grid, true))
                .sum();

        validator.part1(paths);
    }

    @Override
    public void part2() {
        var grid = inputLoader.charGrid();
        var tailheads = grid.findChar('0');

        var paths = tailheads.stream()
                .mapToLong(tailhead -> resolvablePaths(tailhead, grid, false))
                .sum();

        validator.part2(paths);
    }

    private int resolvablePaths(Point tailhead, CharGrid grid, boolean trackVisited) {
        var visited = new HashSet<Point>();
        var score = 0;

        var processing = new LinkedList<Point>();
        processing.offer(tailhead);

        while (!processing.isEmpty()) {
            var current = processing.poll();
            for (var neighbour : current.neighbours()) {
                if ((!trackVisited || !visited.contains(neighbour)) && (grid.at(current) - '0') == (grid.at(neighbour) - '1')) {
                    visited.add(neighbour);
                    if ((grid.at(neighbour) - '0') == 9) {
                        score++;
                    } else {
                        processing.offer(neighbour);
                    }
                }
            }
        }

        return score;
    }
}
