package com.github.gjong.advent.years.y2025;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.List;

@Day(year = 2025, day = 3, name = "Lobby")
public class Day03 implements DaySolver {

    private final InputLoader inputLoader;
    private final Validator validator;

    public Day03(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void part1() {
        int zeroOffset = '0';
        long totalJoltages = 0;
        for (String line : inputLoader.split(System.lineSeparator())) {
            List<Integer> numbers = line.chars().map(c -> c - zeroOffset).boxed().toList();

            // Exclude last when locating (as the highest number should never be the last in the array).
            // we need 2 positions.
            List<Integer> allButLast = numbers.subList(0, numbers.size() - 1);
            int highestNumber = allButLast.stream().max(Integer::compareTo).orElseThrow();
            int firstIndex = allButLast.indexOf(highestNumber);

            // Locate the second-highest number
            List<Integer> fromHighestToEnd = numbers.subList(firstIndex + 1, numbers.size());
            int secondHighestNumber = fromHighestToEnd.stream().max(Integer::compareTo).orElseThrow();

            totalJoltages += Integer.parseInt("" + highestNumber + secondHighestNumber);
        }

        validator.part1(totalJoltages);
    }

    @Override
    public void part2() {
        int zeroOffset = '0';
        long totalJoltages = 0;
        for (String line : inputLoader.split(System.lineSeparator())) {
            List<Integer> numbers = line.chars().map(c -> c - zeroOffset).boxed().toList();

            int lowerBound = 0;
            String foundJoltage = "";
            for (int desiredDigit = 12; desiredDigit > 0; desiredDigit--) {
                List<Integer> fromDesiredToEnd = numbers.subList(lowerBound, numbers.size() - desiredDigit + 1);

                // Find the highest in range
                int highestNumber = fromDesiredToEnd.stream().max(Integer::compareTo).orElseThrow();
                lowerBound += fromDesiredToEnd.indexOf(highestNumber) + 1;

                foundJoltage += highestNumber;
            }
            totalJoltages += Long.parseLong(foundJoltage);
        }

        validator.part2(totalJoltages);
    }
}
