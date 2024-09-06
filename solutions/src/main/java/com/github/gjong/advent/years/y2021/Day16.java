package com.github.gjong.advent.years.y2021;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

@Day(day = 16, year = 2021, name = "Packet Decoder")
public class Day16 implements DaySolver {
    private final InputLoader inputLoader;
    private final Validator validator;

    public Day16(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void part1() {
        var instructions = inputLoader.splitOnNewLine()
                .map(HexDecoder::parseOperations)
                .toList();

        var cumulativeVersion = 0;
        for (var instruction : instructions) {
            cumulativeVersion += computeVersion(instruction);
        }

        validator.part1(cumulativeVersion);
    }

    @Override
    public void part2() {
        var instructions = inputLoader.splitOnNewLine()
                .map(HexDecoder::parseOperations)
                .toList();

        var computedValue = instructions.getFirst().getLiteralValue();
        validator.part2(computedValue);
    }

    private int computeVersion(EncodedOperation operation) {
        int summedVersion = operation.getVersion();

        for (var child : operation.getSubOperations()) {
            summedVersion += computeVersion(child);
        }

        return summedVersion;
    }

    static class EncodedOperation {
        private final int version;
        private final int type;

        private long literalValue;
        private final List<EncodedOperation> subOperations;

        EncodedOperation(int version, int type) {
            this.version = version;
            this.type = type;
            this.subOperations = new ArrayList<>();
        }

        public void setLiteralValue(long literalValue) {
            this.literalValue = literalValue;
        }

        public long getLiteralValue() {
            var subProcessor = getSubOperations()
                    .stream()
                    .map(EncodedOperation::getLiteralValue);

            return switch (getType()) {
                case 0 -> subProcessor.reduce(0L, Long::sum);
                case 1 -> subProcessor.reduce(1L, (value, acc) -> value * acc);
                case 2 -> subProcessor.min(Long::compareTo).orElse(0L);
                case 3 -> subProcessor.max(Long::compareTo).orElse(0L);
                case 4 -> literalValue;
                case 5 -> getSubOperations().get(0).getLiteralValue() > getSubOperations().get(1).getLiteralValue()
                        ? 1 : 0;
                case 6 -> getSubOperations().get(0).getLiteralValue() < getSubOperations().get(1).getLiteralValue()
                        ? 1 : 0;
                case 7 -> getSubOperations().get(0).getLiteralValue() == getSubOperations().get(1).getLiteralValue()
                        ? 1 : 0;
                default -> throw new IllegalStateException("Unsupported type " + getType());
            };
        }

        public int getType() {
            return type;
        }

        public int getVersion() {
            return version;
        }

        public List<EncodedOperation> getSubOperations() {
            return subOperations;
        }

    }

    static class HexDecoder {

        public static EncodedOperation parseOperations(String hexOperation) {
            var bitRepresentation = new StringBuilder();
            for (var hex : hexOperation.toCharArray()) {
                var bits = new BigInteger(String.valueOf(hex), 16).toString(2);
                bitRepresentation.append("%4s".formatted(bits).replace(' ', '0'));
            }

            List<EncodedOperation> operations = new ArrayList<>();
            processBitString(bitRepresentation.toString(), 0, operations);
            return operations.getFirst();
        }

        private static int processBitString(String bitRepresentation, int startPosition, List<EncodedOperation> operations) {
            var index = startPosition;
            var version = parseInt(bitRepresentation.substring(index, index + 3), 2);
            var type = parseInt(bitRepresentation.substring(index + 3, index + 6), 2);

            var operation = new EncodedOperation(version, type);
            operations.add(operation);

            index += 6;
            if (type == 4) {
                // literal value
                var notAtEnd = true;
                var bitBuilder = new StringBuilder();

                while (notAtEnd) {
                    notAtEnd = bitRepresentation.charAt(index) == '1';
                    bitBuilder.append(bitRepresentation, index + 1, index + 5);
                    index += 5;
                }

                operation.setLiteralValue(parseLong(bitBuilder.toString(), 2));
            } else {
                // sub operation
                var lengthTypeId = bitRepresentation.charAt(index);

                index++;
                if (lengthTypeId == '0') {
                    var bitLength = parseInt(bitRepresentation.substring(index, index + 15), 2);
                    index += 15;

                    var endPosition = index + bitLength;
                    while (index < endPosition) {
                        index = processBitString(bitRepresentation, index, operation.getSubOperations());
                    }
                } else {
                    var subOperationCount = parseInt(bitRepresentation.substring(index, index + 11), 2);
                    index += 11;
                    for (var currentSub = 0; currentSub < subOperationCount; currentSub++) {
                        index = processBitString(bitRepresentation, index, operation.getSubOperations());
                    }
                }
            }

            return index;
        }
    }

}
