package com.github.gjong.advent.reporter;

import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.LimitRuns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BenchmarkSuite {
    private final Logger log = LoggerFactory.getLogger("AdventOfCode");
    private final int runs;
    private final int year;
    private final List<DaySolver> days;

    private final List<BenchmarkResult> dayResults = new ArrayList<>();

    public BenchmarkSuite(int runs, int year, List<DaySolver> days) {
        this.runs = runs;
        this.year = year;
        this.days = days;
    }

    public void execute() {
        dayResults.clear();
        log.info("Starting benchmark for year {}.", year);
        for (DaySolver solver : days) {
            log.info("Executing benchmark for {}.", solver.getClass().getSimpleName());
            var part1Total = 0L;
            var part2Total = 0L;
            var totalPrepareTime = 0L;
            var part1Limit = Math.min(limitedRunConfig(solver, 1), runs);
            var part2Limit = Math.min(limitedRunConfig(solver, 2), runs);

            for (var run = 0; run < runs; run++) {
                totalPrepareTime += measure(solver::readInput);
                if (run < part1Limit) part1Total += measure(solver::part1);
                if (run < part2Limit) part2Total += measure(solver::part2);
            }

            dayResults.add(new BenchmarkResult(
                    solver,
                    new BenchmarkResult.Measurement(part1Total, part1Limit),
                    new BenchmarkResult.Measurement(part2Total, part2Limit),
                    new BenchmarkResult.Measurement(totalPrepareTime, runs)));
        }
        log.info("Benchmark complete for year {}.", year);
    }

    public List<BenchmarkResult> getResults() {
        return dayResults;
    }

    public int getYear() {
        return year;
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
