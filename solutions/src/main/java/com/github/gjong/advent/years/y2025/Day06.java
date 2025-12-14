package com.github.gjong.advent.years.y2025;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.ArrayList;
import java.util.List;

@Day(year = 2025, day = 6, name = "Trash Compactor")
public class Day06 implements DaySolver {

    private final InputLoader inputLoader;
    private final Validator validator;
    private List<Equation> equations;

    public Day06(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void part1() {
        long summed = equations.stream()
                .mapToLong(Equation::compute)
                .sum();

        validator.part1(summed);
    }

    @Override
    public void part2() {
        long summed = equations.stream()
                .map(Equation::correctForPart2)
                .mapToLong(Equation::compute)
                .sum();

        validator.part2(summed);
    }

    @Override
    public void readInput() {
        equations = new ArrayList<>();

        List<String> lines = inputLoader.splitOnNewLine().toList();

        int startColumn = 0;
        String operatorLine = lines.getLast();
        for (int i = 1; i < operatorLine.length(); i++) {
            if (operatorLine.charAt(i) != ' ') {
                // start of column
                int numberOfChars = i - startColumn;
                equations.add(new Equation(numberOfChars, false));
                startColumn = i;
            }
        }
        equations.add(new Equation(-1, true));

        for (String line : lines) {
            for (Equation equation : equations) {
                if (equation.last) {
                    equation.numbers.add(line);
                    continue;
                }
                String number = line.substring(0, equation.numberOfChars - 1);
                equation.numbers.add(number);
                line = line.substring(equation.numberOfChars);
            }
        }

        equations.forEach(Equation::fixOperation);
    }

    private class Equation {
        private final List<String> numbers = new ArrayList<>();
        private String operator;
        private final int numberOfChars;
        private final boolean last;

        Equation(int numberOfChars, boolean last) {
            this.numberOfChars = numberOfChars;
            this.last = last;
        }

        void fixOperation() {
            operator = numbers.getLast();
            numbers.removeLast();
        }

        Equation correctForPart2() {
            // column-based numbers
            int largestNumber = numbers.stream().mapToInt(String::length).max().orElseThrow();

            List<String> newNumbers = new ArrayList<>();
            for (int i = 0; i < largestNumber; i++) {
                String columnNumber = "";
                for (String number : numbers) {
                    if (i < number.length()) {
                        columnNumber += number.charAt(i);
                    }
                }
                newNumbers.add(columnNumber);
            }
            numbers.clear();
            numbers.addAll(newNumbers);

            return this;
        }

        long compute() {
            long result = 0;
            for (String number : numbers) {
                switch (operator.trim()) {
                    case "+" -> result += Long.parseLong(number.trim());
                    case "*" -> result = Math.max(result, 1) * Long.parseLong(number.trim());
                    case "-" -> result -= Long.parseLong(number.trim());
                    default -> throw new IllegalArgumentException("Unknown operator: " + operator);
                }
            }
            return result;
        }
    }
}
