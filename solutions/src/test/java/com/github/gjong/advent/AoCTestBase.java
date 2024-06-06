package com.github.gjong.advent;

import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

abstract class AoCTestBase {
    static {
        ResolvedAssignment.initialize();
    }

    @Retention(RetentionPolicy.RUNTIME)
    protected @interface Year {
        int value();
    }

    private final List<DaySolver> testSubjects;

    public AoCTestBase() {
        var year = getClass().getAnnotation(Year.class).value();
        testSubjects = DaySolver.KNOWN_DAYS
                .stream()
                .filter(daySolver -> daySolver.getClass().getAnnotation(Day.class).year() == year)
                .toList();
    }

    @TestFactory
    @DisplayName("Run entire year's test cases")
    Stream<DynamicTest> generateTestCases() {
        var year = getClass().getAnnotation(Year.class).value();

        return testSubjects.stream()
                .flatMap(daySolver -> {
                            var day = daySolver.getClass().getAnnotation(Day.class).day();
                            return findTestInput(year, daySolver.getClass().getAnnotation(Day.class).day())
                                    .flatMap(path -> {
                                        var sample = path.getFileName().toString()
                                                .replace("day_%02d_".formatted(day), "")
                                                .replace(".txt", "");

                                        return Stream.concat(
                                                Stream.of(DynamicTest.dynamicTest(
                                                        "Day %d: %s - part 1".formatted(day, sample),
                                                        () -> {
                                                            // given
                                                            injectInput(daySolver, path, sample);
                                                            daySolver.part1();
                                                        }
                                                )),
                                                Stream.of(DynamicTest.dynamicTest(
                                                        "Day %d: %s - part 2".formatted(day, sample),
                                                        () -> {
                                                            // given
                                                            injectInput(daySolver, path, sample);
                                                            daySolver.part2();
                                                        }
                                                )));

                                    });
                        }
                );
    }

    private void injectInput(DaySolver daySolver, Path path, String sample) {
        try {
            var classPath = "/input/" + Paths.get(AoCTestBase.class.getResource("/input").toURI())
                    .relativize(path).toString().replaceAll("\\\\", "/");

            var day = daySolver.getClass().getAnnotation(Day.class);

            for (var field : daySolver.getClass().getDeclaredFields()) {
                if (field.getType() == InputLoader.class) {
                    field.setAccessible(true);
                    field.set(daySolver, new InputLoader(classPath));
                }

                if (field.getType() == Validator.class) {
                    field.setAccessible(true);
                    field.set(daySolver, new Validator(day.year(), day.day()) {
                        @Override
                        public void part1(long answer) {
                            Assertions.assertTrue(
                                    validate("part1_" + sample, answer),
                                    "Failed for day %d, part 1, case %s.".formatted(day.day(), sample));
                        }

                        @Override
                        public void part2(long answer) {
                            Assertions.assertTrue(
                                    validate("part2_" + sample, answer),
                                    "Failed for day %d, part 2, case %s.".formatted(day.day(), sample));
                        }
                    });
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not read test input.", e);
        }
    }

    private Stream<Path> findTestInput(int year, int day) {
        var resourceRoot = AoCTestBase.class.getResource("/input/%d".formatted(year));
        try {
            return Files.walk(Paths.get(resourceRoot.toURI()))
                    .filter(path -> path.getFileName().toString().startsWith("day_%02d".formatted(day)))
                    .filter(path -> !path.getFileName().toString().equals("day_%02d.txt".formatted(day)));
        } catch (Exception e) {
            throw new RuntimeException("Could not find test input.", e);
        }
    }

}
