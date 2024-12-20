package com.github.gjong.advent.reporter;

import com.jongsoft.lang.Control;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class JsonReporter implements ReportWriter {

    private final Path reportPath;

    public JsonReporter() {
        reportPath = Path.of(System.getProperty("user.dir"), "build", "report");
    }

    @Override
    public void writeResults(BenchmarkSuite suite) {
        ensureOutputDirectoryExists();

        var jsonReport = """
                {
                    "year": %d,
                    "days": [
                        %s
                    ]
                }""".formatted(suite.getYear(), buildResults(suite));

        var outputFile = reportPath.resolve(suite.getYear() + "-report.json");
        Control.Try(() -> Files.writeString(outputFile, jsonReport));
    }

    private String buildResults(BenchmarkSuite suite) {
        return suite.getResults()
                .stream()
                .map(this::convertResult)
                .collect(Collectors.joining(", \n"));
    }

    private String convertResult(BenchmarkResult result) {
        return """
                {
                    "day": %d,
                    "name": "%s",
                    "instructionUri": "%s",
                    "sourceUri": "%s",
                    "part1": {
                        "total": %d,
                        "runs": %d
                    },
                    "part2": {
                        "total": %d,
                        "runs": %d
                    },
                    "preparation": {
                        "total": %d,
                        "runs": %d
                    }
                }""".formatted(
                result.day(),
                result.name(),
                result.instructionUri(),
                result.codeUri(),
                result.part1().totalTime(),
                result.part1().runs(),
                result.part2().totalTime(),
                result.part2().runs(),
                result.preparation().totalTime(),
                result.preparation().runs());
    }

    private void ensureOutputDirectoryExists() {
        if (!Files.exists(reportPath)) {
            Control.Try(() -> Files.createDirectories(reportPath));
        }
    }
}
