package com.github.gjong.advent.years.y2021;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.CharGrid;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.github.gjong.advent.geo.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

@Day(day = 9, year = 2021, name = "Smoke Basin")
public class Day09 implements DaySolver {
    private final InputLoader inputLoader = DayLoader.inputDay9();
    private final Validator validator = DayLoader.validatorDay9();

    private boolean[][] visited;

    @Override
    public void part1() {
        var grid = inputLoader.charGrid();

        var sumLowers = 0;
        for (var row = 0; row < grid.rows(); row++) {
            for (var column = 0; column < grid.cols(); column++) {
                if (isLowestOfNeighbours(grid.grid(), row, column)) {
                    sumLowers += grid.at(column, row) - '0' + 1;
                }
            }
        }
        validator.part1(sumLowers);
    }

    @Override
    public void part2() {
        var grid = inputLoader.charGrid();
        var lowestPoints = new ArrayList<Point>();
        for (var row = 0; row < grid.rows(); row++) {
            for (var column = 0; column < grid.cols(); column++) {
                if (isLowestOfNeighbours(grid.grid(), row, column)) {
                    lowestPoints.add(new Point(row, column));
                }
            }
        }

        visited = new boolean[grid.rows()][grid.cols()];
        var basin = lowestPoints.stream()
                .map(point -> expandBasin(grid, point.x(), point.y()))
                .sorted(Integer::compareTo)
                .collect(Collectors.toList());
        Collections.reverse(basin);

        validator.part2((long) basin.get(0) * basin.get(1) * basin.get(2));
    }

    boolean isLowestOfNeighbours(char[][] grid, int row, int column) {
        var value = grid[row][column];

        return (row == 0 || grid[row - 1][column] > value)
                && (column == 0 || grid[row][column - 1] > value)
                && (column == (grid[0].length - 1) || grid[row][column + 1] > value)
                && (row == (grid.length - 1) || grid[row + 1][column] > value);
    }

    private int expandBasin(CharGrid grid, int row, int column) {
        if (outOfBounds(row, column) || (grid.at(column, row) - '0') == 9 || visited[row][column]) {
            return 0;
        }
        visited[row][column] = true;

        var basinSize = 1; // current point
        basinSize += expandBasin(grid,row + 1, column);
        basinSize += expandBasin(grid,row - 1, column);
        basinSize += expandBasin(grid, row, column + 1);
        basinSize += expandBasin(grid, row, column - 1);
        return basinSize;
    }

    private boolean outOfBounds(int row, int column) {
        return row < 0 || column < 0 || row >= visited.length || column >= visited[0].length;
    }
}
