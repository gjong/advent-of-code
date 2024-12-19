package com.github.gjong.advent.algo;

import java.util.Map;

public abstract class Node<T extends Node<T>> implements Comparable<T> {
    private long cost;
    private boolean visited;

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

    public abstract Map<T, Long> neighbours();

    @Override
    public int compareTo(T o) {
        return Long.compare(cost, o.cost());
    }
}
