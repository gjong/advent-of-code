package com.github.gjong.advent.years.y2021;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.*;
import java.util.stream.Collectors;

@Day(day = 4, year = 2021, name = "Giant Squid")
public class Day04 implements DaySolver {
    private final InputLoader inputLoader = DayLoader.inputDay4();
    private final Validator validator = DayLoader.validatorDay4();

    @Override
    public void part1() {
        var input = inputLoader.splitOnNewLine().toList();
        var drawnNumbers = new LinkedList<>(Arrays.stream(input.getFirst().split(","))
                .map(Integer::parseInt)
                .toList());
        var boards = generateBoards(input.toArray(String[]::new));

        var lastAnnounced = 0;
        Board winningBoard = null;
        while (winningBoard == null && !drawnNumbers.isEmpty()) {
            lastAnnounced = drawnNumbers.poll();
            announceNumber(boards, lastAnnounced);

            for (var board : boards) {
                if (board.hasBingo()) {
                    winningBoard = board;
                    break;
                }
            }
        }

        assert winningBoard != null;
        validator.part1((long) lastAnnounced * winningBoard.getUnmatchedTotal());
    }

    @Override
    public void part2() {
        var input = inputLoader.splitOnNewLine().toList();
        var drawnNumbers = new LinkedList<>(Arrays.stream(input.getFirst().split(","))
                .map(Integer::parseInt)
                .toList());
        var boards = generateBoards(input.toArray(String[]::new));

        int lastAnnounced = 0;
        while (!drawnNumbers.isEmpty() && boards.size() > 1) {
            announceNumber(boards, drawnNumbers.poll());
            boards.removeIf(Board::hasBingo);
        }

        while (!drawnNumbers.isEmpty()) {
            lastAnnounced = drawnNumbers.poll();
            announceNumber(boards, lastAnnounced);
            if (boards.stream().allMatch(Board::hasBingo)) {
                break;
            }
        }

        validator.part2((long) lastAnnounced * boards.getFirst().getUnmatchedTotal());
    }

    private List<Board> generateBoards(String[] boardLines) {
        var boards = new ArrayList<Board>((boardLines.length - 1) / 6);
        for (var line = 1; line < boardLines.length; line = line + 6) {
            boards.add(new Board(Arrays.copyOfRange(boardLines, line + 1, line + 6)));
        }
        return boards;
    }

    private void announceNumber(List<Board> boards, Integer number) {
        for (Board board : boards) {
            board.hearNumber(number);
        }
    }

    static class Board {
        private final List<List<Integer>> boardRows;

        public Board(String[] lines) {
            boardRows = Arrays.stream(lines)
                    .map(line ->
                            Arrays.stream(line.split(" "))
                                    .filter(number -> !number.isEmpty())
                                    .map(Integer::parseInt)
                                    .collect(Collectors.toList()))
                    .toList();
        }

        public void hearNumber(Integer number) {
            for (var row : boardRows) {
                row.replaceAll(ingoing -> number.equals(ingoing) ? -1 : ingoing);
            }
        }

        public boolean hasBingo() {
            var hasBingo = this.boardRows
                    .stream()
                    .anyMatch(row -> row.stream().allMatch(number -> number == -1));

            int boardSize = 5;
            for (var column = 0; !hasBingo && column < boardSize; column++) {
                var columnBingo = true;
                for (var row = 0; columnBingo && row < boardSize; row++) {
                    columnBingo = boardRows.get(row).get(column) == -1;
                }

                hasBingo = columnBingo;
            }

            return hasBingo;
        }

        public int getUnmatchedTotal() {
            return this.boardRows.stream()
                    .flatMap(Collection::stream)
                    .filter(number -> number > -1)
                    .mapToInt(i -> i)
                    .sum();
        }
    }
}
