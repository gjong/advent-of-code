package com.github.gjong.advent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CliParser {

    private final Logger logger = LoggerFactory.getLogger("cli");

    private boolean shouldRun = true;
    private boolean benchmarkMode = false;
    private int benchMarkRuns = 5;
    private int runYear = -1;

    CliParser(String[] args) {
        for (var idx = 0; idx < args.length; ++idx) {
            switch (args[idx]) {
                case "--help":
                    logger.info("Execute using the following options:");
                    logger.info("\t-y <year>\t\tExecute only the provided year.");
                    logger.info("\t-b \t\t\t\tRun in benchmark mode.");
                    logger.info("\t-r \t\t\t\tNumber of runs in benchmark mode, default 5.");
                    logger.info("\n\nSupported years: {}", SolutionProvider.instance().listYears());
                    shouldRun = false;
                    break;
                case "-b":
                    benchmarkMode = true;
                    break;
                case "-r":
                    benchMarkRuns = Integer.parseInt(args[++idx]);
                    break;
                case "-y":
                    runYear = Integer.parseInt(args[++idx]);
                    break;
            }
        }
    }

    public boolean shouldRun() {
        return shouldRun;
    }

    public int year() {
        return runYear;
    }

    public boolean benchmarkMode() {
        return benchmarkMode;
    }

    public int benchMarkRuns() {
        return benchMarkRuns;
    }

}
