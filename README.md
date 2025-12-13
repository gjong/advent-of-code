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
./gradlew run --args="-y 2021"

echo "Running solutions for all years"
./gradlew run
```

The CLI supports the following arguments:

```
[cli] Execute using the following options:
[cli]       -y <year>  Execute only the provided year.
[cli]       -b         Run in benchmark mode.
[cli]       -r         Number of runs in benchmark mode, default 5.
```

## Solution statistics

Each solution is timed using the `Instant.now()` method, and the results are displayed in the table below.
The time is measured in milliseconds and includes the time to read the input, run the solution, and validating the
output.

The benchmarking is achieved by running the following command:

```shell
echo "Running solutions for 2021"
./gradlew run --args="-y 2021 -b -r 100"
```

The measurements are taken on an AMD Ryzen 5 3600X CPU with 64GB of RAM.

:information_source: Click on the name of a solution to go to the related source file.

### 2021 - solutions

| Year | Day | Name                                                                                              | Parsing | Part 1 | Part 2  | Assignment                                           |
|------|-----|---------------------------------------------------------------------------------------------------|---------|--------|---------|------------------------------------------------------|
| 2021 | 01  | [Sonar Sweep](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day01.java)             | -       | 828μs  | 946μs   | [instructions](https://adventofcode.com/2021/day/1)  |
| 2021 | 02  | [Dive!](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day02.java)                   | -       | 1010μs | 653μs   | [instructions](https://adventofcode.com/2021/day/2)  |
| 2021 | 03  | [Binary Diagnostic](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day03.java)       | -       | 484μs  | 859μs   | [instructions](https://adventofcode.com/2021/day/3)  |
| 2021 | 04  | [Giant Squid](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day04.java)             | -       | 2ms    | 3ms     | [instructions](https://adventofcode.com/2021/day/4)  |
| 2021 | 05  | [Hydrothermal Venture](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day05.java)    | 11μs    | 13ms   | 3ms     | [instructions](https://adventofcode.com/2021/day/5)  |
| 2021 | 06  | [Lanternfish](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day06.java)             | -       | 162μs  | 190μs   | [instructions](https://adventofcode.com/2021/day/6)  |
| 2021 | 07  | [The Treachery of Whales](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day07.java) | -       | 932μs  | 849μs   | [instructions](https://adventofcode.com/2021/day/7)  |
| 2021 | 08  | [](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day08.java)                        | -       | 2ms    | 2ms     | [instructions](https://adventofcode.com/2021/day/8)  |
| 2021 | 09  | [Smoke Basin](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day09.java)             | -       | 342μs  | 699μs   | [instructions](https://adventofcode.com/2021/day/9)  |
| 2021 | 10  | [Syntax Scoring](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day10.java)          | -       | 482μs  | 524μs   | [instructions](https://adventofcode.com/2021/day/10) |
| 2021 | 11  | [Dumbo Octopus](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day11.java)           | -       | 354μs  | 353μs   | [instructions](https://adventofcode.com/2021/day/11) |
| 2021 | 12  | [Passage Pathing](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day12.java)         | -       | 5ms    | 540ms   | [instructions](https://adventofcode.com/2021/day/12) |
| 2021 | 13  | [Transparent Origami](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day13.java)     | -       | 4ms    | 5ms     | [instructions](https://adventofcode.com/2021/day/13) |
| 2021 | 14  | [Extended Polymerization](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day14.java) | -       | 7ms    | 1194μs  | [instructions](https://adventofcode.com/2021/day/14) |
| 2021 | 15  | [Chiton](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day15.java)                  | -       | 2ms    | 279ms   | [instructions](https://adventofcode.com/2021/day/15) |
| 2021 | 16  | [Packet Decoder](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day16.java)          | -       | 1ms    | 1ms     | [instructions](https://adventofcode.com/2021/day/16) |
| 2021 | 17  | [Trick Shot](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day17.java)              | 10μs    | 5ms    | 5ms     | [instructions](https://adventofcode.com/2021/day/17) |
| 2021 | 18  | [Snailfish](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day18.java)               | -       | 2ms    | 81ms    | [instructions](https://adventofcode.com/2021/day/18) |
| 2021 | 21  | [Dirac Dice](solutions/src/main/java/com/github/gjong/advent/years/y2021/Day21.java)              | -       | 241μs  | 11560ms | [instructions](https://adventofcode.com/2021/day/21) |

### 2022 - solutions

| Year | Day | Name                                                                                              | Parsing | Part 1 | Part 2 | Assignment                                           |
|------|-----|---------------------------------------------------------------------------------------------------|---------|--------|--------|------------------------------------------------------|
| 2022 | 01  | [Calorie Counting](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day01.java)        | -       | 1078μs | 636μs  | [instructions](https://adventofcode.com/2022/day/1)  |
| 2022 | 02  | [Rock Paper Scissors](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day02.java)     | -       | 478μs  | 620μs  | [instructions](https://adventofcode.com/2022/day/2)  |
| 2022 | 03  | [Rucksack Reorganization](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day03.java) | -       | 1187μs | 1ms    | [instructions](https://adventofcode.com/2022/day/3)  |
| 2022 | 04  | [Camp Cleanup](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day04.java)            | -       | 1ms    | 1ms    | [instructions](https://adventofcode.com/2022/day/4)  |
| 2022 | 05  | [Supply Stacks](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day05.java)           | -       | 952μs  | 1ms    | [instructions](https://adventofcode.com/2022/day/5)  |
| 2022 | 06  | [Tuning Trouble](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day06.java)          | -       | 701μs  | 1031μs | [instructions](https://adventofcode.com/2022/day/6)  |
| 2022 | 07  | [No Space Left On Device](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day07.java) | -       | 833μs  | 1137μs | [instructions](https://adventofcode.com/2022/day/7)  |
| 2022 | 08  | [Treetop Tree House](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day08.java)      | -       | 1ms    | 1ms    | [instructions](https://adventofcode.com/2022/day/8)  |
| 2022 | 09  | [Rope Bridge](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day09.java)             | -       | 1ms    | 1ms    | [instructions](https://adventofcode.com/2022/day/9)  |
| 2022 | 10  | [Cathode-Ray Tube](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day10.java)        | -       | 531μs  | 257μs  | [instructions](https://adventofcode.com/2022/day/10) |
| 2022 | 11  | [Monkey in the Middle](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day11.java)    | -       | 603μs  | 43ms   | [instructions](https://adventofcode.com/2022/day/11) |
| 2022 | 12  | [Hill Climbing Algorithm](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day12.java) | -       | 1ms    | 90ms   | [instructions](https://adventofcode.com/2022/day/12) |
| 2022 | 13  | [Distress Signal](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day13.java)         | -       | 2ms    | 2ms    | [instructions](https://adventofcode.com/2022/day/13) |
| 2022 | 14  | [Regolith Reservoir](solutions/src/main/java/com/github/gjong/advent/years/y2022/Day14.java)      | -       | 1ms    | 65ms   | [instructions](https://adventofcode.com/2022/day/14) |

### 2023 - solutions

| Year | Day | Name                                                                                                      | Parsing | Part 1 | Part 2 | Assignment                                           |
|------|-----|-----------------------------------------------------------------------------------------------------------|---------|--------|--------|------------------------------------------------------|
| 2023 | 01  | [Trebuchet?!](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day01.java)                     | -       | 986μs  | 3ms    | [instructions](https://adventofcode.com/2023/day/1)  |
| 2023 | 02  | [Cube Conundrum](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day02.java)                  | -       | 709μs  | 747μs  | [instructions](https://adventofcode.com/2023/day/2)  |
| 2023 | 03  | [Gear Ratios](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day03.java)                     | -       | 5ms    | 3ms    | [instructions](https://adventofcode.com/2023/day/3)  |
| 2023 | 04  | [Scratchcards](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day04.java)                    | -       | 1ms    | 1ms    | [instructions](https://adventofcode.com/2023/day/4)  |
| 2023 | 05  | [If You Give A Seed A Fertilizer](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day05.java) | -       | 789μs  | 977μs  | [instructions](https://adventofcode.com/2023/day/5)  |
| 2023 | 06  | [Wait For It](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day06.java)                     | -       | 180μs  | 128μs  | [instructions](https://adventofcode.com/2023/day/6)  |
| 2023 | 07  | [Camel Cards](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day07.java)                     | -       | 2ms    | 2ms    | [instructions](https://adventofcode.com/2023/day/7)  |
| 2023 | 08  | [Haunted Wasteland](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day08.java)               | -       | 2ms    | 2ms    | [instructions](https://adventofcode.com/2023/day/8)  |
| 2023 | 09  | [Mirage Maintenance](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day09.java)              | -       | 1ms    | 1ms    | [instructions](https://adventofcode.com/2023/day/9)  |
| 2023 | 10  | [Pipe Maze](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day10.java)                       | -       | 15ms   | -      | [instructions](https://adventofcode.com/2023/day/10) |
| 2023 | 11  | [Cosmic Expansion](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day11.java)                | -       | 2ms    | 1ms    | [instructions](https://adventofcode.com/2023/day/11) |
| 2023 | 12  | [Hot Springs](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day12.java)                     | -       | 3ms    | 24ms   | [instructions](https://adventofcode.com/2023/day/12) |
| 2023 | 13  | [Point of Incidence](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day13.java)              | -       | 661μs  | 490μs  | [instructions](https://adventofcode.com/2023/day/13) |
| 2023 | 14  | [Parabolic Reflector Dish](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day14.java)        | -       | 5ms    | 1539ms | [instructions](https://adventofcode.com/2023/day/14) |
| 2023 | 15  | [Lens Library](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day15.java)                    | -       | 369μs  | 1ms    | [instructions](https://adventofcode.com/2023/day/15) |
| 2023 | 16  | [The Floor Will Be Lava](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day16.java)          | -       | 2ms    | 65ms   | [instructions](https://adventofcode.com/2023/day/16) |
| 2023 | 17  | [Clumsy Crucible](solutions/src/main/java/com/github/gjong/advent/years/y2023/Day17.java)                 | -       | 80ms   | 239ms  | [instructions](https://adventofcode.com/2023/day/17) |

### 2024 - solutions

| Year | Day | Name                                                                                             | Parsing | Part 1 | Part 2  | Assignment                                           |
|------|-----|--------------------------------------------------------------------------------------------------|---------|--------|---------|------------------------------------------------------|
| 2024 | 01  | [Historian Hysteria](solutions/src/main/java/com/github/gjong/advent/years/y2024/Day01.java)     | 20ms    | 221μs  | 269μs   | [instructions](https://adventofcode.com/2024/day/1)  |
| 2024 | 02  | [Red-Nosed Reports](solutions/src/main/java/com/github/gjong/advent/years/y2024/Day02.java)      | -       | 1ms    | 1ms     | [instructions](https://adventofcode.com/2024/day/2)  |
| 2024 | 03  | [Mull it over](solutions/src/main/java/com/github/gjong/advent/years/y2024/Day03.java)           | -       | 606μs  | 394μs   | [instructions](https://adventofcode.com/2024/day/3)  |
| 2024 | 04  | [Ceres Search](solutions/src/main/java/com/github/gjong/advent/years/y2024/Day04.java)           | -       | 2ms    | 958μs   | [instructions](https://adventofcode.com/2024/day/4)  |
| 2024 | 05  | [Print Queue](solutions/src/main/java/com/github/gjong/advent/years/y2024/Day05.java)            | 5ms     | 2ms    | 2ms     | [instructions](https://adventofcode.com/2024/day/5)  |
| 2024 | 06  | [Guard Gallivant](solutions/src/main/java/com/github/gjong/advent/years/y2024/Day06.java)        | 1119μs  | 27ms   | 10015ms | [instructions](https://adventofcode.com/2024/day/6)  |
| 2024 | 07  | [Bridge Repair](solutions/src/main/java/com/github/gjong/advent/years/y2024/Day07.java)          | -       | 32ms   | 6863ms  | [instructions](https://adventofcode.com/2024/day/7)  |
| 2024 | 08  | [Resonant Collinearity](solutions/src/main/java/com/github/gjong/advent/years/y2024/Day08.java)  | 1087μs  | 51μs   | 111μs   | [instructions](https://adventofcode.com/2024/day/8)  |
| 2024 | 09  | [Disk Fragmenter](solutions/src/main/java/com/github/gjong/advent/years/y2024/Day09.java)        | -       | 1065μs | 89ms    | [instructions](https://adventofcode.com/2024/day/9)  |
| 2024 | 10  | [Hoof it](solutions/src/main/java/com/github/gjong/advent/years/y2024/Day10.java)                | -       | 664μs  | 937μs   | [instructions](https://adventofcode.com/2024/day/10) |
| 2024 | 11  | [Plutonian Pebbles](solutions/src/main/java/com/github/gjong/advent/years/y2024/Day11.java)      | -       | 487μs  | 32ms    | [instructions](https://adventofcode.com/2024/day/11) |
| 2024 | 12  | [Garden Groups](solutions/src/main/java/com/github/gjong/advent/years/y2024/Day12.java)          | -       | 6ms    | 10ms    | [instructions](https://adventofcode.com/2024/day/12) |
| 2024 | 13  | [Claw Contraption](solutions/src/main/java/com/github/gjong/advent/years/y2024/Day13.java)       | 11ms    | 24μs   | 67μs    | [instructions](https://adventofcode.com/2024/day/13) |
| 2024 | 14  | [Restroom Redoubt](solutions/src/main/java/com/github/gjong/advent/years/y2024/Day14.java)       | -       | 19ms   | 171ms   | [instructions](https://adventofcode.com/2024/day/14) |
| 2024 | 17  | [Chronospatial Computer](solutions/src/main/java/com/github/gjong/advent/years/y2024/Day17.java) | 1000μs  | 110μs  | 1ms     | [instructions](https://adventofcode.com/2024/day/17) |
| 2024 | 18  | [RAM Run](solutions/src/main/java/com/github/gjong/advent/years/y2024/Day18.java)                | 4ms     | 43ms   | 2189ms  | [instructions](https://adventofcode.com/2024/day/18) |
| 2024 | 19  | [Linen Layout](solutions/src/main/java/com/github/gjong/advent/years/y2024/Day19.java)           | -       | 11ms   | 56ms    | [instructions](https://adventofcode.com/2024/day/19) |
| 2024 | 20  | [Race Condition](solutions/src/main/java/com/github/gjong/advent/years/y2024/Day20.java)         | -       | 71ms   | 336ms   | [instructions](https://adventofcode.com/2024/day/20) |

### 2025 - solutions

| Year  | Day  | Name                                | Parsing | Part 1  | Part 2  | Assignment                          |
|------|-----|--------------------------------------------------------------------------------------------------|---------|--------|---------|------------------------------------------------------|
|  2025 |  01  | [Secret Entrance](solutions/src/main/java/com/github/gjong/advent/years/y2025/Day01.java) | 604μs   | 185μs   | 149μs   | [instructions](https://adventofcode.com/2025/day/1) |
|  2025 |  02  | [Gift Shop](solutions/src/main/java/com/github/gjong/advent/years/y2025/Day02.java) | 394μs   | 396μs   | 1ms     | [instructions](https://adventofcode.com/2025/day/2) |
