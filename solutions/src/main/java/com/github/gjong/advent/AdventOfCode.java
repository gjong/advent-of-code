package com.github.gjong.advent;

import ch.qos.logback.classic.Level;
import com.github.gjong.advent.reporter.BenchmarkSuite;
import com.github.gjong.advent.reporter.ReportWriterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AdventOfCode {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger("AdventOfCode");

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
        var reportWriter = ReportWriterFactory.create(arguments.getReportFormat());
        LOGGER.info("Running with {} benchmark runs.", numberOfRuns);

        for (var year : runYears) {
            List<DaySolver> solverList;
            if (arguments.day() > -1) {
                solverList = List.of(SolutionProvider.instance().provide(year, arguments.day()));
            } else {
                solverList = SolutionProvider.instance().provide(year);
            }

            var suite = new BenchmarkSuite(numberOfRuns, year, solverList);
            suite.execute();
            reportWriter.writeResults(suite);
        }
    }

    private static int determineNumberOfRuns(CliParser parser) {
        return parser.benchmarkMode() ?
                parser.benchMarkRuns()
                : 1;
    }
}
