package com.github.gjong.advent.years.y2025;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.geo.Point;
import com.github.gjong.advent.grid.CharGrid;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.github.gjong.advent.grid.VirtualGrid;

import java.util.ArrayList;
import java.util.List;

@Day(year = 2025, day = 4, name = "Printing Department")
public class Day04 implements DaySolver {

    private final InputLoader inputLoader;
    private final Validator validator;

    private CharGrid grid;

    private record ValidRolls(int count, List<Point> locations) {}

    public Day04(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void part1() {
        ValidRolls validRolls = determineValidRolls();
        validator.part1(validRolls.count);
    }

    @Override
    public void part2() {
        long validRolls = 0;
        ValidRolls validLocations = determineValidRolls();
        while (!validLocations.locations.isEmpty()) {
            validRolls += validLocations.count;

            // remove the roll from the grid for the next iteration
            validLocations.locations.forEach(p -> grid.set(p, '.'));
            validLocations = determineValidRolls();
        }

        validator.part2(validRolls);
    }

    private ValidRolls determineValidRolls() {
        int rollCount = 0;
        List<Point> locations = new ArrayList<>();

        VirtualGrid<Character> virtualGrid = grid.virtual(3, 3);
        for (int y = 0; y < grid.rows(); y++) {
            for (int x = 0; x < grid.cols(); x++) {
                virtualGrid.position(x, y);
                if (virtualGrid.at() == '@' && virtualGrid.count('@') <= 4) {
                    rollCount++;
                    locations.add(new Point(x, y));
                }
            }
        }

        return new ValidRolls(rollCount, locations);
    }

    @Override
    public void readInput() {
        grid = inputLoader.charGrid();
    }
}
