package com.github.gjong.advent.years.y2023;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

@Day(day = 2, year = 2023, name = "Cube Conundrum")
public class Day02 implements DaySolver {

    record Game(int number, int green, int red, int blue) {
    }

    private final InputLoader inputLoader = new InputLoader(2023, 2);
    private final Validator validator = new Validator(2023, 2);

    @Override
    public void part1() {
        var answer = inputLoader.splitOnNewLine()
                .map(this::createGame)
                .filter(game -> game.green() <= 13 && game.red() <= 12 && game.blue() <= 14)
                .mapToInt(Game::number)
                .sum();
        validator.part1(answer);
    }

    @Override
    public void part2() {
        var answer = inputLoader.splitOnNewLine()
                .map(this::createGame)
                // The power of a set of cubes is equal to the numbers of red, green, and blue cubes
                // multiplied together.
                .mapToInt(game -> game.red() * game.green() * game.blue())
                .sum();
        validator.part2(answer);
    }

    private Game createGame(String line) {
        String[] parts = line.split(":");

        var red = 0;
        var green = 0;
        var blue = 0;
        var number = Integer.parseInt(parts[0].substring(5));
        for (var segment : parts[1].split(";")) {
            for (var color : segment.split(",")) {
                var colorParts = color.trim().split(" ");
                var count = Integer.parseInt(colorParts[0]);
                var colorName = colorParts[1];
                switch (colorName) {
                    case "green" -> green = Math.max(green, count);
                    case "red" -> red = Math.max(red, count);
                    case "blue" -> blue = Math.max(blue, count);
                }
            }
        }

        return new Game(number, green, red, blue);
    }
}
