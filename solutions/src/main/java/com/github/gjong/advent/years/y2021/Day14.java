package com.github.gjong.advent.years.y2021;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.jongsoft.lang.API;
import com.jongsoft.lang.collection.tuple.Pair;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Day(day = 14, year = 2021, name = "Extended Polymerization")
public class Day14 implements DaySolver {
    private final InputLoader inputLoader = DayLoader.inputDay14();
    private final Validator validator = DayLoader.validatorDay14();

    @Override
    public void part1() {
        var data = inputLoader.splitOnNewLine().toList();
        var polymer = new ArrayList<>(Arrays.stream(data.getFirst().split("")).toList());

        var polymerGrowMap = new HashMap<String, String>(data.size() - 2);
        for (var index = 2; index < data.size(); index++) {
            var mapping = API.Tuple(data.get(index).split(" -> "));
            polymerGrowMap.put(mapping.getFirst(), mapping.getSecond());
        }

        var mostCommonCount = 0L;
        var leastCommonCount = 0L;
        for (var growOperation = 0; growOperation < 10; growOperation++) {
            growPolymer(polymer, polymerGrowMap);
        }

        var countedMap = polymer.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .toList();

        leastCommonCount = countedMap.getFirst().getValue();
        mostCommonCount = countedMap.getLast().getValue();

        validator.part1(mostCommonCount - leastCommonCount);
    }

    @Override
    public void part2() {
        var data = inputLoader.splitOnNewLine().toList();

        var polymer = data.getFirst();
        var polymerGrowMap = new HashMap<String, Pair<String, String>>(data.size() - 2);
        for (var index = 2; index < data.size(); index++) {
            var mapping = API.Tuple(data.get(index).split(" -> "));
            polymerGrowMap.put(
                    mapping.getFirst(),
                    API.Tuple(
                            mapping.getFirst().charAt(0) + mapping.getSecond(),
                            mapping.getSecond() + mapping.getFirst().charAt(1)));
        }

        var mostCommonCount = BigInteger.ZERO;
        var leastCommonCount = BigInteger.ZERO;

        Map<String, BigInteger> polymerPairMap = new HashMap<>();
        for (var position = 0; position < (polymer.length() - 1); position++) {
            var pair = polymer.substring(position, position + 2);
            polymerPairMap.compute(pair, (k, counted) -> counted != null ? counted.add(BigInteger.ONE) : BigInteger.ONE);
        }

        for (var round = 0; round < 40; round++) {
            polymerPairMap = handleRound(polymerPairMap, polymerGrowMap);
        }

        Map<Character, BigInteger> counted = new HashMap<>();
        for (var entry : polymerPairMap.entrySet()) {
            var letter = entry.getKey().charAt(0);

            counted.compute(letter, (k, count) -> count != null ? count.add(entry.getValue()) : entry.getValue());
        }
        var lastLetter = polymer.charAt(polymer.length() - 1);
        counted.put(lastLetter, counted.computeIfAbsent(lastLetter, k -> BigInteger.ZERO).add(BigInteger.ONE));

        var counts = counted.values()
                .stream()
                .sorted().toList();

        leastCommonCount = counts.getFirst();
        mostCommonCount = counts.getLast();

        validator.part2(mostCommonCount.subtract(leastCommonCount));
    }

    private void growPolymer(List<String> polymer, HashMap<String, String> polymerGrowMap) {
        var pairIndex = 0;
        while ((pairIndex + 1) < polymer.size()) {
            var lookup = polymer.get(pairIndex) + polymer.get(pairIndex + 1);
            var addition = polymerGrowMap.get(lookup);
            polymer.add(pairIndex + 1, addition);
            pairIndex += 2;
        }
    }

    private Map<String, BigInteger> handleRound(Map<String, BigInteger> polymerPairMap, Map<String, Pair<String, String>> polymerGrowMap) {
        Map<String, BigInteger> updatedGrowMap = new HashMap<>();
        for (var growSet : polymerGrowMap.entrySet()) {
            var currentCount = polymerPairMap.get(growSet.getKey());
            if (currentCount == null) {
                continue;
            }

            updatedGrowMap.put(
                    growSet.getValue().getFirst(),
                    updatedGrowMap.computeIfAbsent(growSet.getValue().getFirst(), key -> BigInteger.ZERO).add(currentCount));
            updatedGrowMap.put(
                    growSet.getValue().getSecond(),
                    updatedGrowMap.computeIfAbsent(growSet.getValue().getSecond(), key -> BigInteger.ZERO).add(currentCount));
        }
        return updatedGrowMap;
    }
}
