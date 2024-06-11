package com.github.gjong.advent.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class InputLoader {

    private final String resourceFile;

    public InputLoader(int year, int day) {
        resourceFile = "/input/%d/day_%02d.txt".formatted(year, day);
    }

    public InputLoader(String resourceFile) {
        this.resourceFile = resourceFile;
    }

    public Stream<String> splitOnNewLine() {
        return string().lines();
    }

    public IntStream splitOnNewLineToInt() {
        return string().lines()
                .map(String::trim)
                .mapToInt(Integer::parseInt);
    }

    public char[] chars() {
        return string().toCharArray();
    }

    public String string() {
        try (var inputData = readInputData()) {
            return new String(inputData.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Could not read input data.", e);
        }
    }

    public String[] split(CharSequence splitter) {
        return string().split(splitter.toString());
    }

    public Scanner scanner() {
        return new Scanner(string());
    }

    public void consumeLine(Consumer<String> lineConsumer) {
        var scanner = scanner();
        while (scanner.hasNextLine()) {
            lineConsumer.accept(scanner.nextLine());
        }
    }

    public CharGrid charGrid() {
        return new CharGrid(string());
    }

    private InputStream readInputData() {
        var resourceStream = getClass().getResourceAsStream(resourceFile);
        assert resourceStream != null : "Could not read input data.";

        return resourceStream;
    }
}
