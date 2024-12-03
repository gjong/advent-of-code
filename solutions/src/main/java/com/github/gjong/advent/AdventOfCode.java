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

    private static final String LINE_TEMPLATE = "| % 5d |  %02d  | %-35s | %-5s | %-5s | %-5s | %-35s |\n";

    public static void main(String[] args) {
        if (args.length == 1) {
            var year = Integer.parseInt(args[0]);
            analyzeYear(year, SolutionProvider.instance().provide(year));
        } else {
            var solutionsPerYear = SolutionProvider.instance().provideAll();
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

        var solutionOutput = new StringBuilder("| %-5s | %-4s | %-35s | %-7s | %-7s | %-7s | %-35s |\n".formatted("Year", "Day", "Name", "Parsing", "Part 1", "Part 2", "Assignment"))
                .append("|-------|------|-------------------------------------|---------|---------|---------|----------------------------|\n");
        days.stream()
                .sorted(Comparator.comparingInt(a -> a.getClass().getAnnotation(Day.class).day()))
                .forEach(daySolver -> {
                    var day = daySolver.getClass().getAnnotation(Day.class).day();

                    solutionOutput.append(
                            LINE_TEMPLATE.formatted(
                                    year,
                                    day,
                                    "[%s](solutions/src/main/java/com/github/gjong/advent/years/y%d/%s.java)".formatted(
                                            daySolver.getClass().getAnnotation(Day.class).name(),
                                            year,
                                            daySolver.getClass().getSimpleName()),
                                    measure(daySolver::readInput),
                                    measure(daySolver::part1),
                                    measure(daySolver::part2),
                                    "[instructions](https://adventofcode.com/%d/day/%d)".formatted(year, day)));
                });

        LOGGER.info("Year {} performance statistics:\n\n{}", year, solutionOutput);
        LOGGER.info("-".repeat(80));
    }

    private static String measure(Runnable runnable) {
        var start = Instant.now();
        runnable.run();
        var duration = Duration.between(start, Instant.now()).toNanos() / 1000;
        if (duration > 1000) {
            return duration / 1000 + "ms";
        }

        return duration + "μs";
    }
}
