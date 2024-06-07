package com.github.gjong.advent;

import org.slf4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class AdventOfCode {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AdventOfCode.class);

    private static final String LINE_TEMPLATE = "| % 5d |  %02d  | %-35s | % 5dms | % 5dms |\n";

    public static void main(String[] args) {
        var solutionsPerYear = ServiceLoader.load(DaySolver.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.groupingBy(daySolver -> daySolver.getClass().getAnnotation(Day.class).year()));

        if (args.length == 1) {
            var year = Integer.parseInt(args[0]);
            analyzeYear(year, solutionsPerYear.get(year));
        } else {
            solutionsPerYear.keySet()
                    .stream()
                    .sorted()
                    .forEach(year -> analyzeYear(year, solutionsPerYear.get(year)));
        }
    }

    private static void analyzeYear(int year, List<DaySolver> days) {
        LOGGER.info("");
        LOGGER.info("-".repeat(80));
        LOGGER.info("                              Advent of Code {}", year);
        LOGGER.info("-".repeat(80));


        var solutionOutput = new StringBuilder("| %-5s | %-4s | %-35s | %-7s | %-7s |\n".formatted("Year", "Day", "Name", "Part 1", "Part 2"))
                .append("|-------|------|-------------------------------------|---------|---------|\n");
        days.stream()
                .sorted(Comparator.comparingInt(a -> a.getClass().getAnnotation(Day.class).day()))
                .forEach(daySolver -> {
                    var day = daySolver.getClass().getAnnotation(Day.class).day();

                    solutionOutput.append(
                            LINE_TEMPLATE.formatted(
                                    year,
                                    day,
                                    daySolver.getClass().getAnnotation(Day.class).name(),
                                    measure(daySolver::part1),
                                    measure(daySolver::part2)));
                });

        LOGGER.info("Year {} performance statistics:\n\n{}", year, solutionOutput);
        LOGGER.info("-".repeat(80));
    }

    private static long measure(Runnable runnable) {
        var start = Instant.now();
        runnable.run();
        return Duration.between(start, Instant.now()).toMillis();
    }
}
