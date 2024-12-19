package com.github.gjong.advent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CliParser {

    private final Logger logger = LoggerFactory.getLogger("cli");

    private boolean shouldRun = true;
    private boolean benchmarkMode = false;
    private boolean debugLogging = false;
    private int benchMarkRuns = 5;
    private int runYear = -1;
    private int runDay = -1;

    CliParser(String[] args) {
        for (var idx = 0; idx < args.length; ++idx) {
            switch (args[idx]) {
                case "--help":
                    logger.info("Execute using the following options:");
                    logger.info("\t-y <year>\t\tExecute only the provided year.");
                    logger.info("\t-d <day>\t\tExecute only the provided day, requires year.");
                    logger.info("\t-b \t\t\t\tRun in benchmark mode.");
                    logger.info("\t-r \t\t\t\tNumber of runs in benchmark mode, default 5.");
                    logger.info("\t-x \t\t\t\tEnable debug logging.");
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
                case "-d":
                    runDay = Integer.parseInt(args[++idx]);
                    break;
                case "-x":
                    debugLogging = true;
                    break;
            }
        }
    }

    public boolean shouldRun() {
        return shouldRun;
    }

    public boolean debugLogging() {
        return debugLogging;
    }

    public int year() {
        return runYear;
    }

    public int day() {
        return runDay;
    }

    public boolean benchmarkMode() {
        return benchmarkMode;
    }

    public int benchMarkRuns() {
        return benchMarkRuns;
    }

}
