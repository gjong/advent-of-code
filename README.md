# Advent of Code Solutions

Welcome to the repository containing my solutions for [Advent of Code](https://adventofcode.com/), an annual coding
event that takes place every December.
Each day, a new programming challenge is released, and this repository contains my attempts to solve those challenges.

## Repository structure

The repository is organized as a Java project with a submodule named solutions. The solutions module includes a package
com.github.gjong.advent with subpackages for each year, prefixed with a 'y', and additional subpackages for shared
logic: common, algo, and geo.

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
The time is measured in milliseconds and includes the time to read the input, run the solution, and validating the output.

The measurements are taken on an AMD Ryzen 5 3600X CPU with 64GB of RAM.

### 2022 - solutions

| Year | Day | Name                    | Part 1 | Part 2 | Assignment                                           |
|------|-----|-------------------------|--------|--------|------------------------------------------------------|
| 2022 | 01  | Calorie Counting        | 3ms    | 3ms    | [instructions](https://adventofcode.com/2022/day/1)  |
| 2022 | 02  | Rock Paper Scissors     | 2ms    | 2ms    | [instructions](https://adventofcode.com/2022/day/2)  |
| 2022 | 03  | Rucksack Reorganization | 9ms    | 5ms    | [instructions](https://adventofcode.com/2022/day/3)  |
| 2022 | 04  | Camp Cleanup            | 15ms   | 4ms    | [instructions](https://adventofcode.com/2022/day/4)  |
| 2022 | 05  | Supply Stacks           | 10ms   | 10ms   | [instructions](https://adventofcode.com/2022/day/5)  |
| 2022 | 06  | Tuning Trouble          | 6ms    | 5ms    | [instructions](https://adventofcode.com/2022/day/6)  |
| 2022 | 07  | No Space Left On Device | 9ms    | 10ms   | [instructions](https://adventofcode.com/2022/day/7)  |
| 2022 | 08  | Treetop Tree House      | 4ms    | 4ms    | [instructions](https://adventofcode.com/2022/day/8)  |
| 2022 | 09  | Rope Bridge             | 25ms   | 12ms   | [instructions](https://adventofcode.com/2022/day/9)  |
| 2022 | 10  | Cathode-Ray Tube        | 2ms    | 1ms    | [instructions](https://adventofcode.com/2022/day/10) |
| 2022 | 11  | Monkey in the Middle    | 8ms    | 120ms  | [instructions](https://adventofcode.com/2022/day/11) |
| 2022 | 12  | Hill Climbing Algorithm | 10ms   | 178ms  | [instructions](https://adventofcode.com/2022/day/12) |
| 2022 | 13  | Distress Signal         | 7ms    | 5ms    | [instructions](https://adventofcode.com/2022/day/13) |
| 2022 | 14  | Regolith Reservoir      | 10ms   | 92ms   | [instructions](https://adventofcode.com/2022/day/14) |

### 2023 - solutions

| Year | Day | Name                            | Part 1 | Part 2 | Assignment                                           |
|------|-----|---------------------------------|--------|--------|------------------------------------------------------|
| 2023 | 01  | Trebuchet?!                     | 5ms    | 16ms   | [instructions](https://adventofcode.com/2023/day/1)  |
| 2023 | 02  | Cube Conundrum                  | 2ms    | 0ms    | [instructions](https://adventofcode.com/2023/day/2)  |
| 2023 | 03  | Gear Ratios                     | 31ms   | 16ms   | [instructions](https://adventofcode.com/2023/day/3)  |
| 2023 | 04  | Scratchcards                    | 7ms    | 6ms    | [instructions](https://adventofcode.com/2023/day/4)  |
| 2023 | 05  | If You Give A Seed A Fertilizer | 4ms    | 4ms    | [instructions](https://adventofcode.com/2023/day/5)  |
| 2023 | 06  | Wait For It                     | 1ms    | 0ms    | [instructions](https://adventofcode.com/2023/day/6)  |
| 2023 | 07  | Camel Cards                     | 12ms   | 10ms   | [instructions](https://adventofcode.com/2023/day/7)  |
| 2023 | 08  | Haunted Wasteland               | 7ms    | 8ms    | [instructions](https://adventofcode.com/2023/day/8)  |
| 2023 | 09  | Mirage Maintenance              | 9ms    | 4ms    | [instructions](https://adventofcode.com/2023/day/9)  |
| 2023 | 10  | Pipe Maze                       | 64ms   | 0ms    | [instructions](https://adventofcode.com/2023/day/10) |
| 2023 | 11  | Cosmic Expansion                | 20ms   | 5ms    | [instructions](https://adventofcode.com/2023/day/11) |
| 2023 | 12  | Hot Springs                     | 15ms   | 89ms   | [instructions](https://adventofcode.com/2023/day/12) |
| 2023 | 13  | Point of Incidence              | 2ms    | 1ms    | [instructions](https://adventofcode.com/2023/day/13) |
| 2023 | 14  | Parabolic Reflector Dish        | 30ms   | 1754ms | [instructions](https://adventofcode.com/2023/day/14) |
| 2023 | 15  | Lens Library                    | 0ms    | 5ms    | [instructions](https://adventofcode.com/2023/day/15) |
| 2023 | 16  | The Floor Will Be Lava          | 13ms   | 364ms  | [instructions](https://adventofcode.com/2023/day/16) |
| 2023 | 17  | Clumsy Crucible                 | 160ms  | 282ms  | [instructions](https://adventofcode.com/2023/day/17) |