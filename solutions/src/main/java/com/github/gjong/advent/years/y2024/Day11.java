package com.github.gjong.advent.years.y2024;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import org.slf4j.Logger;

import java.util.*;

@Day(year = 2024, day = 11, name = "Plutonian Pebbles")
public class Day11 implements DaySolver {
    private final Logger log = org.slf4j.LoggerFactory.getLogger(Day11.class);

    private final InputLoader inputLoader;
    private final Validator validator;

    private final List<String> stones = new ArrayList<>();
    record CachedKey(int round, long stone) {}

    public Day11(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void readInput() {
        stones.addAll(Arrays.asList(inputLoader.split(" ")));
    }

    @Override
    public void part1() {
        var cache = new HashMap<CachedKey, Long>();
        var counted = stones.stream()
                .map(Long::parseLong)
                .mapToLong(stone -> stonesAfterBlink(25, stone, cache))
                .sum();

        validator.part1(counted);
    }

    private long stonesAfterBlink(int blinkRemaining, long stone, Map<CachedKey, Long> cache) {
        if (cache.containsKey(new CachedKey(blinkRemaining, stone))) {
            return cache.get(new CachedKey(blinkRemaining, stone));
        }

        var counted = 0L;
        if (blinkRemaining == 0) {
            counted = 1;
        } else if (stone == 0) {
            counted = stonesAfterBlink(blinkRemaining - 1, 1, cache);
        } else {
            int amountOfDigits = Long.toString(stone).length();
            if (amountOfDigits % 2 == 0) {
                var middle = amountOfDigits / 2;
                var modulo = (int) Math.pow(10, middle);
                var right = stone % modulo;
                var left = (stone - right) / modulo;

                counted = stonesAfterBlink(blinkRemaining - 1, left, cache)
                        + stonesAfterBlink(blinkRemaining - 1, right, cache);
            } else {
                counted = stonesAfterBlink(blinkRemaining - 1, stone * 2024, cache);
            }
        }
        cache.put(new CachedKey(blinkRemaining, stone), counted);

        return counted;
    }

    @Override
    public void part2() {
        var cache = new HashMap<CachedKey, Long>();
        var counted = stones.stream()
                .map(Long::parseLong)
                .mapToLong(stone -> stonesAfterBlink(75, stone, cache))
                .sum();

        validator.part2(counted);
    }
}
