package com.github.gjong.advent.years.y2024;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Day(year = 2024, day = 1, name = "Historian Hysteria")
public class Day01 implements DaySolver {

    private final Validator validator;

    private final List<Integer> leftList;
    private final List<Integer> rightList;

    public Day01(Validator validator, InputLoader inputLoader) {
        this.validator = validator;

        leftList = new ArrayList<>();
        rightList = new ArrayList<>();
        inputLoader.splitOnNewLine()
                .forEach(line -> {
                    String[] split = line.split(" +");
                    leftList.add(Integer.parseInt(split[0]));
                    rightList.add(Integer.parseInt(split[1]));
                });

        Collections.sort(leftList);
        Collections.sort(rightList);
    }

    @Override
    public void part1() {
        int sum = 0;
        for (var i = 0; i < leftList.size(); i++) {
            var left = leftList.get(i);
            var right = rightList.get(i);

            sum += left > right ? left - right : right - left;
        }

        validator.part1(sum);
    }

    @Override
    public void part2() {
        int similarity = 0;

        int lastLeft = -1;
        int lastCount = 0;
        var countedRight = rightList.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        for (int left : leftList) {
            if (lastLeft != left) {
                lastLeft = left;
                var rightCount = countedRight.get(left);
                lastCount = rightCount != null ? rightCount.intValue() : 0;
            }

            similarity += lastCount * lastLeft;
        }

        validator.part2(similarity);
    }
}
