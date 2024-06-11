package com.github.gjong.advent.years.y2022;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Long.parseLong;

@Day(day = 7, year = 2022, name = "No Space Left On Device")
public class Day07 implements DaySolver {
    private final InputLoader inputLoader = DayLoader.inputDay7();
    private final Validator validator = DayLoader.validatorDay7();

    private static final int TOTAL_DISK_SIZE = 70_000_000;
    private static final int NEEDED_DISK_SIZE = 30_000_000;

    @Override
    public void part1() {
        var answer = computeTotalSize(readListing());
        validator.part1(answer);
    }

    @Override
    public void part2() {
        Directory rootDirectory = readListing();

        var clearSpace = NEEDED_DISK_SIZE - (TOTAL_DISK_SIZE - rootDirectory.size());
        var directoriesOnly = rootDirectory.directories();

        var answer = directoriesOnly.stream()
                .filter(dir -> dir.size() >= clearSpace).min(Comparator.comparingLong(Directory::size))
                .map(Directory::size)
                .get();

        validator.part2(answer);
    }

    private Directory readListing() {
        var rootDirectory = new Directory("/");
        var workingDirectory = rootDirectory;
        var input = inputLoader.scanner();
        while (input.hasNextLine()) {
            var instruction = input.nextLine();
            if (instruction.startsWith("$")) {
                workingDirectory = processInstruction(workingDirectory, instruction.substring(2));
            } else {
                processOutput(workingDirectory, instruction);
            }
        }
        return rootDirectory;
    }

    private long computeTotalSize(Directory directory) {
        var size = 0L;
        if (directory.size() <= 100000) {
            size += directory.size();
        }

        size += directory.contents().stream()
                .filter(Directory.class::isInstance)
                .map(Directory.class::cast)
                .mapToLong(this::computeTotalSize)
                .sum();

        return size;
    }

    private void processOutput(Directory workingDirectory, String instruction) {
        if (!instruction.startsWith("dir")) {
            var sizeAndFile = instruction.split(" ");
            workingDirectory.add(new File(sizeAndFile[1], parseLong(sizeAndFile[0])));
        }
    }

    private Directory processInstruction(Directory workingDirectory, String instruction) {
        if (instruction.startsWith("ls")) {
            return workingDirectory;
        } else if (instruction.startsWith("cd")) {
            var directory = instruction.substring("cd ".length());
            if (directory.equals("..")) {
                return workingDirectory.parent();
            } else {
                var childDir = new Directory(workingDirectory, directory);
                workingDirectory.add(childDir);
                return childDir;
            }
        }
        throw new IllegalStateException("Unknown instruction " + instruction);
    }

    interface ListingElement {
        String name();
        long size();
    }

    class Directory implements ListingElement {
        private final Directory parent;
        private final String name;
        private final Set<ListingElement> contents;

        Directory(String name) {
            this.parent = null;
            this.name = name;
            contents = new HashSet<>();
        }

        Directory(Directory parent, String name) {
            this.parent = parent;
            this.name = name;
            contents = new HashSet<>();
        }

        public Set<ListingElement> contents() {
            return contents;
        }

        public Set<Directory> directories() {
            return Stream.concat(Stream.of(this),
                    contents.stream()
                            .filter(Directory.class::isInstance)
                            .map(Directory.class::cast)
                            .flatMap(d -> d.directories().stream())
            ).collect(Collectors.toSet());
        }

        public Directory parent() {
            return parent;
        }

        public void add(ListingElement child) {
            contents.add(child);
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public long size() {
            return this.contents.stream()
                    .mapToLong(ListingElement::size)
                    .sum();
        }
    }

    class File implements ListingElement {
        private final String name;
        private final long size;

        File(String name, long size) {
            this.name = name;
            this.size = size;
        }

        @Override
        public long size() {
            return size;
        }

        @Override
        public String name() {
            return name;
        }
    }
}
