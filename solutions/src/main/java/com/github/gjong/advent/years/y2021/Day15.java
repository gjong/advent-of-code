package com.github.gjong.advent.years.y2021;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.Grid;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import com.github.gjong.advent.geo.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.lang.Integer.parseInt;

@Day(day = 15, year = 2021, name = "Chiton")
public class Day15 implements DaySolver {
    private final InputLoader inputLoader = DayLoader.inputDay15();
    private final Validator validator = DayLoader.validatorDay15();

    @Override
    public void part1() {
        var data = inputLoader.splitOnNewLine().toList();
        var costGrid = new Grid<PathFinder.Node>(data.getFirst().length(), data.size());
        costGrid.populate(data, (point, number) -> new PathFinder.Node(parseInt(number)));

        var pathFinder = new PathFinder(costGrid);
        validator.part1(pathFinder.solve());
    }

    @Override
    public void part2() {
        var data = inputLoader.splitOnNewLine().toList();
        var costGrid = new VirtualGrid(data.getFirst().length(), data.size(), 5);
        costGrid.populate(data, (point, number) -> new PathFinder.Node(parseInt(number)));

        var pathFinder = new PathFinder(costGrid);
        validator.part2(pathFinder.solve());
    }

    static class PathFinder {
        public static class Node {
            private final int weight;
            private long distance = Long.MAX_VALUE;
            private final Node[] connectedNodes;

            public Node(int weight) {
                this.weight = weight;
                this.connectedNodes = new Node[4];
            }

            public int getWeight() {
                return weight;
            }

            public long getDistance() {
                return distance;
            }
        }

        private final Grid<Node> costGrid;

        public PathFinder(Grid<Node> costGrid) {
            for (var y = 0; y < costGrid.height(); y++) {
                for (var x = 0; x < costGrid.width(); x++) {
                    var node = costGrid.at(x, y);

                    if (costGrid.inBounds(x + 1, y)) {
                        node.connectedNodes[0] = costGrid.at(x + 1, y);
                    }
                    if (costGrid.inBounds(x - 1, y)) {
                        node.connectedNodes[1] = costGrid.at(x - 1, y);
                    }
                    if (costGrid.inBounds(x, y + 1)) {
                        node.connectedNodes[2] = costGrid.at(x, y + 1);
                    }
                    if (costGrid.inBounds(x, y - 1)) {
                        node.connectedNodes[1] = costGrid.at(x, y - 1);
                    }
                }
            }
            this.costGrid = costGrid;
        }

        public long solve() {
            Set<Node> settledNodes = new HashSet<>();
            PriorityQueue<Node> pq = new PriorityQueue<>(1000, Comparator.comparingLong(Node::getDistance));

            var startNode = this.costGrid.at(0, 0);
            startNode.distance = 0;
            pq.add(startNode);

            while (!pq.isEmpty()) {
                var evaluate = pq.poll();
                settledNodes.add(evaluate);

                for (var node : evaluate.connectedNodes) {
                    if (node != null && !settledNodes.contains(node)) {
                        var computedDistance = evaluate.distance + node.weight;
                        if (computedDistance < node.distance) {
                            node.distance = computedDistance;
                            pq.add(node);
                        }
                    }
                }
            }

            return costGrid.at(costGrid.width() - 1, costGrid.height() - 1).distance;
        }
    }

    static class VirtualGrid extends Grid<PathFinder.Node> {
        final Map<Point, PathFinder.Node> computed = new HashMap<>();

        final int virtualWidth;
        final int virtualHeight;

        public VirtualGrid(int width, int height, int expanded) {
            super(width, height);
            this.virtualHeight = height * expanded;
            this.virtualWidth = width * expanded;
        }

        @Override
        public int width() {
            return virtualWidth;
        }

        @Override
        public int height() {
            return virtualHeight;
        }

        @Override
        public PathFinder.Node at(int x, int y) {
            return computed.computeIfAbsent(new Point(x, y), point -> {
                var actualWidth = super.width();
                var actualHeight = super.height();
                var sourceX = x % actualWidth;
                var sourceY = y % actualHeight;
                var sourceValue = super.at(sourceX, sourceY);

                var xCorrection = x / actualWidth;
                var yCorrection = y / actualHeight;
                var correctedValue = sourceValue.getWeight() + xCorrection + yCorrection;
                if (correctedValue > 9) {
                    correctedValue -= 9;
                }

                return new PathFinder.Node(correctedValue);
            });
        }
    }
}
