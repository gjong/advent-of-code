package com.github.gjong.advent.years.y2022;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.github.gjong.advent.geo.Point;
import com.github.gjong.advent.geo.Vector;

import java.util.Arrays;
import java.util.HashSet;

import static java.lang.Integer.parseInt;

@Day(day = 9, year = 2022, name = "Rope Bridge")
public class Day09 implements DaySolver {
    private final InputLoader inputLoader = DayLoader.inputDay9();
    private final Validator validator = DayLoader.validatorDay9();

    @Override
    public void part1() {
        var answer = solve(new Rope(2));
        validator.part1(answer);
    }

    @Override
    public void part2() {
        var answer = solve(new Rope(10));
        validator.part2(answer);
    }

    private int solve(Rope rope) {
        var tailVisits = new HashSet<>();
        inputLoader.consumeLine(line -> {
            var instruction = Instruction.ofLine(line);
            var deltaPoint = instruction.direction().getTranslation();
            for (var step = 0; step < instruction.stepSize(); step++) {
                rope.move(deltaPoint);
                tailVisits.add(rope.tail());
            }
        });

        return tailVisits.size();
    }

    class Rope {
        private final Point[] knots;

        Rope(int length) {
            knots = new Point[length];
            Arrays.fill(knots, Point.zero);
        }

        void move(Point translation) {
            knots[0] = knots[0].translate(translation);
            for (var knotIdx = 1; knotIdx < knots.length; knotIdx++) {
                var knot = knots[knotIdx];
                var previousKnot = knots[knotIdx - 1];
                if (!knot.touches(previousKnot)) {
                    var vector = new Vector(knot, previousKnot);
                    knots[knotIdx] = knot.translate(vector.direction());
                }
            }
        }

        Point tail() {
            return knots[knots.length - 1];
        }
    }

    enum Direction {
        R(new Point(1, 0)),
        U(new Point(0, -1)),
        D(new Point(0, 1)),
        L(new Point(-1, 0));
        private final Point translation;

        Direction(Point translation) {
            this.translation = translation;
        }

        public Point getTranslation() {
            return translation;
        }
    }

    record Instruction(Direction direction, int stepSize) {
        static Instruction ofLine(String line) {
            var split = line.split(" ");
            return new Instruction(
                    Direction.valueOf(split[0]),
                    parseInt(split[1]));
        }
    }
}
