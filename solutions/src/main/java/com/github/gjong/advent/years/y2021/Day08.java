package com.github.gjong.advent.years.y2021;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.jongsoft.lang.API;
import com.jongsoft.lang.Collections;
import com.jongsoft.lang.collection.Sequence;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

@Day(day = 8, year = 2021, name = "")
public class Day08 implements DaySolver {
    private final InputLoader inputLoader = DayLoader.inputDay8();
    private final Validator validator = DayLoader.validatorDay8();

    @Override
    public void part1() {
        var answer = inputLoader.splitOnNewLine()
                .map(line -> line.split("\\|"))
                .map(splitted -> API.Tuple(
                        new Decoder(splitToNumbers(splitted[0]).toJava()),
                        splitToNumbers(splitted[1])))
                .mapToDouble(tuple -> tuple
                        .getSecond()
                        .map(tuple.getFirst()::countMatch1or4or7or8)
                        .sum()
                        .get())
                .sum();

        validator.part1(Double.valueOf(answer).intValue());
    }

    @Override
    public void part2() {
        var sum = 0;
        for (var line : inputLoader.splitOnNewLine().toList()) {
            var split = line.split("\\|");

            var decoder = new Decoder(splitToNumbers(split[0]).toJava());
            decoder.alignment();
            sum += decoder.decode(splitToNumbers(split[1]).toJava());
        }

        validator.part2(sum);
    }

    private Sequence<CodedEntry> splitToNumbers(String token) {
        return Collections.List(token.trim().split(" "))
                .map(String::trim)
                .map(CodedEntry::new);
    }

    class Decoder {
        private final List<CodedEntry> codedEntries;
        private final CodedEntry[] decoded;

        public Decoder(List<CodedEntry> codedEntries) {
            this.codedEntries = codedEntries;
            this.decoded = new CodedEntry[10];
            alignment();
        }

        public final void alignment() {
            // 2 and 7 are the only having unique counts
            decoded[1] = findMatchingSegment(2).getFirst();
            decoded[4] = findMatchingSegment(4).getFirst();
            decoded[7] = findMatchingSegment(3).getFirst();
            decoded[8] = findMatchingSegment(7).getFirst();

            resolve0and6and9();
            resolve2and3and5();
        }

        public int countMatch1or4or7or8(CodedEntry codedEntry) {
            var countedMatches = 0;
            var idxOfInterest = new int[] {1, 4, 7, 8};
            for (var idx : idxOfInterest) {
                if (codedEntry.matches(decoded[idx])) {
                    countedMatches++;
                }
            }

            return countedMatches;
        }

        public int decode(List<CodedEntry> codedEntries) {
            var stringRepresentation = codedEntries.stream()
                    .map(codedEntry -> {
                        for (var idx = 0; idx < decoded.length; idx++) {
                            if (decoded[idx].matches(codedEntry)) {
                                return idx;
                            }
                        }

                        throw new IllegalStateException("Cannot locate codedEntry " + codedEntries);
                    })
                    .map(String::valueOf)
                    .collect(Collectors.joining());

            return parseInt(stringRepresentation);
        }

        private void resolve0and6and9() {
            for (var alignOn : findMatchingSegment(6)) {
                if (alignOn.fullOverlap(decoded[4])) {
                    decoded[9] = alignOn;
                } else if (alignOn.fullOverlap(decoded[1])) {
                    decoded[0] = alignOn;
                } else {
                    decoded[6] = alignOn;
                }
            }
        }

        private void resolve2and3and5() {
            for (var alignOn : findMatchingSegment(5)) {
                if (alignOn.fullOverlap(decoded[7])) {
                    decoded[3] = alignOn;
                } else if (alignOn.partialOverlap(decoded[6]) == 1) {
                    decoded[5] = alignOn;
                } else {
                    // the overlap between 2 and 6 differs by 2 segments not 1
                    decoded[2] = alignOn;
                }
            }
        }

        private List<CodedEntry> findMatchingSegment(int count) {
            return codedEntries.stream()
                    .filter(entry -> entry.matchesSegments(count))
                    .toList();
        }

    }

    record CodedEntry(String codedEntry) {
        public boolean matchesSegments(int count) {
            return codedEntry.length() == count;
        }

        public boolean matches(CodedEntry other) {
            return fullOverlap(other) && other.fullOverlap(this);
        }

        public boolean fullOverlap(CodedEntry other) {
            return other.codedEntry
                    .chars()
                    .allMatch(n -> codedEntry.contains(Character.toString(n)));
        }

        public long partialOverlap(CodedEntry other) {
            return other.codedEntry
                    .chars()
                    .filter(n -> !codedEntry.contains(Character.toString(n)))
                    .count();
        }
    }
}