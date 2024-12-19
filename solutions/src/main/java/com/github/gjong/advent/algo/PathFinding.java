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
            for (var neighbour : visit.neighbours().entrySet()) {
                var updatedCost = visit.cost() + neighbour.getValue();

                if (neighbour.getKey().cost() > updatedCost) {
                    neighbour.getKey().setCost(updatedCost);
                    toVisit.add(neighbour.getKey());
                }
            }
        }

        return Optional.empty();
    }
}
