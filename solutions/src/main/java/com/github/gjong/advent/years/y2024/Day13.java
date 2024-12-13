package com.github.gjong.advent.years.y2024;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
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

    record PlayCost(int x, int y) {
    }

    record WinPoint(long x, long y) {
        public WinPoint translate(long dx, long dy) {
            return new WinPoint(x + dx, y + dy);
        }
    }

    record Machine(WinPoint price, PlayCost buttonA, PlayCost buttonB) {

        /**
         * Computes the minimum cost based on the given machine's price and button configurations.
         * If the cost calculation results in a valid integer value, the formula is applied to determine the minimum cost.
         * Otherwise, returns 0 as the default cost.
         *
         * @return the computed minimum cost based on the machine's configuration
         */
        long computeMinCost() {
            var top = price.y() * buttonA.x() - price().x() * buttonA.y();
            var bottom = buttonA().x() * buttonB.y() - buttonA().y() * buttonB.x();
            if (top % bottom == 0) {
                var D = price.y() - (top / bottom) * buttonB.y();
                if (D % buttonA.y() == 0) {
                    return 3 * (D / buttonA.y()) + (top / bottom);
                }
            }

            return 0L;
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
            var matcher = Pattern.compile("X\\+(?<X>\\d+), Y\\+(?<Y>\\d+)")
                    .matcher(costLine);
            matcher.find();
            return new PlayCost(
                    parseInt(matcher.group("X")),
                    parseInt(matcher.group("Y")));
        };
        Function<String, WinPoint> priceExtractor = costLine -> {
            var matcher = Pattern.compile("X=(?<X>\\d+), Y=(?<Y>\\d+)")
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
                .mapToLong(Machine::computeMinCost)
                .sum();

        validator.part1(totalCost);
    }

    @Override
    public void part2() {
        var addedCost = 10000000000000L;
        var totalCost = computeMachines.stream()
                .map(machine -> new Machine(machine.price().translate(addedCost, addedCost), machine.buttonB, machine.buttonA))
                .mapToLong(Machine::computeMinCost)
                .sum();

        validator.part2(totalCost);
    }
}