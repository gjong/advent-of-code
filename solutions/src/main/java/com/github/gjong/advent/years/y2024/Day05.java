package com.github.gjong.advent.years.y2024;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Day(year = 2024, day = 5, name = "Print Queue")
public class Day05 implements DaySolver {

    private static final Set<String> EMPTY = Collections.emptySet();

    private final Logger log = LoggerFactory.getLogger(Day05.class);

    private final InputLoader inputLoader;
    private final Validator validator;

    private final HashMap<String, Set<String>> priorityMap = new HashMap<>();
    private final List<List<String>> printOrders = new ArrayList<>();

    private final Comparator<String> priorityComparator = (left, right) -> {
        if (priorityMap.getOrDefault(right, EMPTY).contains(left)) {
            return -1;
        }

        if (priorityMap.getOrDefault(left, EMPTY).contains(right)) {
            return 1;
        }

        return 0;
    };

    public Day05(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void readInput() {
        priorityMap.clear();
        printOrders.clear();

        inputLoader.consumeLine(line -> {
            if (line.contains(",")) {
                printOrders.add(List.of(line.split(",")));
            } else if (line.contains("|")) {
                var split = line.split("\\|");
                priorityMap.computeIfAbsent(split[1].trim(), s -> new HashSet<>())
                        .add(split[0].trim());
            }
        });
    }

    @Override
    public void part1() {
        var result = printOrders.stream()
                .filter(this::printQueueValid)
                .map(orderSet -> orderSet.get(orderSet.size() / 2))
                .mapToInt(Integer::parseInt)
                .sum();

        validator.part1(result);
    }

    @Override
    public void part2() {
        var result = printOrders.stream()
                .filter(orderSet -> !printQueueValid(orderSet))
                .map(this::reorderToValid)
                .map(orderSet -> orderSet.get(orderSet.size() / 2))
                .mapToInt(Integer::parseInt)
                .sum();

        validator.part2(result);
    }

    private List<String> reorderToValid(List<String> incorrectPrintOrder) {
        return incorrectPrintOrder.stream()
                .sorted(priorityComparator)
                .toList();
    }

    private boolean printQueueValid(List<String> pagePrintOrder) {
        for (var idx = 0; idx < pagePrintOrder.size(); idx++) {
            var page = pagePrintOrder.get(idx);

            var constIdx = idx;
            var errorInQueue = priorityMap.getOrDefault(page, Set.of())
                    .stream()
                    .anyMatch(precedingPage -> pagePrintOrder.indexOf(precedingPage) > constIdx);
            if (errorInQueue) {
                return false;
            }
        }

        log.debug("Found valid order: {}", pagePrintOrder);
        return true;
    }
}
