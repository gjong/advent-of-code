package com.github.gjong.advent.years.y2024;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.Grid;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.github.gjong.advent.geo.Point;
import com.github.gjong.advent.geo.Vector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

@Day(year = 2024, day = 20, name = "Race Condition")
public class Day20 implements DaySolver {

    private final InputLoader inputLoader;
    private final Validator validator;

    public Day20(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void part1() {
        validator.part1(countCheatsWithMaxSteps(2));
    }

    @Override
    public void part2() {
        validator.part2(countCheatsWithMaxSteps(20));
    }

    private int countCheatsWithMaxSteps(int steps) {
        var grid = prepareGrid();
        resetWalls(grid);

        var cheats = 0;
        var pathPositions = grid.find(GridPlace::isNotWall);
        for (var cheatStart : pathPositions) {
            for (var cheatEnd : pathPositions) {
                var stepDistance = Vector.stepsInVector(cheatEnd, cheatStart);
                if (stepDistance <= steps) {
                    var cheatDistance = grid.at(cheatEnd).distance - grid.at(cheatStart).distance - stepDistance;
                    if (cheatDistance >= 100) {
                        cheats++;
                    }
                }
            }
        }
        return cheats;
    }

    /**
     * Resets the distance value of all grid places that are not walls to -1.
     * This makes sure the gain computation is not exceeding the actual gain.
     *
     * @param grid the grid containing the grid places to reset
     */
    private void resetWalls(Grid<GridPlace> grid) {
        grid.find(Predicate.not(GridPlace::isNotWall))
                .stream()
                .map(grid::at)
                .forEach(gridPlace -> gridPlace.distance = -1);
    }

    /**
     * Prepares and processes a grid based on the inputLoader data.
     *
     * @return the prepared Grid object after processing and setting distances for grid places
     */
    private Grid<GridPlace> prepareGrid() {
        var grid = Grid.of(
                Arrays.asList(inputLoader.split("\n")),
                (point, value) -> new GridPlace(value));

        var startPos = grid.find(place -> place.value == 'S');
        var investigate = Set.of(startPos.getFirst());

        var currentDistance = 0;
        while (!investigate.isEmpty()) {
            var nextInvestigate = new HashSet<Point>();

            for (var lookAt : investigate) {
                var gridPlace = grid.at(lookAt);
                gridPlace.setDistance(currentDistance);
                for (var neighbor : lookAt.neighbours()) {
                    var nGridPlace = grid.at(neighbor);
                    if (nGridPlace.isNotWall() && nGridPlace.distance == Integer.MAX_VALUE) {
                        nextInvestigate.add(neighbor);
                    }
                }
            }

            currentDistance++;
            investigate = nextInvestigate;
        }
        return grid;
    }

    private static class GridPlace {
        private final char value;
        private int distance = Integer.MAX_VALUE;

        private GridPlace(char value) {
            this.value = value;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public boolean isNotWall() {
            return value != '#';
        }
    }
}
