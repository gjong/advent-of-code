package com.github.gjong.advent.years.y2023;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@Day(year = 2023, day = 15, name = "Lens Library")
public class Day15 implements DaySolver {
    private final InputLoader inputLoader;
    private final Validator validator;

    public Day15(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    record Lens(String lens, int focalStrength) {}

    @Override
    public void part1() {
        var answer = Stream.of(inputLoader.split(","))
                .mapToLong(this::hash)
                .sum();
        validator.part1(answer);
    }

    @Override
    public void part2() {
        var boxes = new HashMap<Long, List<Lens>>(255);
        IntStream.range(0, 256).forEach(i -> boxes.put((long) i, new ArrayList<>()));

        for (var instruction : inputLoader.split(",")) {
            if (instruction.endsWith("-")) {
                var lens = instruction.substring(0, instruction.length() - 1);
                var hash = hash(lens);
                boxes.get(hash).removeIf(l -> l.lens.equals(lens));
                continue;
            }

            var lens = new Lens(instruction.split("=")[0], Integer.parseInt(instruction.split("=")[1]));
            var box = hash(lens.lens);
            var lenses = boxes.get(box);

            // find index to replace
            var replaceIndex = -1;
            for (int i = 0; i < lenses.size(); i++) {
                if (lenses.get(i).lens.equals(lens.lens)) {
                    replaceIndex = i;
                    break;
                }
            }

            if (replaceIndex != -1) {
                lenses.remove(replaceIndex);
                lenses.add(replaceIndex, lens);
            } else {
                // add lens to end
                lenses.add(lens);
            }
        }

        var answer = boxes.entrySet()
                .stream()
                .filter(e -> !e.getValue().isEmpty())
                .flatMapToLong(e -> computeStrength(e.getKey(), e.getValue()))
                .sum();
        validator.part2(answer);
    }

    private LongStream computeStrength(long box, List<Lens> lenses) {
        var strengths = new ArrayList<Long>();
        for (int i = 0; i < lenses.size(); i++) {
            var lens = lenses.get(i);
            strengths.add((box + 1) * (i + 1) * lens.focalStrength);
        }
        return strengths.stream().mapToLong(Long::longValue);
    }

    private long hash(String input) {
        long hash = 0;
        for (char c : input.toCharArray()) {
            hash += c;
            hash *= 17;
            hash %= 256;
        }
        return hash;
    }
}
