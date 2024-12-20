package com.github.gjong.advent.algo;

import java.util.List;

public abstract class Node<T extends Node<T>> implements Comparable<T> {

    public record NeighbourWithCost<T>(T node, long cost) {}

    private long cost;
    private boolean visited;
    private T origin;

    protected Node() {
        cost = Long.MAX_VALUE;
    }

    public long cost() {
        return cost;
    }

    public boolean visited() {
        return visited;
    }

    public void visit() {
        visited = true;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public void setOrigin(T origin) {
        this.origin = origin;
    }

    public abstract List<NeighbourWithCost<T>> neighbours();

    @Override
    public int compareTo(T o) {
        return Long.compare(cost, o.cost());
    }
}
