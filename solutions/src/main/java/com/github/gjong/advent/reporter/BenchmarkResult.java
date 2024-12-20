package com.github.gjong.advent.reporter;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;

public record BenchmarkResult(DaySolver daySolver, Measurement part1, Measurement part2, Measurement preparation) {
    public record Measurement(long totalTime, int runs) {
        int computeAverage() {
            return (int) (totalTime / runs);
        }
    }

    public String instructionUri() {
        var day = daySolver.getClass().getAnnotation(Day.class);
        return "https://adventofcode.com/%d/day/%d".formatted(day.year(), day.day());
    }

    public String codeUri() {
        var srcLocation = daySolver.getClass().getName().replace('.', '/');
        return "solutions/src/main/java/%s.java"
                .formatted(srcLocation);
    }

    public int day() {
        return daySolver.getClass().getAnnotation(Day.class).day();
    }

    public String name() {
        return daySolver.getClass().getAnnotation(Day.class).name();
    }

    public String prepareTime() {
        return prettify(preparation.computeAverage());
    }

    public String part1Time() {
        return prettify(part1.computeAverage());
    }

    public String part2Time() {
        return prettify(part2.computeAverage());
    }

    private String prettify(int nanos) {
        if (nanos == 0) return "-";
        if (nanos > 1200) return (nanos / 1000) + "ms";
        return nanos + "μs";
    }
}
