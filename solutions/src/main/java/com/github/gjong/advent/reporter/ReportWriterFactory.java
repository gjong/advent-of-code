package com.github.gjong.advent.reporter;

public class ReportWriterFactory {
    private ReportWriterFactory() {}

    public static ReportWriter create(String format) {
        return switch (format) {
            default -> new MarkdownReporter();
        };
    }
}
