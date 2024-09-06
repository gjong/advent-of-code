package com.github.gjong.advent.years.y2021;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

@Day(day = 12, year = 2021, name = "Passage Pathing")
public class Day12 implements DaySolver {
    private final InputLoader inputLoader;
    private final Validator validator;

    public Day12(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void part1() {
        var graph = processInput();

        var resolver = new RouteResolver<>(graph, this::canVisit);
        var routes = resolver.resolveAllRoutes(
                graph.computeIfAbsent("start"),
                graph.computeIfAbsent("end"));

        validator.part1(routes.size());
    }

    @Override
    public void part2() {
        var graph = processInput();
        var resolver = new RouteResolver<>(graph, this::canVisit2);
        var routes = resolver.resolveAllRoutes(
                graph.computeIfAbsent("start"),
                graph.computeIfAbsent("end"));

        validator.part2(routes.size());
    }

    private Graph<String> processInput() {
        var graph = new Graph<String>();
        inputLoader.splitOnNewLine()
                .forEach(line -> {
                    var path = line.trim().split("-");
                    graph.addPath(
                            graph.computeIfAbsent(path[0]),
                            graph.computeIfAbsent(path[1]));
                });
        return graph;
    }

    private boolean canVisit(GraphNode<String> node, Route<String> route) {
        if (node.value().equalsIgnoreCase("start")) {
            return false;
        }

        if (!node.value().equals(node.value().toUpperCase())) {
            return !route.containsNode(node);
        }

        return true;
    }

    private boolean canVisit2(GraphNode<String> node, Route<String> route) {
        if (node.value().equalsIgnoreCase("start")) {
            return false;
        }

        if (isBigCave(node.value())) {
            return true;
        }

        if (route.containsNode(node)) {
            return route.getSteps()
                    .stream()
                    .filter(step -> !isBigCave(step.value()))
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                    .values()
                    .stream()
                    .noneMatch(count -> count > 1);
        }

        return true;
    }

    private boolean isBigCave(String caveName) {
        return caveName.equals(caveName.toUpperCase());
    }

    record GraphNode<T>(T value) {}

    static class Graph<T> {
        private final Set<GraphNode<T>> nodes;
        private final Map<GraphNode<T>, Set<GraphNode<T>>> paths;

        public Graph() {
            nodes = new HashSet<>();
            paths = new HashMap<>();
        }

        public GraphNode<T> computeIfAbsent(T value) {
            for (var node : nodes) {
                if (Objects.equals(node.value(), value)) {
                    return node;
                }
            }

            var node = new GraphNode<>(value);
            nodes.add(node);
            return node;
        }

        public void addPath(GraphNode<T> start, GraphNode<T> end) {
            paths.computeIfAbsent(start, key -> new HashSet<>()).add(end);
            paths.computeIfAbsent(end, key -> new HashSet<>()).add(start);
        }

        public Set<GraphNode<T>> getConnectedNodes(GraphNode<T> node) {
            return Set.copyOf(paths.get(node));
        }
    }

    static class Route<T> {
        private final GraphNode<T> start;
        private final GraphNode<T> end;

        private final List<GraphNode<T>> steps;

        public Route(GraphNode<T> start, GraphNode<T> end) {
            this.start = start;
            this.end = end;
            this.steps = new ArrayList<>();
        }

        public GraphNode<T> getEnd() {
            return end;
        }

        public boolean containsNode(GraphNode<T> node) {
            return steps.contains(node);
        }

        public List<GraphNode<T>> getSteps() {
            return steps;
        }

        public Route<T> addStep(GraphNode<T> node) {
            var updatedRoute = new Route<>(start, end);
            updatedRoute.steps.addAll(steps);
            updatedRoute.steps.add(node);
            return updatedRoute;
        }

        @Override
        public String toString() {
            return start + steps.toString();
        }
    }

    static class RouteResolver<T> {

        private final Graph<T> graph;
        private final BiPredicate<GraphNode<T>, Route<T>> visitAllowed;

        public RouteResolver(Graph<T> graph, BiPredicate<GraphNode<T>, Route<T>> visitAllowed) {
            this.visitAllowed = visitAllowed;
            this.graph = graph;
        }

        public List<Route<T>> resolveAllRoutes(GraphNode<T> start, GraphNode<T> end) {
            return resolveNextStep(
                    new Route<>(start, end),
                    start);
        }

        public List<Route<T>> resolveNextStep(Route<T> existingRoute, GraphNode<T> fromNode) {
            var routes = new ArrayList<Route<T>>();
            for (var nextStep : graph.getConnectedNodes(fromNode)) {
                if (nextStep == existingRoute.getEnd()) {
                    routes.add(existingRoute.addStep(nextStep));
                } else if (visitAllowed.test(nextStep, existingRoute)) {
                    routes.addAll(
                            resolveNextStep(
                                    existingRoute.addStep(nextStep),
                                    nextStep));
                }
            }
            return routes;
        }
    }
}
