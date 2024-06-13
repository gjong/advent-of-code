package com.github.gjong.advent.years.y2021;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

@Day(day = 18, year = 2021, name = "Snailfish")
public class Day18 implements DaySolver {
    private final InputLoader inputLoader = DayLoader.inputDay18();
    private final Validator validator = DayLoader.validatorDay18();

    @Override
    public void part1() {
        SnailNumber snailNumber = null;
        for (var line : inputLoader.splitOnNewLine().toList()) {
            if (snailNumber == null) {
                snailNumber = SnailNumber.generateSnailNumber(line);
            } else {
                snailNumber = snailNumber.add(SnailNumber.generateSnailNumber(line));
            }

            snailNumber.reduce();
        }

        validator.part1(snailNumber.magnitude());
    }

    @Override
    public void part2() {
        var snailNumbers = inputLoader.splitOnNewLine().toList();
        var maxMagnitude = Long.MIN_VALUE;
        for (var index = 0; index < snailNumbers.size(); index++) {
            for (var computeIndex = 0; computeIndex < snailNumbers.size(); computeIndex++) {
                if (computeIndex == index)
                    continue;
                var addedSnail = SnailNumber.generateSnailNumber(snailNumbers.get(index))
                        .add(SnailNumber.generateSnailNumber(snailNumbers.get(computeIndex)));

                addedSnail.reduce();
                var magnitude = addedSnail.magnitude();
                if (magnitude > maxMagnitude) {
                    maxMagnitude = magnitude;
                }
            }
        }

        validator.part2(maxMagnitude);
    }

    interface SnailNumber {

        default int size() {
            return 0;
        }

        SnailNumber getParent();

        default void setParent(SnailNumberPair parent) {}

        default int value() {
            return 0;
        }

        default long magnitude() {
            return value();
        }

        default SnailNumber add(SnailNumber fishNumber) {
            var newPair = new SnailNumberPair();
            newPair.setPair(this, fishNumber);
            setParent(newPair);
            fishNumber.setParent(newPair);
            return newPair;
        }

        default boolean split() {
            return false;
        }

        default boolean explode(int depth) {
            return false;
        }

        default void reduce() {
            boolean isComplete = false;
            while (!isComplete) {
                if (explode(0)) {
                    continue;
                }

                if (split()) {
                    continue;
                }

                isComplete = true;
            }
        }

        static SnailNumber of(int value) {
            return new SnailNumberPrimitive(value);
        }

        static SnailNumber of(SnailNumberPair parent, double value) {
            var newValue = new SnailNumberPrimitive((int) value);
            newValue.setParent(parent);
            return newValue;
        }

        static SnailNumber zero(SnailNumberPair parent) {
            var newFishValue = new SnailNumberPrimitive(0);
            newFishValue.setParent(parent);
            return newFishValue;
        }

        static SnailNumber generateSnailNumber(String line) {
            List<Character> characters = new ArrayList<>(line.length());
            for (char character : line.toCharArray())
                characters.add(character);

            return generateSnailNumber(characters);
        }

        static SnailNumber generateSnailNumber(List<Character> line) {
            while (!line.isEmpty()) {
                char nextCharacter = line.removeFirst();

                if (nextCharacter == '[') {
                    var fishPair = new SnailNumberPair();
                    var leftValue = generateSnailNumber(line);
                    leftValue.setParent(fishPair);

                    line.removeFirst(); // remove ,
                    var rightValue = generateSnailNumber(line);
                    rightValue.setParent(fishPair);
                    line.removeFirst(); // remove ]

                    fishPair.setPair(leftValue, rightValue);
                    return fishPair;
                } else {
                    return new SnailNumberPrimitive(parseInt(String.valueOf(nextCharacter)));
                }
            }

            return null;
        }
    }

    static class SnailNumberPair implements SnailNumber {
        private SnailNumberPair parent;
        private final SnailNumber[] values = new SnailNumber[2];

        @Override
        public SnailNumberPair getParent() {
            return parent;
        }

        public void setParent(SnailNumberPair parent) {
            this.parent = parent;
        }

        public void setPair(SnailNumber left, SnailNumber right) {
            values[0] = left;
            values[1] = right;
        }

        public SnailNumber left() {
            return values[0];
        }

        public SnailNumber right() {
            return values[1];
        }

        @Override
        public int size() {
            return 2;
        }

        @Override
        public boolean split() {
            return values[0].split() || values[1].split();
        }

        void combineOnIndex(int index, int value) {
            var step = 0;
            var locateIndex = (index + 1) % 2;
            SnailNumber investigate = this;
            while (step < 2) {
                switch (step) {
                    case 0 -> {
                        if (investigate.getParent() == null) {
                            return;
                        } else if (((SnailNumberPair)investigate.getParent()).values[index] == investigate) {
                            investigate = investigate.getParent();
                        } else {
                            step++;
                            investigate = ((SnailNumberPair)investigate.getParent()).values[index];
                        }
                    }
                    case 1 -> {
                        if (investigate.size() == 1) {
                            step++;
                            ((SnailNumberPrimitive) investigate).increment(value);
                        } else {
                            investigate = ((SnailNumberPair)investigate).values[locateIndex];
                        }
                    }
                }
            }
        }

        void replace(SnailNumber old, SnailNumber with) {
            if (values[0] == old) {
                values[0] = with;
            } else if (values[1] == old) {
                values[1] = with;
            } else {
                throw new IllegalStateException("Replace request received for %s, but its not a child of %s.".formatted(old, this));
            }
        }

        @Override
        public boolean explode(int depth) {
            if (depth >= 4 && values[0].size() == 1 && values[1].size() == 1) {
                combineOnIndex(0, values[0].value());
                combineOnIndex(1, values[1].value());

                parent.replace(this, SnailNumber.zero(parent));
                return true;
            }

            return values[0].explode(depth + 1) || values[1].explode(depth + 1);
        }

        @Override
        public long magnitude() {
            return 3 * values[0].magnitude() + 2 * values[1].magnitude();
        }

        @Override
        public String toString() {
            return "[%s,%s]".formatted(values[0], values[1]);
        }
    }

    static class SnailNumberPrimitive implements SnailNumber {

        private SnailNumberPair parent;
        private int value;

        public SnailNumberPrimitive(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }

        @Override
        public SnailNumberPair getParent() {
            return parent;
        }

        @Override
        public void setParent(SnailNumberPair parent) {
            this.parent = parent;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean split() {
            if (value > 9) {
                var newPair = new SnailNumberPair();
                newPair.setParent(parent);
                newPair.setPair(
                        SnailNumber.of(newPair, Math.floor(value / 2D)),
                        SnailNumber.of(newPair, Math.ceil(value / 2D)));

                if (parent.left() == this) {
                    parent.setPair(newPair, parent.right());
                } else if (parent.right() == this){
                    parent.setPair(parent.left(), newPair);
                } else {
                    throw new IllegalStateException("Neither left nor right of my parent is me.");
                }

                return true;
            }

            return false;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public void increment(int value) {
            this.value += value;
        }
    }
}
