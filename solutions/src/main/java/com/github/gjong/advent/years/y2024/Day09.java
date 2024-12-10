package com.github.gjong.advent.years.y2024;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Day(year = 2024, day = 9, name = "Disk Fragmenter")
public class Day09 implements DaySolver {

    private final Logger log = org.slf4j.LoggerFactory.getLogger(Day09.class);

    private final InputLoader inputLoader;
    private final Validator validator;

    // Any segment that fits on disk
    sealed interface DiskUsage permits Spacing, FileSegment {
        int size();
    }

    // Empty space on disk
    final class Spacing implements DiskUsage {
        private int size;
        public Spacing(int size) {
            this.size = size;
        }

        public int size() {
            return size;
        }

        public void shrink(int size) {
            this.size -= size;
        }

        @Override
        public String toString() {
            return ".".repeat(size);
        }
    }

    // File on disk
    record FileSegment(int size, int fileNumber) implements DiskUsage {
        public FileSegment reduceSize(int size) {
            return new FileSegment(this.size - size, this.fileNumber);
        }

        @Override
        public String toString() {
            return (fileNumber + "").repeat(size);
        }
    }

    public Day09(InputLoader inputLoader, Validator validator) {
        this.inputLoader = inputLoader;
        this.validator = validator;
    }

    @Override
    public void part1() {
        var diskUsage = parseUsage(inputLoader.string());
        var updatedList = new ArrayList<DiskUsage>();

        // defrag algorithm
        for (var idx = 0; idx < diskUsage.size(); idx++) {
            var usage = diskUsage.get(idx);
            switch (usage) {
                case Spacing spacing -> {
                    // fill from right of list
                    var toFill = spacing.size();
                    while (toFill > 0) {
                        var fillWith = diskUsage.removeLast();
                        switch (fillWith) {
                            case FileSegment fileSegment -> {
                                var movable = Math.min(toFill, fileSegment.size);
                                updatedList.add(new FileSegment(movable, fileSegment.fileNumber));
                                if (movable < fileSegment.size) {
                                    diskUsage.add(fileSegment.reduceSize(movable));
                                }
                                toFill -= movable;
                            }
                            case Spacing space -> {}
                        }
                    }
                }
                case FileSegment fileSegment -> {
                    log.debug("Encountered file {} of size {}, no need to move.", fileSegment.fileNumber, fileSegment.size);
                    updatedList.add(fileSegment);
                }
            }
        }

        log.atDebug()
                .addArgument(() -> updatedList.stream().map(Object::toString).reduce("", (a, b) -> a + b))
                .log("Updated list: {}");

        validator.part1(computeChecksum(updatedList));
    }

    @Override
    public void part2() {
        var diskUsage = parseUsage(inputLoader.string());

        // defrag algorithm
        for (var idx = 0; idx < diskUsage.size(); idx++) {
            var usage = diskUsage.get(idx);
            switch (usage) {
                case Spacing spacing -> {
                    // fill from right of list
                    for (var rtl = diskUsage.size() - 1; rtl >= idx; rtl--) {
                        var fillAttempt = diskUsage.get(rtl);
                        if (fillAttempt instanceof FileSegment fileSegment && fileSegment.size <= spacing.size) {
                            // insert at idx
                            diskUsage.set(idx, fileSegment);

                            // replace file with space
                            diskUsage.set(rtl, new Spacing(fileSegment.size()));

                            spacing.shrink(fileSegment.size);
                            if (spacing.size() > 0) {
                                diskUsage.add(idx + 1, spacing);
                            }

                            break;
                        }
                    }
                }
                case FileSegment s -> {}
            }
        }

        log.atDebug()
                .addArgument(() -> diskUsage.stream().map(Object::toString).reduce("", (a, b) -> a + b))
                .log("Updated list: {}");

        validator.part2(computeChecksum(diskUsage));
    }

    private List<DiskUsage> parseUsage(String input) {
        var diskUsage = new ArrayList<DiskUsage>();
        for (var idx = 0; idx < input.length(); idx++) {
            if (idx % 2 == 0) {
                diskUsage.add(new FileSegment(input.charAt(idx) - '0', idx / 2));
            } else {
                diskUsage.add(new Spacing(input.charAt(idx) - '0'));
            }
        }

        return diskUsage;
    }

    private long computeChecksum(List<DiskUsage> diskUsage) {
        var checkSum = 0L;
        var blockPointer = 0;
        for (var file : diskUsage) {
            if (file instanceof FileSegment fileSegment) {
                for (var i = 0; i < fileSegment.size; i++) {
                    checkSum += (blockPointer + i) * fileSegment.fileNumber;
                }
            }

            blockPointer += file.size();
        }

        return checkSum;
    }
}
