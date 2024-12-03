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

    public void part2() {
        var multiplication = inputLoader.string();
        var offset = 0;

        var answer = 0;
        while (offset < multiplication.length()) {
            var nextDont = multiplication.indexOf("don't()", offset);
            var nextMultiply = multiplication.indexOf("mul(", offset);

            if (nextDont < nextMultiply && nextDont > 0) {
                offset = multiplication.indexOf("do()", nextDont) + 4;
            } else if (nextMultiply > 0) {
                offset = nextMultiply + 4;
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

                var numbers = multiplication.substring(offset, closing).split(",");
                answer += Integer.parseInt(numbers[0]) * Integer.parseInt(numbers[1]);
                offset = closing;
            } else {
                // no more multiplications
                break;
            }
        }

        validator.part2(answer);
    }

    public void part2_regex() {
        var multiplication = inputLoader.string();

        var c = 0;
        var inclusion = true;
        var matcher = Pattern.compile("mul\\(([1-9][0-9]{0,2}),([1-9][0-9]{0,2})\\)|(do\\(\\))|don't\\(\\)")
                .matcher(multiplication);
        while (matcher.hasMatch()) {
            if (matcher.group(0).startsWith("do(")) {
                inclusion = true;
            } else if (matcher.group(0).startsWith("don't(")) {
                inclusion = false;
            } else if (inclusion) {
                c += Integer.parseInt(matcher.group(1)) * Integer.parseInt(matcher.group(2));
            }
        }
        validator.part2(c);
    }
}
