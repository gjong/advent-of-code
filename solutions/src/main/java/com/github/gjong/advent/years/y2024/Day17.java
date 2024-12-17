package com.github.gjong.advent.years.y2024;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Day(year = 2024, day = 17, name = "Chronospatial Computer")
public class Day17 implements DaySolver {

    private final Logger log = LoggerFactory.getLogger(Day17.class);

    private static final int A = 0;
    private static final int B = 1;
    private static final int C = 2;

    private final InputLoader inputLoader;
    private final Validator validator;
    private ComputeEngine engine;

    public Day17(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    enum Opcode {
        adv, bxl, bst, jnz, bxc, out, bdv, cdv
    }

    @Override
    public void readInput() {
        var lines = inputLoader.split("\n");
        engine = new ComputeEngine(
                new long[]{Integer.parseInt(lines[0].substring(12)),
                        Integer.parseInt(lines[1].substring(12)),
                        Integer.parseInt(lines[2].substring(12))},
                Arrays.stream(lines[4].substring(9)
                        .split(",")).mapToInt(Integer::parseInt).toArray()
        );
    }

    @Override
    public void part1() {
        validator.part1(engine.run());
    }

    @Override
    public void part2() {
        validator.part2(find(engine.program.length - 1, 0));
    }

    private long find(int pointer, long a) {
        if (pointer == -1) {
            return a;
        }

        a = a * 8;
        for (var i = 0; i < 8; i++) {
            engine.reset(a + i, 0, 0);
            engine.run();
            if (engine.tailEqual()) {
                long candidate = find(pointer - 1, a + i);
                if (candidate >= 0) {
                    return candidate;
                }
            }
        }

        return -1;
    }

    public static class ComputeEngine {
        private final long[] registers;

        int[] program;
        int instructionPointer;

        List<Integer> output = new ArrayList<>();

        public ComputeEngine(long[] registers, int[] program) {
            this.registers = registers;
            this.program = program;
            this.instructionPointer = 0;
        }

        public void reset(long a, long b, long c) {
            registers[A] = a;
            registers[B] = b;
            registers[C] = c;
            instructionPointer = 0;
            output.clear();
        }

        public String run() {
            while (instructionPointer < program.length) {
                var combo = switch (program[instructionPointer + 1]) {
                    case 4 -> registers[0];
                    case 5 -> registers[1];
                    case 6 -> registers[2];
                    default -> program[instructionPointer + 1];
                };

                var opcode = Opcode.class.getEnumConstants()[program[instructionPointer]];
                switch (opcode) {
                    case adv -> registers[A] = registers[A] / (1L << combo);
                    case bxl -> registers[B] ^= program[instructionPointer + 1];
                    case bst -> registers[B] = combo & 7;
                    case jnz -> {
                        if (registers[A] != 0) {
                            instructionPointer = program[instructionPointer + 1] - 2;
                        }
                    }
                    case bxc -> registers[B] ^= registers[C];
                    case bdv -> registers[B] = registers[A] / (1L << combo);
                    case cdv -> registers[C] = registers[A] / (1L << combo);
                    case out -> output.add((int) combo & 7);
                }

                instructionPointer += 2;
            }

            return output.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
        }

        public boolean tailEqual() {
            int d = program.length - output.size();
            for (int i = 0; i < output.size(); i++) {
                if (program[d + i] != output.get(i)) {
                    return false;
                }
            }
            return true;
        }
    }
}
