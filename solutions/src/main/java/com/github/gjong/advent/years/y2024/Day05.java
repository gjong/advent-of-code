package com.github.gjong.advent.years.y2024;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Day(year = 2024, day = 5, name = "Print Queue")
public class Day05 implements DaySolver {

    private final Logger log = LoggerFactory.getLogger(Day05.class);

    private final InputLoader inputLoader;
    private final Validator validator;

    private final HashMap<String, Set<String>> priorityMap = new HashMap<>();
    private List<List<String>> printOrders;

    public Day05(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void readInput() {
        var dataSplit = inputLoader.string().split("\n\n");

        // put the page requiring another page as the key and the required one as the value
        dataSplit[0].lines()
                .map(line -> line.split("\\|"))
                .forEach(split -> priorityMap.computeIfAbsent(split[1].trim(), s -> new HashSet<>())
                        .add(split[0].trim()));

        printOrders = dataSplit[1].lines()
                .map(line -> line.split(","))
                .map(List::of)
                .toList();
    }

    @Override
    public void part1() {
        var result = printOrders.stream()
                .filter(orderSet -> {
                    for (var idx = 0; idx < orderSet.size(); idx++) {
                        var page = orderSet.get(idx);

                        var constIdx = idx;
                        var errorInQueue = priorityMap.getOrDefault(page, Set.of())
                                .stream()
                                .anyMatch(precedingPage -> orderSet.indexOf(precedingPage) > constIdx);
                        if (errorInQueue) {
                            return false;
                        }
                    }

                    log.debug("Found valid order: {}", orderSet);
                    return true;
                })
                .map(orderSet -> orderSet.get(orderSet.size() / 2))
                .mapToInt(Integer::parseInt)
                .sum();

        validator.part1(result);
    }

    @Override
    public void part2() {
        var emptyList = Set.<String>of();
        var result = printOrders.stream()
                .filter(orderSet -> {
                    for (var idx = 0; idx < orderSet.size(); idx++) {
                        var page = orderSet.get(idx);

                        var constIdx = idx;
                        var errorInQueue = priorityMap.getOrDefault(page, emptyList)
                                .stream()
                                .anyMatch(precedingPage -> orderSet.indexOf(precedingPage) > constIdx);
                        if (errorInQueue) {
                            return true;
                        }
                    }
                    return false;
                })
                .map(orderSet -> orderSet.stream()
                        .sorted((left, right) -> {
                            if (priorityMap.getOrDefault(right, emptyList).contains(left)) {
                                return -1;
                            }

                            if (priorityMap.getOrDefault(left, emptyList).contains(right)) {
                                return 1;
                            }

                            return 0;
                        })
                        .toList())
                .map(orderSet -> orderSet.get(orderSet.size() / 2))
                .mapToInt(Integer::parseInt)
                .sum();

        validator.part2(result);
    }
}
