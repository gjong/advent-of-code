package com.github.gjong.advent.years.y2021;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.github.gjong.advent.geo.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.lang.Integer.parseInt;

@Day(day = 13, year = 2021, name = "Transparent Origami")
public class Day13 implements DaySolver {
    private final InputLoader inputLoader = DayLoader.inputDay13();
    private final Validator validator = DayLoader.validatorDay13();

    @Override
    public void part1() {
        var data = inputLoader.split("((\r)?\n){2}");
        var matrix = generateMatrix(data[0]);
        var foldInstructions = generateInstructions(data[1]);

        var folded = foldInstructions.getFirst().apply(matrix);
        validator.part1(folded.countActive());
    }

    @Override
    public void part2() {
        var data = inputLoader.split("((\r)?\n){2}");
        var matrix = generateMatrix(data[0]);
        var foldInstructions = generateInstructions(data[1]);

        var matrixUnderOperation = matrix;
        for (var instruction : foldInstructions) {
            matrixUnderOperation = instruction.apply(matrixUnderOperation);
        }

        validator.part2("\n" + matrixUnderOperation.toString());
    }

    private Matrix generateMatrix(String input) {
        var points = Arrays.stream(input.split("((\r)?\n)"))
                .map(coordinate -> coordinate.split(","))
                .map(point -> new Point(Integer.parseInt(point[0]), Integer.parseInt(point[1])))
                .toList();

        var maxX = points.stream().map(Point::x).max(Integer::compare).get() + 1;
        var maxY = points.stream().map(Point::y).max(Integer::compare).get() + 1;
        var matrix = new Matrix(maxX, maxY);
        points.forEach(p -> matrix.activateBit(p.x(), p.y()));
        return matrix;
    }

    private List<Instruction> generateInstructions(String input) {
        List<Instruction> foldInstructions = new ArrayList<>();
        for (var line : input.split("\n")){
            if (line.startsWith("fold along y=")) {
                foldInstructions.add(source -> source.foldOnY(parseInt(line.replace("fold along y=", ""))));
            } else if (line.startsWith("fold along x=")) {
                foldInstructions.add(source -> source.foldOnX(parseInt(line.replace("fold along x=", ""))));
            }
        }
        return foldInstructions;
    }

    private interface Instruction extends Function<Matrix, Matrix> {
    }

    static class Matrix {

        boolean[][] grid;

        private Matrix(boolean[][] grid) {
            this.grid = grid;
        }

        public Matrix(int width, int height) {
            grid = new boolean[height][width];
        }

        public void activateBit(int x, int y) {
            grid[y][x] = true;
        }

        public int countActive() {
            int count = 0;
            for (var row : grid) {
                for (var column : row) {
                    if (column) {
                        count++;
                    }
                }
            }
            return count;
        }

        public int size() {
            return grid.length * grid[0].length;
        }

        public Matrix foldOnX(int x) {
            var width = Math.max(x - 1, grid[0].length - x - 2);
            var leftShifted = x - width - 1;

            boolean[][] newGrid = new boolean[grid.length][width + 1];
            for (var row = 0; row < grid.length; row ++) {
                for (var column = 0; column < grid[0].length; column++) {
                    if (column == x) {
                        continue;
                    }
                    if (column <= x) {
                        newGrid[row][column + leftShifted] |= grid[row][column];
                    } else {
                        var newXPos = (width + 1) - (column - x);
                        newGrid[row][newXPos] |= grid[row][column];
                    }
                }
            }

            return new Matrix(newGrid);
        }

        public Matrix foldOnY(int y) {
            var height = Math.max(y - 1, grid.length - y - 2);
            var topShifted = y - height - 1;

            boolean[][] newGrid = new boolean[height + 1][grid[0].length];
            for (var row = 0; row < grid.length; row ++) {
                if (row == y) {
                    continue;
                }

                int shiftedY;
                if (row <= y) {
                    shiftedY = row + topShifted;
                } else {
                    shiftedY = (height + 1) - (row - y);
                }

                for (var column = 0; column < grid[0].length; column++) {
                    newGrid[shiftedY][column] |= grid[row][column];
                }
            }

            return new Matrix(newGrid);
        }

        public String toString() {
            var builder = new StringBuilder();
            for (var row : grid) {
                for (var column : row) {
                    builder.append(column ? "#" : " ");
                }
                builder.append("\n");
            }
            return builder.toString();
        }
    }
}
