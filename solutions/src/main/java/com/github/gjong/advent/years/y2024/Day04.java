package com.github.gjong.advent.years.y2024;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.geo.Vector;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;


@Day(year = 2024, day = 4, name = "Ceres Search")
public class Day04 implements DaySolver {

    private final InputLoader inputLoader;
    private final Validator validator;

    public Day04(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void part1() {
        var counted = 0;
        var grid = inputLoader.charGrid();

        for (var start : grid.findChar('X')) {
            var neighbours = start.allNeighbours();
            for (var neighbour : neighbours) {
                if (grid.at(neighbour) == 'M') {
                    var direction = new Vector(start, neighbour).direction();
                    var aCheck = neighbour.translate(direction);
                    var sCheck = aCheck.translate(direction);

                    if (grid.at(aCheck) == 'A' && grid.at(sCheck) == 'S') {
                        counted++;
                    }
                }
            }
        }

        validator.part1(counted);
    }

    @Override
    public void part2() {
        var counted = 0;
        var grid = inputLoader.charGrid();

        for (var start : grid.findChar('A')) {
            var tlbrValid = grid.at(start.left().up()) == 'M' && grid.at(start.right().down()) == 'S';
            var brtlValid = grid.at(start.down().right()) == 'M' && grid.at(start.up().left()) == 'S';

            var bltrValid = grid.at(start.left().down()) == 'M' && grid.at(start.right().up()) == 'S';
            var trblValid = grid.at(start.up().right()) == 'M' && grid.at(start.down().left()) == 'S';

            if ((tlbrValid || brtlValid) && (bltrValid || trblValid)) {
                counted++;
            }
        }

        validator.part2(counted);
    }
}
