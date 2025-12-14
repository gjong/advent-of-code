package com.github.gjong.advent.years.y2025;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@Day(year = 2025, day = 5, name = "Cafeteria")
public class Day05 implements DaySolver {

    private final InputLoader inputLoader;
    private final Validator validator;

    private List<IdRange> ranges ;
    private List<Id> ingredientIds;

    public Day05(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void part1() {
        long nonSpoiled = ingredientIds.stream()
                .filter(id -> ranges.stream().anyMatch(range -> range.contains(id)))
                .count();

        validator.part1(nonSpoiled);
    }

    @Override
    public void part2() {
        BigInteger nonSpoiled = ranges.stream()
                .map(range -> BigInteger.valueOf(range.freshIds()))
                .reduce(BigInteger::add)
                .get();

        validator.part2(nonSpoiled);
    }

    @Override
    public void readInput() {
        ranges = new ArrayList<>();
        ingredientIds = new ArrayList<>();

        boolean rangesDone = false;
        Scanner scanner = inputLoader.scanner();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                rangesDone = true;
                continue;
            }

            if (!rangesDone) {
                String[] bounds = line.split("-");
                IdRange idRange = new IdRange(Long.parseLong(bounds[0]), Long.parseLong(bounds[1]));
                ranges.add(idRange);
            } else {
                ingredientIds.add(new Id(Long.parseLong(line)));
            }
        }

        mergeOverlappingRanges();
    }

    private void mergeOverlappingRanges() {
        // loop over the ranges to merge any range with overlap, do so recursively until nothing changes

        boolean hasChanged = true;
        List<IdRange> original = ranges;
        List<IdRange> mergedRanges = new ArrayList<>();
        while (hasChanged) {
            hasChanged = false;
            for (IdRange range : original) {
                Optional<IdRange> overlappingRange = original.stream()
                        .filter(r -> !Objects.equals(r, range) && r.hasOverlap(range)).findFirst();
                if (overlappingRange.isPresent()) {
                    hasChanged = true;
                    mergedRanges.add(range.merge(overlappingRange.get()));
                } else {
                    mergedRanges.add(range);
                }
            }
            original = mergedRanges.stream().distinct().toList();
            mergedRanges = new ArrayList<>();
        }

        ranges = original;
    }

    private record Id(long id) {}

    private record IdRange(long min, long max) {
        public boolean contains(Id id) {
            return id.id >= min && id.id <= max;
        }

        public long freshIds() {
            return max - min + 1;
        }

        public boolean hasOverlap(IdRange other) {
            return min <= other.max && max >= other.min;
        }

        public IdRange merge(IdRange other) {
            return new IdRange(Math.min(min, other.min), Math.max(max, other.max));
        }
    }
}
