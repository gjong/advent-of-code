package com.github.gjong.advent.years.y2022;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

@Day(day = 13, year = 2022, name = "Distress Signal")
public class Day13 implements DaySolver {
    private final InputLoader inputLoader = new InputLoader(2022, 13);
    private final Validator validator = new Validator(2022, 13);

    @Override
    public void part1() {
        var pairIdx = 1;
        DataPacket leftPacket = null, rightPacket = null;

        var result = 0;
        var input = inputLoader.scanner();
        while (input.hasNextLine()) {
            var line = input.nextLine();
            if (line.isEmpty()) {
                // compare the packets
                if (leftPacket.compareTo(rightPacket) < 0) {
                    result += pairIdx;
                }

                leftPacket = null;
                rightPacket = null;
                pairIdx++;
            } else {
                if (leftPacket == null) {
                    leftPacket = DataPacket.of(line);
                } else {
                    rightPacket = DataPacket.of(line);
                }
            }
        }

        validator.part1(result);
    }

    @Override
    public void part2() {
        var smallerDiv2 = 1;
        var smallerDiv6 = 2; // starts at 2 because [[2]] is also in the list

        var dataPacketDiv2 = DataPacket.of("[[2]]");
        var dataPacketDiv6 = DataPacket.of("[[6]]");
        var input = inputLoader.scanner();
        while (input.hasNextLine()) {
            var line = input.nextLine();
            if (line.isEmpty()) {
                continue;
            }

            var packet = DataPacket.of(line);
            if (packet.compareTo(dataPacketDiv2) < 0) smallerDiv2++;
            if (packet.compareTo(dataPacketDiv6) < 0) smallerDiv6++;
        }

        validator.part2((long) smallerDiv2 * smallerDiv6);
    }

    interface Packet extends Comparable<Packet> {
    }

    static class DataPacket implements Packet {

        private record SingleValue(int value) implements Packet {
            @Override
            public int compareTo(Packet o) {
                if (o instanceof DataPacket) {
                    return new DataPacket(List.of(this)).compareTo(o);
                }
                if (o instanceof SingleValue casted) {
                    return Integer.compare(value, casted.value);
                }

                return -1;
            }

            @Override
            public String toString() {
                return String.valueOf(value);
            }

            static SingleValue of(char startNumber, List<Character> characters) {
                var number = String.valueOf(startNumber);
                if (!characters.isEmpty() && Character.isDigit(characters.getFirst())) {
                    number += characters.removeFirst();
                }
                return new SingleValue(parseInt(number));
            }
        }

        private final List<Packet> packets;

        private DataPacket(List<Packet> packets) {
            this.packets = packets;
        }

        @Override
        public int compareTo(Packet other) {
            if (other instanceof SingleValue) {
                return compareTo(new DataPacket(List.of(other)));
            }

            if (other instanceof DataPacket casted) {
                for (var idx = 0; idx < packets.size(); idx++) {
                    if (idx >= casted.packets.size()) {
                        return 1;
                    }

                    var compared = packets.get(idx).compareTo(casted.packets.get(idx));
                    if (compared == 0) {
                        continue;
                    }
                    return compared;
                }

                if (packets.size() < casted.packets.size()) {
                    return -1;
                }
            }

            return 0;
        }

        @Override
        public String toString() {
            return "[%s]".formatted(
                    packets.stream()
                            .map(Packet::toString)
                            .collect(Collectors.joining(","))
            );
        }

        public static DataPacket of(String line) {
            var dataPacket = line.substring(1, line.length() - 1);
            List<Character> characters = new ArrayList<>(dataPacket.length());
            for (char character : dataPacket.toCharArray())
                characters.add(character);

            return (DataPacket) parse(characters);
        }

        private static Packet parse(List<Character> characters) {
            var dataPacket = new DataPacket(new ArrayList<>());
            while (!characters.isEmpty()) {
                var nextChar = characters.removeFirst();
                if (nextChar == '[') {
                    dataPacket.packets.add(DataPacket.parse(characters));
                } else if (nextChar == ']') {
                    return dataPacket;
                } else if (nextChar != ',') {
                    dataPacket.packets.add(SingleValue.of(nextChar, characters));
                }
            }

            return dataPacket;
        }
    }
}
