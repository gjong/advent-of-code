package com.github.gjong.advent.years.y2023;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.CharGrid;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.github.gjong.advent.geo.Point;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Day(year = 2023, day = 16)
public class Day16 implements DaySolver {
    private final InputLoader inputLoader = new InputLoader(2023, 16);
    private final Validator validator = new Validator(2023, 16);

    private record Movement(Point position, Direction direction) {}

    public enum Direction {
        RIGHT(Point.zero.right()),
        DOWN(Point.zero.down()),
        LEFT(Point.zero.left()),
        UP(Point.zero.up());
        private final Point vector;

        Direction(Point vector) {
            this.vector = vector;
        }
    }

    private final Map<Character, Map<Direction, List<Direction>>> conversion = Map.of(
            '.', Map.of(Direction.RIGHT, List.of(Direction.RIGHT), Direction.DOWN, List.of(Direction.DOWN), Direction.LEFT, List.of(Direction.LEFT), Direction.UP, List.of(Direction.UP)),
            '|', Map.of(Direction.RIGHT, List.of(Direction.DOWN, Direction.UP),Direction.DOWN, List.of(Direction.DOWN),Direction.LEFT, List.of(Direction.DOWN, Direction.UP),Direction.UP, List.of(Direction.UP)),
            '-', Map.of(Direction.RIGHT, List.of(Direction.RIGHT),Direction.DOWN, List.of(Direction.RIGHT, Direction.LEFT),Direction.LEFT, List.of(Direction.LEFT),Direction.UP, List.of(Direction.RIGHT, Direction.LEFT)),
            '\\', Map.of(Direction.RIGHT, List.of(Direction.DOWN), Direction.DOWN, List.of(Direction.RIGHT),Direction.LEFT, List.of(Direction.UP),Direction.UP, List.of(Direction.LEFT)),
            '/', Map.of(Direction.RIGHT, List.of(Direction.UP),Direction.DOWN, List.of(Direction.LEFT),Direction.LEFT, List.of(Direction.DOWN),Direction.UP, List.of(Direction.RIGHT)));

    @Override
    public void part1() {
        var answer = solve(new Movement(new Point(0, 0), Direction.RIGHT), inputLoader.charGrid());
        validator.part1(answer);
    }

    @Override
    public void part2() {
        var grid = inputLoader.charGrid();
        // since the grid is square, we can start at all edges at once
        var answer = IntStream.range(0, grid.rows())
                .mapToObj(r -> Stream.of(
                        new Movement(Point.of(r, 0), Direction.DOWN),
                        new Movement(Point.of(r, grid.cols() - 1), Direction.UP),
                        new Movement(Point.of(0, r), Direction.RIGHT),
                        new Movement(Point.of(grid.rows() - 1, r), Direction.LEFT))
                )
                .flatMap(Function.identity())
                .parallel()
                .mapToLong(start -> solve(start, grid))
                .max()
                .getAsLong();
        validator.part2(answer);
    }

    private long solve(Movement start, CharGrid grid) {
        var bounds = grid.bounds();

        var queue = new Stack<Movement>();
        queue.add(start);

        var visited = new HashSet<Movement>();
        while (!queue.isEmpty()) {
            var move = queue.pop();
            for (var directions : getDirections(grid.at(move.position.x(), move.position.y()), move.direction)) {
                var nextPoint = move.position.translate(directions.vector);
                var nextState = new Movement(nextPoint, directions);
                if (bounds.inBounds(nextPoint) && !visited.contains(nextState)) {
                    visited.add(nextState);
                    queue.add(nextState);
                }
            }
        }

        return visited.stream()
                .map(m -> m.position)
                .distinct()
                .count() + 1;
    }

    private List<Direction> getDirections(char c, Direction direction) {
        return conversion.get(c).get(direction);
    }

}
