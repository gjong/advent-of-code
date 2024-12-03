package com.github.gjong.advent.years.y2024;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.regex.Pattern;

@Day(year = 2024, day = 3, name = "Mull it over")
public class Day03 implements DaySolver {

    private final InputLoader inputLoader;
    private final Validator validator;

    public Day03(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void part1() {
        var multiplication = inputLoader.string();

        var sum = Pattern.compile("mul\\(([1-9][0-9]{0,2}),([1-9][0-9]{0,2})\\)")
                .matcher(multiplication)
                .results()
                .mapToInt(m -> Integer.parseInt(m.group(1)) * Integer.parseInt(m.group(2)))
                .sum();

        validator.part1(sum);
    }

    @Override
    public void part2() {
        var multiplication = inputLoader.string();
        var enabled = true;
        var offset = 0;

        var answer = 0;
        while (offset < multiplication.length()) {
            if (multiplication.startsWith("do()", offset)) {
                enabled = true;
                offset += 4;
            } else if (multiplication.startsWith("don't()", offset)) {
                enabled = false;
                offset += 7;
            } else if (enabled && multiplication.startsWith("mul(", offset)) {
                offset += 4;
                var closing = multiplication.indexOf(')', offset);
                if (closing == -1 || closing - offset > 7) {
                    // invalid not max 3 numbers and on ,
                    continue;
                }

                var separator = multiplication.indexOf(',', offset);
                if (separator == -1 || separator > closing) {
                    // invalid no , inside mul function
                    continue;
                }

                var left = multiplication.substring(offset, separator);
                var right = multiplication.substring(separator + 1, closing);

                answer += Integer.parseInt(left) * Integer.parseInt(right);
                offset = closing;
            } else {
                offset++;
            }
        }

        validator.part2(answer);
    }
}
