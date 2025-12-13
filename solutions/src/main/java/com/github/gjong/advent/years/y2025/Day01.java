package com.github.gjong.advent.years.y2025;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.List;

@Day(year = 2025, day = 1, name = "Secret Entrance")
public class Day01 implements DaySolver {

    record Turn(char direction, int count) {
        public static Turn parse(String line) {
            return new Turn(line.charAt(0), Integer.parseInt(line.substring(1)));
        }

        public long computeAddition() {
            return direction == 'R' ? count : -count;
        }

        public long numberOfFullIterations() {
            return count / 100;
        }

        public long numberOfPartialIterations() {
            return direction == 'R' ? count % 100 : -(count % 100);
        }
    }

    private final Validator validator;
    private final InputLoader inputLoader;

    private List<Turn> turns;

    public Day01(Validator validator, InputLoader inputLoader) {
        this.validator = validator;
        this.inputLoader = inputLoader;
    }

    @Override
    public void part1() {
        long numberOfZero = 0;
        long startPos = 50L;
        for (var turn : turns) {
            long turning = turn.computeAddition();
            startPos += turning;
            startPos %= 100;

            if (startPos == 0) {
                numberOfZero++;
            }
        }

        validator.part1(numberOfZero);
    }

    /**
     * You remember from the training seminar that "method 0x434C49434B" means you're actually supposed to count the
     * number of times any click causes the dial to point at 0, regardless of whether it happens during a
     * rotation or at the end of one.
     *
     * Be careful: if the dial were pointing at 50, a single rotation like R1000 would cause the dial
     * to point at 0 ten times before returning back to 50!
     */
    @Override
    public void part2() {
        long numberPassing = 0;
        long startPos = 50L;
        for (var turn : turns) {
            long partialIterations = turn.numberOfPartialIterations();

            numberPassing += turn.numberOfFullIterations();

            long updatedPos = startPos + partialIterations;
            if (startPos != 0 && (updatedPos <= 0 || updatedPos >= 100)) {
                numberPassing++;
            }
            startPos = computeNewPosition(updatedPos);
        }

        validator.part2(numberPassing);
    }

    private long computeNewPosition(long unsafePos) {
        if (unsafePos < 0) {
            return 100 + unsafePos;
        } else if (unsafePos > 99) {
            return (unsafePos - 100);
        }
        return unsafePos;
    }

    @Override
    public void readInput() {
        turns = inputLoader.splitOnNewLine()
                .map(Turn::parse)
                .toList();
    }
}
