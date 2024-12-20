package com.github.gjong.advent.algo;

import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.Predicate;

public class PathFinding {

    public static <T extends Node<T>> Optional<T> dijkstra(List<T> start,
                                                   Predicate<T> desiredPoint) {
        var toVisit = new PriorityQueue<T>();

        toVisit.addAll(start);
        while (!toVisit.isEmpty()) {
            var visit = toVisit.poll();
            if (desiredPoint.test(visit)) {
                return Optional.of(visit);
            }
            if (visit.visited()) {
                continue;
            }

            visit.visit();
            for (var neighbour : visit.neighbours()) {
                var updatedCost = visit.cost() + neighbour.cost();

                if (neighbour.node().cost() > updatedCost) {
                    neighbour.node().setOrigin(visit);
                    neighbour.node().setCost(updatedCost);
                    toVisit.add(neighbour.node());
                }
            }
        }

        return Optional.empty();
    }
}
