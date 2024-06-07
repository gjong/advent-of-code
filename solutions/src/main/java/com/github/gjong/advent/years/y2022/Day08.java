package com.github.gjong.advent.years.y2022;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import static java.lang.Integer.parseInt;

@Day(day = 8, year = 2022, name = "Treetop Tree House")
public class Day08 implements DaySolver {
    private final InputLoader inputLoader = new InputLoader(2022, 8);
    private final Validator validator = new Validator(2022, 8);

    @Override
    public void part1() {
        var input = inputLoader.splitOnNewLine().toList();
        HighestTree[][] highestTree = processInput();

        var scenicScore = 0;
        for (var row = input.size() - 2; row > 0; row--) {
            for (var column = highestTree[0].length - 2; column > 0; column--) {
                var treeHeight = highestTree[row][column];

                treeHeight.correct(
                        highestTree[row][column + 1],
                        highestTree[row + 1][column]);

                scenicScore = Math.max(scenicScore, treeHeight.scenicScore());
            }
        }

        validator.part1(scenicScore);
    }

    @Override
    public void part2() {
        var input = inputLoader.splitOnNewLine().toList();
        HighestTree[][] highestTree = processInput();

        var visibleTrees = (input.size() * 2) + ((highestTree[0].length - 2) * 2);
        for (var row = input.size() - 2; row > 0; row--) {
            for (var column = highestTree[0].length - 2; column > 0; column--) {
                var treeHeight = highestTree[row][column];

                treeHeight.correct(
                        highestTree[row][column + 1],
                        highestTree[row + 1][column]);

                if (treeHeight.isHighest()) {
                    visibleTrees++;
                }
            }
        }

        validator.part2(visibleTrees);
    }

    private HighestTree[][] processInput() {
        var input = inputLoader.splitOnNewLine().toList();
        HighestTree[][] highestTree = new HighestTree[input.size()][input.getFirst().length()];
        var mostRightTreeIdx = highestTree[0].length - 1;
        var mostBottomTreeIdx = highestTree.length - 1;

        for (var row = 0; row < input.size(); row++) {
            var treeValues = input.get(row).split("");
            for (var column = 0; column < input.get(0).length(); column++) {
                var value = parseInt(treeValues[column]);

                if (row == 0 || row == mostBottomTreeIdx || column == 0 || column == mostRightTreeIdx) {
                    highestTree[row][column] = new HighestTree(value);
                } else {
                    highestTree[row][column] = new HighestTree(
                            highestTree[row][column - 1],
                            highestTree[row - 1][column],
                            value);
                }
            }
        }
        return highestTree;
    }

    class HighestTree {

        private HighestTree leftTree;
        private HighestTree rightTree;
        private HighestTree aboveTree;
        private HighestTree belowTree;

        private int left;
        private int viewToLeft = 0;
        private int right;
        private int viewToRight = 0;
        private int top;
        private int viewToTop = 0;
        private int bottom;
        private int viewToBottom = 0;

        private int value;

        HighestTree(int value) {
            left = value;
            top = value;
            bottom = value;
            right = value;
            this.value = value;
        }

        public HighestTree(HighestTree left, HighestTree above, int value) {
            this.value = value;
            this.left = Math.max(left.value, left.left);
            this.top = Math.max(above.value, above.top);

            leftTree = left;
            aboveTree = above;
            viewToLeft = 1;
            viewToTop = 1;
            if (leftTree.value < value) {
                var nextLeft = left.leftTree;
                while (nextLeft != null) {
                    viewToLeft++;
                    if (value <= nextLeft.value) break;
                    nextLeft = nextLeft.leftTree;
                }
            }
            if (aboveTree.value < value) {
                var nextAbove = aboveTree.aboveTree;
                while (nextAbove != null) {
                    viewToTop++;
                    if (value <= nextAbove.value) break;
                    nextAbove = nextAbove.aboveTree;
                }
            }
        }

        void correct(HighestTree right, HighestTree below) {
            this.right = Math.max(right.value, right.right);
            this.bottom = Math.max(below.value, below.bottom);
            this.belowTree = below;
            this.rightTree = right;

            viewToBottom = 1;
            viewToRight = 1;
            if (leftTree.value < value) {
                var nextRight = right.rightTree;
                while (nextRight != null) {
                    viewToRight++;
                    if (value <= nextRight.value) break;
                    nextRight = nextRight.rightTree;
                }
            }
            if (belowTree.value < value) {
                var nextBelow = belowTree.belowTree;
                while (nextBelow != null) {
                    viewToBottom++;
                    if (value <= nextBelow.value) break;
                    nextBelow = nextBelow.belowTree;
                }
            }
        }

        boolean isHighest() {
            return value > right
                    || value > left
                    || value > top
                    || value > bottom;
        }

        int scenicScore() {
            return viewToRight * viewToLeft * viewToTop * viewToBottom;
        }
    }
}
