package com.github.gjong.advent.grid;

import com.github.gjong.advent.geo.Point;
import com.github.gjong.advent.geo.Bounds;

import java.util.ArrayList;
import java.util.List;

public class CharGrid implements Grid<Character> {

    private final char[][] grid;

    private CharGrid(char[][] grid) {
        this.grid = grid;
    }

    public CharGrid(int rows, int cols) {
        this.grid = new char[rows][cols];
        for (var y = 0; y < rows; y++) {
            for (var x = 0; x < cols; x++) {
                grid[y][x] = '.';
            }
        }
    }

    public CharGrid(String input) {
        this.grid = input.lines()
                .map(String::toCharArray)
                .toArray(char[][]::new);
    }

    public String print() {
        var sb = new StringBuilder();
        for (var row : grid) {
            sb.append(row).append("\n");
        }
        return sb.toString();
    }

    public void set(int x, int y, char c) {
        grid[y][x] = c;
    }

    public void set(Point p, char c) {
        set(p.x(), p.y(), c);
    }

    public Character at(int x, int y) {
        if (x < 0 || x >= cols() || y < 0 || y >= rows()) {
            return ' ';
        }
        return grid[y][x];
    }

    public Character at(Point p) {
        return at(p.x(), p.y());
    }

    public int rows() {
        return grid.length;
    }

    public int cols() {
        return grid[0].length;
    }

    public char[] row(int idx) {
        return grid[idx];
    }

    public char[][] grid() {
        return grid;
    }

    public List<Point> findChar(char c) {
        var matches = new ArrayList<Point>();
        for (var x = 0; x < cols(); x++) {
            for (var y = 0; y < rows(); y++) {
                if (at(x, y) == c) {
                    matches.add(new Point(x, y));
                }
            }
        }
        return matches;
    }

    /**
     * Transpose the grid
     */
    public CharGrid transpose() {
        char[][] rotated = new char[grid[0].length][grid.length];
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++){
                rotated[c][r] = grid[r][c];
            }
        }
        return new CharGrid(rotated);
    }

    public Bounds bounds() {
        return new Bounds(0, 0, cols() - 1, rows() - 1);
    }
}
