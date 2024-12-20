package com.github.gjong.advent.reporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MarkdownReporter implements ReportWriter {
    private static final String LINE_TEMPLATE = "| % 5d |  %02d  | %-35s | %-7s | %-7s | %-7s | %-35s |\n";

    private final Logger log = LoggerFactory.getLogger("AdventOfCode");

    @Override
    public void writeResults(BenchmarkSuite suite) {
        var yearMessage = new StringBuilder();

        addHeader(yearMessage, suite.getYear());
        addResultTable(yearMessage, suite);

        log.info("Benchmark results:\n{}", yearMessage);
    }

    private void addHeader(StringBuilder sb, int year) {
        addSeparator(sb);
        sb.append(" ".repeat(30)).append("Advent of Code ").append(year);
        addSeparator(sb);
    }

    private void addResultTable(StringBuilder sb, BenchmarkSuite suite) {
        sb.append("| %-5s | %-4s | %-35s | %-7s | %-7s | %-7s | %-35s |\n".formatted("Year", "Day", "Name", "Parsing", "Part 1", "Part 2", "Assignment"));
        for (var dayResult : suite.getResults()) {
            sb.append(LINE_TEMPLATE.formatted(
                    suite.getYear(),
                    dayResult.day(),
                    "[%s](%s)".formatted(
                            dayResult.name(),
                            dayResult.codeUri()),
                    dayResult.prepareTime(),
                    dayResult.part1Time(),
                    dayResult.part2Time(),
                    "[instructions](%s)".formatted(dayResult.instructionUri())
            ));
        }
    }

    private void addSeparator(StringBuilder sb) {
        sb.append(System.lineSeparator()).append("-".repeat(80)).append(System.lineSeparator());
    }
}
