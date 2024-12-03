package com.github.gjong.advent;

import com.github.gjong.advent.cdi.BeanContext;
import com.github.gjong.advent.cdi.BeanProvider;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SolutionProvider {
    private static final SolutionProvider INSTANCE = new SolutionProvider();

    private final BeanProvider context;

    private SolutionProvider() {
        var beanContext = new BeanContext();
        beanContext.initialize();
        context = beanContext;
    }

    public List<Integer> listYears() {
        return StreamSupport.stream(context.provideAll(DaySolver.class).spliterator(), false)
                .map(solver -> solver.getClass().getAnnotation(Day.class).year())
                .sorted()
                .distinct()
                .toList();
    }

    public List<DaySolver> provide(int year) {
        return StreamSupport.stream(context.provideAll(DaySolver.class).spliterator(), false)
                .filter(solver -> solver.getClass().getAnnotation(Day.class).year() == year)
                .toList();
    }

    public DaySolver provide(int year, int day) {
        return StreamSupport.stream(context.provideAll(DaySolver.class).spliterator(), false)
                .filter(solver -> solver.getClass().getAnnotation(Day.class).year() == year)
                .filter(solver -> solver.getClass().getAnnotation(Day.class).day() == day)
                .findFirst()
                .orElseThrow();
    }

    public Map<Integer, List<DaySolver>> provideAll() {
        return StreamSupport.stream(context.provideAll(DaySolver.class).spliterator(), false)
                .collect(Collectors.groupingBy(daySolver -> daySolver.getClass().getAnnotation(Day.class).year()));
    }

    public static SolutionProvider instance() {
        return INSTANCE;
    }
}
