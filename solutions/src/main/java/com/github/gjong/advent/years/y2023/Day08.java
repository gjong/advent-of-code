package com.github.gjong.advent.years.y2023;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.algo.Algo;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Day(day = 8, year = 2023, name = "Haunted Wasteland")
public class Day08 implements DaySolver {
    private final InputLoader inputLoader = new InputLoader(2023, 8);
    private final Validator validator = new Validator(2023, 8);

    /**
     * This record represents a possible move in the network.
     * It contains the current position and the positions to the left and right.
     */
    record PossibleMove(String position, String left, String right) {
    }

    @Override
    public void part1() {
        var answer = solve(inputLoader.string(), "AAA", "ZZZ");
        validator.part1(answer);
    }

    @Override
    public void part2() {
        var answer = solve(inputLoader.string(), "A", "Z");
        validator.part2(answer);
    }

    /**
     * This method solves the problem.
     * It processes the input to get the possible moves and then navigates the network.
     */
    private long solve(String input, String start, String end) {
        var scanner = new Scanner(input);

        var moves = scanner.nextLine().toCharArray();
        var possibleMoves = processMoves(scanner);

        return possibleMoves.keySet()
                .stream()
                .filter(m -> m.endsWith(start))
                .mapToLong(pos -> solve(possibleMoves, moves, pos, end))
                .reduce(Algo::lcm)
                .orElseThrow();
    }

    /**
     * This method navigates the network from the start node to the end node.
     * It uses the directions array to decide whether to move left or right at each step.
     */
    private long solve(Map<String, PossibleMove> moves, char[] directions, String start, String end) {
        var steps = 0;
        while (!start.endsWith(end)) {
            start = directions[steps++ % directions.length] == 'L'
                    ? moves.get(start).left()
                    : moves.get(start).right();
        }
        return steps;
    }

    /**
     * Format of the input is:
     * AAA = (BBB, CCC)
     * BBB = (DDD, EEE)
     * CCC = (ZZZ, GGG)
     */
    private Map<String, PossibleMove> processMoves(Scanner input) {
        Pattern pattern = Pattern.compile("(\\w+)\\s=\\s\\((\\w+),\\s(\\w+)\\)");

        Map<String, PossibleMove> moves = new HashMap<>();
        while (input.hasNextLine()) {
            Matcher matcher = pattern.matcher(input.nextLine());
            if (matcher.find()) {
                String position = matcher.group(1);
                String leftMove = matcher.group(2);
                String rightMove = matcher.group(3);

                moves.put(position, new PossibleMove(position, leftMove, rightMove));
            }

        }
        return moves;
    }
}
