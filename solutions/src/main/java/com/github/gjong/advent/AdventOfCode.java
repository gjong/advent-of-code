package com.github.gjong.advent;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class AdventOfCode {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AdventOfCode.class);

    private static final String LINE_TEMPLATE = "| % 5d |  %02d  | %-35s | %-5s | %-5s | %-5s | %-35s |\n";

    public static void main(String[] args) {
        var arguments = new CliParser(args);
        if (!arguments.shouldRun()) {
            System.exit(0);
        }

        var supportedYears = SolutionProvider.instance()
                .listYears();

        var runYears = List.copyOf(supportedYears);
        if (arguments.year() > -1) {
            runYears = List.of(arguments.year());
        }

        if (arguments.debugLogging()) {
            ((ch.qos.logback.classic.Logger) LoggerFactory.getILoggerFactory()
                    .getLogger("com.github.gjong.advent"))
                    .setLevel(Level.DEBUG);
        }

        var numberOfRuns = determineNumberOfRuns(arguments);
        LOGGER.info("Running with {} benchmark runs.", numberOfRuns);
        for (var year : runYears) {
            List<DaySolver> solverList;
            if (arguments.day() > -1) {
                solverList = List.of(SolutionProvider.instance().provide(year, arguments.day()));
            } else {
                solverList = SolutionProvider.instance().provide(year);
            }

            var suite = new BenchmarkSuite(numberOfRuns, year, solverList);
            var results = benchmark(suite);

            LOGGER.info("");
            LOGGER.info("-".repeat(80));
            LOGGER.info("                              Advent of Code {}", year);
            LOGGER.info("-".repeat(80));

            var solutionOutput = new StringBuilder("| %-5s | %-4s | %-35s | %-7s | %-7s | %-7s | %-35s |\n".formatted("Year", "Day", "Name", "Parsing", "Part 1", "Part 2", "Assignment"))
                    .append("|-------|------|-------------------------------------|---------|---------|---------|----------------------------|\n");
            results.forEach(result -> {
                solutionOutput.append(
                        LINE_TEMPLATE.formatted(
                                result.year,
                                result.day,
                                "[%s](solutions/src/main/java/com/github/gjong/advent/years/y%d/%s.java)".formatted(
                                        result.name(),
                                        result.year(),
                                        result.className()),
                                prettify(result.prepNanos),
                                prettify(result.part1Nanos),
                                prettify(result.part2Nanos),
                                "[instructions](https://adventofcode.com/%d/day/%d)".formatted(result.year(), result.day()))
                );
            });

            LOGGER.info("Year {} performance statistics:\n\n{}", year, solutionOutput);
            LOGGER.info("-".repeat(80));
        }
    }

    private static int determineNumberOfRuns(CliParser parser) {
        return parser.benchmarkMode() ?
                parser.benchMarkRuns()
                : 1;
    }

    private record BenchmarkResult(int year, int day, int part1Nanos, int part2Nanos, int prepNanos, String name, String className) {
    }

    private record BenchmarkSuite(int runs, int year, List<DaySolver> days) {
    }

    private static String prettify(int nanos) {
        if (nanos == 0) return "-";
        if (nanos > 1200) return (nanos / 1000) + "ms";
        return nanos + "μs";
    }

    private static Stream<BenchmarkResult> benchmark(BenchmarkSuite suite) {
        var results = new ArrayList<BenchmarkResult>();
        for (var solver : suite.days) {
            var day = solver.getClass().getAnnotation(Day.class);

            var part1Total = 0L;
            var part2Total = 0L;
            var totalPrepareTime = 0L;
            var part1Limit = limitedRunConfig(solver, 1);
            var part2Limit = limitedRunConfig(solver, 2);

            for (var run = 0; run < suite.runs; run++) {
                totalPrepareTime += measure(solver::readInput);
                if (run < part1Limit) part1Total += measure(solver::part1);
                if (run < part2Limit) part2Total += measure(solver::part2);
            }

            results.add(new BenchmarkResult(
                    suite.year(),
                    day.day(),
                    new BigDecimal(part1Total).divide(new BigDecimal(Math.min(suite.runs, part1Limit)), RoundingMode.CEILING).round(new MathContext(0, RoundingMode.CEILING)).intValue(),
                    new BigDecimal(part2Total).divide(new BigDecimal(Math.min(suite.runs, part2Limit)), RoundingMode.CEILING).round(new MathContext(0, RoundingMode.CEILING)).intValue(),
                    new BigDecimal(totalPrepareTime).divide(new BigDecimal(suite.runs), RoundingMode.CEILING).round(new MathContext(0, RoundingMode.CEILING)).intValue(),
                    day.name(),
                    solver.getClass().getSimpleName()));
        }
        return results.stream();
    }

    private static long measure(Runnable runnable) {
        var start = Instant.now();
        runnable.run();
        return Duration.between(start, Instant.now()).toNanos() / 1000;
    }

    private static int limitedRunConfig(DaySolver solver, int part) {
        try {
            var method = solver.getClass().getMethod("part" + part);
            return Optional.ofNullable(method.getAnnotation(LimitRuns.class))
                    .map(LimitRuns::value)
                    .orElse(Integer.MAX_VALUE);
        } catch (NoSuchMethodException nme) {
            System.out.println("Method not found: " + nme.getMessage());
            return Integer.MAX_VALUE;
        }
    }
}
