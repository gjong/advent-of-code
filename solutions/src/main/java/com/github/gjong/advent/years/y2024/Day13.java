package com.github.gjong.advent.years.y2024;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.algo.Algo;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

@Day(year = 2024, day = 13, name = "Claw Contraption")
public class Day13 implements DaySolver {

    private final Logger log = org.slf4j.LoggerFactory.getLogger(Day13.class);

    private final InputLoader inputLoader;
    private final Validator validator;

    record PlayCost(int x, int y) {}
    record WinPoint(long x, long y) {
        public WinPoint translate(long dx, long dy) {
            return new WinPoint(x + dx, y + dy);
        }
    }
    record Machine(WinPoint price, PlayCost buttonA, PlayCost buttonB) {
        long buttonDeterminant() {
            return Algo.determinant(buttonA.x(), buttonB.y(), buttonA.y(), buttonB.x());
        }

        long buttonABest() {
            return Algo.determinant(price.y(), buttonA.x(), price.x(), buttonA.y());
        }

        long buttonBBest() {
            return Algo.determinant(price.x(), buttonB.y(), price.y(), buttonB.x());
        }
    }

    private final List<Machine> computeMachines = new ArrayList<>();

    public Day13(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void readInput() {
        Function<String, PlayCost> costExtractor = costLine -> {
            var matcher = Pattern.compile("X\\+(?<X>[0-9]+), Y\\+(?<Y>[0-9]+)")
                    .matcher(costLine);
            matcher.find();
            return new PlayCost(
                    parseInt(matcher.group("X")),
                    parseInt(matcher.group("Y")));
        };
        Function<String, WinPoint> priceExtractor = costLine -> {
            var matcher = Pattern.compile("X=(?<X>[0-9]+), Y=(?<Y>[0-9]+)")
                    .matcher(costLine);
            matcher.find();
            return new WinPoint(
                    parseInt(matcher.group("X")),
                    parseInt(matcher.group("Y")));
        };

        for (var machineString : inputLoader.string().split("\n\n")) {
            var instructions = machineString.split("\n");
            computeMachines.add(new Machine(
                    priceExtractor.apply(instructions[2]),
                    costExtractor.apply(instructions[0]),
                    costExtractor.apply(instructions[1])));
        }
    }

    @Override
    public void part1() {
        var totalCost = computeMachines.stream()
                .mapToLong(this::computeCost)
                .sum();

        validator.part1(totalCost);
    }

    @Override
    public void part2() {
        var addedCost = 10000000000000L;
        var totalCost = computeMachines.stream()
                .map(machine -> new Machine(machine.price().translate(addedCost, addedCost), machine.buttonB, machine.buttonA))
                .mapToLong(this::computeCost)
                .sum();

        validator.part2(totalCost);
    }

    /**
     * Computes the cost for a given machine based on its properties.
     * Using the <a href="https://en.wikipedia.org/wiki/Cramer%27s_rule">Cramer Rule</a>.
     *
     * @param machine the machine for which to compute the cost
     * @return the computed cost, which is a long value representing the total cost based on certain conditions,
     *         or 0L if the cost calculation does not meet the criteria
     */
    private long computeCost(Machine machine) {
        var D = machine.buttonDeterminant();
        var numerator_n = machine.buttonBBest();
        var numerator_m = machine.buttonABest();

        log.debug("D: {}, numerator_n: {}, numerator_m: {}", D, numerator_n, numerator_m);

        if (D != 0 && numerator_n % D == 0 && numerator_m % D == 0) {
            var n0 = numerator_n / D;
            var m0 = numerator_m / D;

            if (n0 >= 0 && m0 >= 0) {
                var computedCostX = machine.buttonA.x() * n0 + machine.buttonB.x() * m0;
                var computedCostY = machine.buttonA.y() * n0 + machine.buttonB.y() * m0;
                if (computedCostX == machine.price().x() &&  computedCostY ==  machine.price().y()) {
                    return 3 * n0 + m0;
                }
            }
        }

        return 0L;
    }
}