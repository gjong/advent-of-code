# Advent of Code Solutions

Welcome to the repository containing my solutions for [Advent of Code](https://adventofcode.com/), an annual coding
event that takes place every December.
Each day, a new programming challenge is released, and this repository contains my attempts to solve those challenges.

## Repository structure

The repository is organized as a Java project with a submodule named solutions and one called annotation-scanner.

### Annotation Scanner

The annotation scanner module supports
the [generation](annotation-scanner/src/main/java/com/github/gjong/advent/processor/DayProcessor.java) of the
`DaySolver`
class for each year.
This annotation scanner will add an instance of the DaySolver in the bean context registry.

Automated test generation is done using
the [generation](annotation-scanner/src/main/java/com/github/gjong/advent/processor/TestCaseProcessor.java) test classes
per year with test cases for each case.
A test case is determined by files in the following location:

    test/resources/input/%year%/day_%day%_%test-case%

Where the year and day are substituted with the year and day of the challenge, and the test-case is the name of the test
case.

### Solutions

The solutions module includes a package `com.github.gjong.advent` with subpackages for each year, prefixed with a 'y',
and additional subpackages for shared
logic: [common](solutions/src/main/java/com/github/gjong/advent/common), [algo](solutions/src/main/java/com/github/gjong/advent/algo),
and [geo](solutions/src/main/java/com/github/gjong/advent/geo).

```
advent-of-code/
├── solutions/
│   ├── src/
│   │   └── main/
│   │       └── java/
│   │           └── com/
│   │               └── github/
│   │                   └── gjong/
│   │                       └── advent/
│   │                           ├── years/
│   │                           │   ├── y2021/
│   │                           │   │   ├── Day01.java
│   │                           │   │   ├── Day02.java
│   │                           │   |   └── ...
│   │                           │   └── ...
│   │                           ├── common/
│   │                           │   └── CharGrid.java
│   │                           │   └── InputLoader.java
│   │                           │   └── Validator.java
│   │                           ├── algo/
│   │                           │   └── Algo.java
│   │                           ├── geo/
│   │                           |   └── Bounds.java
│   │                           |   └── Point.java
│   │                           |   └── Vector.java
│   │                           └── AdventOfCode.java
│   └── gradle.build.kts
├── README.md
├── gradle.build.kts
```

## Getting started

To run the solutions, you need to have Java 21 or higher installed on your machine.
The project is built using Gradle, so you can run the solutions using the following command:

```shell
echo "Running solutions for 2021"
./gradlew run --args="2021"

echo "Running solutions for all years"
./gradlew run
```

## Solution statistics

Each solution is timed using the `Instant.now()` method, and the results are displayed in the table below.
The time is measured in milliseconds and includes the time to read the input, run the solution, and validating the
output.

The measurements are taken on an AMD Ryzen 5 3600X CPU with 64GB of RAM.

:information_source: Click on the name of a solution to go to the related source file.

### 2021 - solutions

| Year | Day | Name                                                                                              | Part 1 | Part 2 | Assignment                                           |
|------|-----|---------------------------------------------------------------------------------------------------|--------|--------|------------------------------------------------------|
| 2021 | 01  | [Sonar Sweep](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day01.java)             | 14ms   | 14ms   | [instructions](https://adventofcode.com/2021/day/1)  |
| 2021 | 02  | [Dive!](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day02.java)                   | 6ms    | 1ms    | [instructions](https://adventofcode.com/2021/day/2)  |
| 2021 | 03  | [Binary Diagnostic](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day03.java)       | 2ms    | 8ms    | [instructions](https://adventofcode.com/2021/day/3)  |
| 2021 | 04  | [Giant Squid](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day04.java)             | 23ms   | 20ms   | [instructions](https://adventofcode.com/2021/day/4)  |
| 2021 | 05  | [Hydrothermal Venture](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day05.java)    | 71ms   | 22ms   | [instructions](https://adventofcode.com/2021/day/5)  |
| 2021 | 06  | [Lanternfish](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day06.java)             | 1ms    | 0ms    | [instructions](https://adventofcode.com/2021/day/6)  |
| 2021 | 07  | [The Treachery of Whales](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day07.java) | 3ms    | 8ms    | [instructions](https://adventofcode.com/2021/day/7)  |
| 2021 | 08  | [...](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day08.java)                     | 18ms   | 10ms   | [instructions](https://adventofcode.com/2021/day/8)  |
| 2021 | 09  | [Smoke Basin](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day09.java)             | 1ms    | 3ms    | [instructions](https://adventofcode.com/2021/day/9)  |
| 2021 | 10  | [Syntax Scoring](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day10.java)          | 6ms    | 3ms    | [instructions](https://adventofcode.com/2021/day/10) |
| 2021 | 11  | [Dumbo Octopus](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day11.java)           | 2ms    | 0ms    | [instructions](https://adventofcode.com/2021/day/11) |
| 2021 | 12  | [Passage Pathing](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day12.java)         | 24ms   | 717ms  | [instructions](https://adventofcode.com/2021/day/12) |
| 2021 | 13  | [Transparent Origami](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day13.java)     | 16ms   | 18ms   | [instructions](https://adventofcode.com/2021/day/13) |
| 2022 | 14  | [Regolith Reservoir](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day14.java)      | 7ms    | 94ms   | [instructions](https://adventofcode.com/2022/day/14) |
| 2021 | 15  | [Chiton](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day15.java)                  | 16ms   | 297ms  | [instructions](https://adventofcode.com/2021/day/15) |
| 2021 | 16  | [Packet Decoder](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day16.java)          | 11ms   | 5ms    | [instructions](https://adventofcode.com/2021/day/16) |
| 2021 | 17  | [Trick Shot](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day17.java)              | 20ms   | 18ms   | [instructions](https://adventofcode.com/2021/day/17) |
| 2021 | 18  | [Snailfish](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day18.java)               | 10ms   | 128ms  | [instructions](https://adventofcode.com/2021/day/18) |
| 2021 | 21  | [Dirac Dice](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day21.java)              | 2ms    | 9664ms | [instructions](https://adventofcode.com/2021/day/21) |

### 2022 - solutions

| Year | Day | Name                                                                                              | Part 1 | Part 2 | Assignment                                           |
|------|-----|---------------------------------------------------------------------------------------------------|--------|--------|------------------------------------------------------|
| 2022 | 01  | [Calorie Counting](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day01.java)        | 2ms    | 2ms    | [instructions](https://adventofcode.com/2022/day/1)  |
| 2022 | 02  | [Rock Paper Scissors](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day02.java)     | 1ms    | 2ms    | [instructions](https://adventofcode.com/2022/day/2)  |
| 2022 | 03  | [Rucksack Reorganization](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day03.java) | 6ms    | 4ms    | [instructions](https://adventofcode.com/2022/day/3)  |
| 2022 | 04  | [Camp Cleanup](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day04.java)            | 13ms   | 3ms    | [instructions](https://adventofcode.com/2022/day/4)  |
| 2022 | 05  | [Supply Stacks](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day05.java)           | 6ms    | 5ms    | [instructions](https://adventofcode.com/2022/day/5)  |
| 2022 | 06  | [Tuning Trouble](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day06.java)          | 3ms    | 2ms    | [instructions](https://adventofcode.com/2022/day/6)  |
| 2022 | 07  | [No Space Left On Device](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day07.java) | 7ms    | 7ms    | [instructions](https://adventofcode.com/2022/day/7)  |
| 2022 | 08  | [Treetop Tree House](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day08.java)      | 4ms    | 3ms    | [instructions](https://adventofcode.com/2022/day/8)  |
| 2022 | 09  | [Rope Bridge](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day09.java)             | 7ms    | 9ms    | [instructions](https://adventofcode.com/2022/day/9)  |
| 2022 | 10  | [Cathode-Ray Tube](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day10.java)        | 2ms    | 2ms    | [instructions](https://adventofcode.com/2022/day/10) |
| 2022 | 11  | [Monkey in the Middle](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day11.java)    | 9ms    | 107ms  | [instructions](https://adventofcode.com/2022/day/11) |
| 2022 | 12  | [Hill Climbing Algorithm](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day12.java) | 12ms   | 170ms  | [instructions](https://adventofcode.com/2022/day/12) |
| 2022 | 13  | [Distress Signal](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day13.java)         | 7ms    | 4ms    | [instructions](https://adventofcode.com/2022/day/13) |
| 2022 | 14  | [Regolith Reservoir](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day14.java)      | 8ms    | 95ms   | [instructions](https://adventofcode.com/2022/day/14) |

### 2023 - solutions

| Year | Day | Name                                                                                                      | Part 1 | Part 2 | Assignment                                           |
|------|-----|-----------------------------------------------------------------------------------------------------------|--------|--------|------------------------------------------------------|
| 2023 | 01  | [Trebuchet?!](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day01.java)                     | 3ms    | 13ms   | [instructions](https://adventofcode.com/2023/day/1)  |
| 2023 | 02  | [Cube Conundrum](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day02.java)                  | 1ms    | 1ms    | [instructions](https://adventofcode.com/2023/day/2)  |
| 2023 | 03  | [Gear Ratios](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day03.java)                     | 24ms   | 15ms   | [instructions](https://adventofcode.com/2023/day/3)  |
| 2023 | 04  | [Scratchcards](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day04.java)                    | 6ms    | 6ms    | [instructions](https://adventofcode.com/2023/day/4)  |
| 2023 | 05  | [If You Give A Seed A Fertilizer](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day05.java) | 3ms    | 3ms    | [instructions](https://adventofcode.com/2023/day/5)  |
| 2023 | 06  | [Wait For It](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day06.java)                     | 0ms    | 0ms    | [instructions](https://adventofcode.com/2023/day/6)  |
| 2023 | 07  | [Camel Cards](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day07.java)                     | 11ms   | 9ms    | [instructions](https://adventofcode.com/2023/day/7)  |
| 2023 | 08  | [Haunted Wasteland](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day08.java)               | 6ms    | 9ms    | [instructions](https://adventofcode.com/2023/day/8)  |
| 2023 | 09  | [Mirage Maintenance](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day09.java)              | 6ms    | 4ms    | [instructions](https://adventofcode.com/2023/day/9)  |
| 2023 | 10  | [Pipe Maze](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day10.java)                       | 54ms   | 0ms    | [instructions](https://adventofcode.com/2023/day/10) |
| 2023 | 11  | [Cosmic Expansion](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day11.java)                | 19ms   | 5ms    | [instructions](https://adventofcode.com/2023/day/11) |
| 2023 | 12  | [Hot Springs](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day12.java)                     | 16ms   | 76ms   | [instructions](https://adventofcode.com/2023/day/12) |
| 2023 | 13  | [Point of Incidence](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day13.java)              | 2ms    | 0ms    | [instructions](https://adventofcode.com/2023/day/13) |
| 2023 | 14  | [Parabolic Reflector Dish](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day14.java)        | 26ms   | 1651ms | [instructions](https://adventofcode.com/2023/day/14) |
| 2023 | 15  | [Lens Library](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day15.java)                    | 1ms    | 5ms    | [instructions](https://adventofcode.com/2023/day/15) |
| 2023 | 16  | [The Floor Will Be Lava](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day16.java)          | 11ms   | 464ms  | [instructions](https://adventofcode.com/2023/day/16) |
| 2023 | 17  | [Clumsy Crucible](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day17.java)                 | 133ms  | 255ms  | [instructions](https://adventofcode.com/2023/day/17) |

### 2024 - solutions

| Year | Day | Name                                                                                         | Parsing | Part 1 | Part 2 | Assignment                                          |
|------|-----|----------------------------------------------------------------------------------------------|---------|--------|--------|-----------------------------------------------------|
| 2024 | 01  | [Historian Hysteria](solutions/src/main/java/com/github/gjong/advent/years/y2024/Day01.java) | 24ms    | 9ms    | 3ms    | [instructions](https://adventofcode.com/2024/day/1) |
| 2024 | 02  | [Red-Nosed Reports](solutions/src/main/java/com/github/gjong/advent/years/y2024/Day02.java)  | 0ms     | 12ms   | 7ms    | [instructions](https://adventofcode.com/2024/day/2) |

