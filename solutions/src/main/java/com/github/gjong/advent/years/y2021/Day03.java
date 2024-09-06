package com.github.gjong.advent.years.y2021;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

@Day(day = 3, year = 2021, name = "Binary Diagnostic")
public class Day03 implements DaySolver {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final InputLoader inputLoader;
    private final Validator validator;

    public Day03(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void part1() {
        var gammaRate = 0L;
        var epsilonRate = 0L;
        var bitMatrix = inputLoader.charGrid().transpose();

        for (var bitPos = bitMatrix.rows() - 1; bitPos >= 0; bitPos--) {
            var numberOnes = countOnes(bitMatrix.row(bitPos));
            var pusher = bitMatrix.rows() - 1 - bitPos;

            if (numberOnes > (bitMatrix.cols() - numberOnes)) {
                gammaRate += (1L << pusher);
            } else {
                epsilonRate += (1L << pusher);
            }
        }

        validator.part1(gammaRate * epsilonRate);
    }

    @Override
    public void part2() {
        var oxygenCandidates = computeBitString(true);
        var co2Candidates = computeBitString(false);

        validator.part2((long) oxygenCandidates * co2Candidates);
    }

    private int computeBitString(boolean mostCounted) {
        logger.debug("Computing the bit for most counted bit {} in position.", mostCounted);
        var candidates = inputLoader.splitOnNewLine().toList();
        var amountOfBits = candidates.getFirst().length();

        for (var counter = 0; counter < amountOfBits && candidates.size() > 1; counter++) {
            candidates = removeIrrelevantBits(mostCounted, candidates, counter);
        }

        return parseInt(candidates.getFirst(), 2);
    }

    private long countOnes(char[] bits) {
        var counted = 0;
        for (var bit : bits) {
            if (bit == '1') {
                counted++;
            }
        }
        return counted;
    }

    private List<String> removeIrrelevantBits(boolean mostCounted, List<String> candidates, int index) {
        var counts = candidates.stream()
                .map(bitString -> bitString.charAt(index))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        var lessOnesThanZero = counts.get('1') < counts.get('0');
        char filterForBit = lessOnesThanZero
                ? (mostCounted ? '1' : '0')
                : (mostCounted ? '0' : '1');

        return candidates.stream()
                .filter(candidate -> candidate.charAt(index) != filterForBit)
                .toList();
    }
}
