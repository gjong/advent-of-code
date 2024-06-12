package com.github.gjong.advent.years.y2021;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.List;
import java.util.Optional;
import java.util.Stack;

@Day(day = 10, year = 2021, name = "Syntax Scoring")
public class Day10 implements DaySolver {
    private final InputLoader inputLoader = DayLoader.inputDay10();
    private final Validator validator = DayLoader.validatorDay10();

    @Override
    public void part1() {
        var answer = inputLoader.splitOnNewLine()
                .map(LineParser::new)
                .filter(LineParser::isInValid)
                .mapToDouble(parser -> parser.getInvalidOn().mismatchValue)
                .sum();

        validator.part1((long) answer);
    }

    @Override
    public void part2() {
        var answer = inputLoader.splitOnNewLine()
                .map(LineParser::new)
                .filter(LineParser::isValid)
                .map(parser -> parser
                        .computeAutocomplete()
                        .stream()
                        .map(bracket -> (long) bracket.matchValue)
                        .reduce(0L, (accumulator, value) -> accumulator * 5 + value))
                .sorted()
                .toList();

        validator.part2(answer.get(answer.size() / 2));
    }

    static class LineParser {

        private final Stack<Brackets> parsingStack;
        private Brackets invalidOn;

        public LineParser(String line) {
            parsingStack = new Stack<>();
            for (char instruction : line.toCharArray()) {
                var matchingOpen = Brackets.forOpenChar(instruction);
                if (matchingOpen.isPresent()) {
                    parsingStack.add(matchingOpen.get());
                } else {
                    var matchingClosing = Brackets.forClosingChar(instruction);
                    if (matchingClosing.isPresent() && matchingClosing.get() != parsingStack.pop()) {
                        invalidOn = matchingClosing.get();
                        break;
                    }
                }
            }
        }

        public boolean isValid() {
            return invalidOn == null;
        }

        public boolean isInValid() {
            return invalidOn != null;
        }

        public Brackets getInvalidOn() {
            return invalidOn;
        }

        public List<Brackets> computeAutocomplete() {
            return parsingStack.reversed();
        }
    }

    enum Brackets {
        curly('{', '}', 3,1197),
        rounded('(', ')', 1, 3),
        square('[', ']', 2, 57),
        angle('<', '>', 4, 25137);

        final char openChar;
        final char closingChar;
        final int matchValue;
        final int mismatchValue;
        Brackets(char openChar, char closingChar, int matchValue, int mismatchValue) {
            this.openChar = openChar;
            this.closingChar = closingChar;
            this.matchValue = matchValue;
            this.mismatchValue = mismatchValue;
        }

        static Optional<Brackets> forOpenChar(char openChar) {
            for (var bracket : Brackets.values()) {
                if (bracket.openChar == openChar) {
                    return Optional.of(bracket);
                }
            }
            return Optional.empty();
        }

        static Optional<Brackets> forClosingChar(char closingChar) {
            for (var bracket : Brackets.values()) {
                if (bracket.closingChar == closingChar) {
                    return Optional.of(bracket);
                }
            }
            return Optional.empty();
        }
    }
}
