package com.github.gjong.advent.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Validator {

    private final Logger logger;
    private final Properties answers;
    private final int day;

    public Validator(int year, int day) {
        this.day = day;
        answers = new Properties();
        logger = LoggerFactory.getLogger(getClass());
        try {
            answers.load(getClass().getResourceAsStream("/solutions/%d/day_%02d.properties".formatted(year, day)));
        } catch (Exception e) {
            logger.trace("Could not load answers for day %d.".formatted(day));
        }
    }

    public void part1(long answer) {
        validate("part1", answer);
    }

    public void part2(long answer) {
        validate("part2", answer);
    }

    protected boolean validate(String key, long answer) {
        if (!answers.containsKey(key)) {
            logger.warn("Solution for day {} and {} is: {}.", day, key, answer);
            return false;
        }

        var expected = Long.parseLong(answers.getProperty(key));
        if (expected != answer) {
            logger.error("Failure for day {} and {}. Expected {}, but got {}.", day, key, expected, answer);
        }

        return expected == answer;
    }
}
