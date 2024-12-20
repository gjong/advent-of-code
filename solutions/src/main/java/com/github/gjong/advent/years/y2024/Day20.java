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
        var grid = prepareGrid();
        resetWalls(grid);

        var cheats = 0;
        var pathPositions = grid.find(GridPlace::isNotWall);
        for (var position : pathPositions) {
            cheats += position.neighbours()
                    .stream()
                    .filter(neighbour -> !grid.at(neighbour).isNotWall())
                    .mapToInt(wall -> countGoodCheat(grid, wall, position))
                    .sum();
        }

        validator.part1(cheats);
    }

    @Override
    public void part2() {
        var grid = prepareGrid();
        resetWalls(grid);

        var cheats = 0;
        var pathPositions = grid.find(GridPlace::isNotWall);
        for (var cheatStart : pathPositions) {
            for (var cheatEnd : pathPositions) {
                var stepDistance = Vector.stepsInVector(cheatEnd, cheatStart);
                if (stepDistance <= 20) {
                    // compute gained distance
                    var pointDistance = grid.at(cheatEnd).distance - grid.at(cheatStart).distance;
                    var cheatDistance = pointDistance - stepDistance;
                    if (cheatDistance >= 100) {
                        cheats++;
                    }
                }
            }
        }

        validator.part2(cheats);
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
     * Counts the number of good cheats based on the given grid, wall position, and cheat starting position.
     *
     * @param grid the grid containing the grid places to analyze
     * @param wall the position of the wall to check for neighboring cheats
     * @param cheatStart the position of the cheat starting point
     * @return the number of good cheats found around the wall position
     */
    private int countGoodCheat(Grid<GridPlace> grid, Point wall, Point cheatStart) {
        var cheats = 0;
        for (var cheatPos : wall.neighbours()) {
            if (grid.inBounds(cheatPos)) {
                var potentialGain = grid.at(cheatPos).distance - grid.at(cheatStart).distance;
                if (potentialGain > 100) {
                    cheats++;
                }
            }
        }
        return cheats;
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
