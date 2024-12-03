package com.github.gjong.advent.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.Objects;
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

    public void part1(BigInteger answer) {
        validate("part1", answer);
    }

    public void part1(String answer) {
        validate("part1", answer);
    }

    public void part2(long answer) {
        validate("part2", answer);
    }

    public void part2(BigInteger answer) {
        validate("part2", answer);
    }

    public void part2(String answer) {
        validate("part2", answer);
    }

    protected <T> boolean validate(String key, T answer) {
        if (!answers.containsKey(key)) {
            logger.warn("Solution for day {} and {} is: {}.", day, key, answer);
            return false;
        }

        var rawAnswer = answers.getProperty(key);
        var matching = switch (answer) {
            case BigInteger value -> new BigInteger(rawAnswer).equals(value);
            case String value -> rawAnswer.equalsIgnoreCase(value);
            case Long value -> Long.parseLong(rawAnswer) == value;
            default -> Objects.equals(answer, rawAnswer);
        };

        if (!matching) {
            logger.error("Failure for day {} and {}. Expected {}, but got {}.", day, key, rawAnswer, answer);
        }

        return matching;
    }

    protected boolean hasAnswer(String key) {
        return answers.containsKey(key);
    }
}
