package com.github.gjong.advent;

import java.util.ArrayList;
import java.util.List;

public interface DaySolver {

    void part1();
    void part2();

    List<DaySolver> KNOWN_DAYS = new ArrayList<>();
}
