package com.github.gjong.advent.years.y2021;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.math.BigInteger;
import java.util.Arrays;

import static java.lang.Integer.parseInt;

@Day(day = 6, year = 2021, name = "Lanternfish")
public class Day06 implements DaySolver {
    private final InputLoader inputLoader = DayLoader.inputDay6();
    private final Validator validator = DayLoader.validatorDay6();

    @Override
    public void part1() {
        validator.part1(solve(80));
    }

    @Override
    public void part2() {
        validator.part2(solve(256));
    }

    public BigInteger solve(int amountOfDays) {
        var fishOnDayStart = new long[Simulator.MAX_REPRODUCTION_AGE];

        Arrays.stream(inputLoader.split(","))
                .forEach(startAmount -> fishOnDayStart[parseInt(startAmount.trim())]++);

        return new Simulator(amountOfDays)
                .simulateWithStart(fishOnDayStart);
    }

    static class Simulator {
        public static final int MAX_REPRODUCTION_AGE = 9;
        private final int amountOFDays;

        public Simulator(int amountOFDays) {
            this.amountOFDays = amountOFDays;
        }

        public BigInteger simulateWithStart(long[] amountOfFisherPerDay) {
            BigInteger[] fishCycles = new BigInteger[MAX_REPRODUCTION_AGE];
            for (var idx=0; idx < amountOfFisherPerDay.length; idx++) {
                fishCycles[idx] = BigInteger.valueOf(amountOfFisherPerDay[idx]);
            }

            var currentDay = 0;
            while (currentDay < amountOFDays) {
                var newBorn = fishCycles[0];
                System.arraycopy(fishCycles, 1, fishCycles, 0, MAX_REPRODUCTION_AGE - 1);

                fishCycles[6] = fishCycles[6].add(newBorn);
                fishCycles[MAX_REPRODUCTION_AGE - 1] = newBorn;
                currentDay++;
            }

            BigInteger result = BigInteger.ZERO;
            for (BigInteger fishCycle : fishCycles) {
                result = result.add(fishCycle);
            }

            return result;
        }
    }
}
