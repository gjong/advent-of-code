package com.github.gjong.advent.years.y2023;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Day(day = 4, year = 2023)
public class Day04 implements DaySolver {

    record Card(int hand, List<Integer> winningNumbers, List<Integer> yourNumbers) {
        public long score() {
            return (long) Math.pow(2, numberMatches() - 1);
        }

        public long numberMatches() {
            return yourNumbers.stream()
                    .filter(winningNumbers::contains)
                    .count();
        }
    }

    static class CountedCards {
        private final Card card;
        private int count;

        public CountedCards(Card card) {
            this.card = card;
            this.count = 1;
        }

        public void increment(int amount) {
            this.count += amount;
        }
    }

    private final InputLoader inputLoader = new InputLoader(2023, 4);
    private final Validator validator = new Validator(2023, 4);

    @Override
    public void part1() {
        var answer = inputLoader.splitOnNewLine()
                .map(this::parseCard)
                .mapToLong(Card::score)
                .sum();
        validator.part1(answer);
    }

    @Override
    public void part2() {
        var cardInput = inputLoader.splitOnNewLine()
                .map(this::parseCard)
                .map(CountedCards::new)
                .toList();

        for (var i = 0; i < cardInput.size(); i++) {
            var card = cardInput.get(i);
            if (card.card.numberMatches() > 0) {
                IntStream.range(i + 1, (int) (i + card.card.numberMatches() + 1))
                        .forEach(index -> cardInput.get(index).increment(card.count));
            }
        }

        var answer =  cardInput.stream()
                .mapToLong(card -> card.count)
                .sum();
        validator.part2(answer);
    }

    private Card parseCard(String line) {
        var cardSplit = line.indexOf(":");
        var drawSplit = line.indexOf("|");
        // extract the card number, the winning numbers, and your numbers
        var cardNumber = Integer.parseInt(line.substring(5, cardSplit).trim());

        var winningNumbers = Arrays.stream(line.substring(cardSplit + 1, drawSplit)
                        .split("\\s"))
                .filter(number -> !number.isBlank())
                .map(nr -> Integer.parseInt(nr.trim()))
                .toList();
        var myNumbers = Arrays.stream(line.substring(drawSplit + 1).split("\\s"))
                .filter(number -> !number.isBlank())
                .map(nr -> Integer.parseInt(nr.trim()))
                .toList();

        return new Card(
                cardNumber,
                winningNumbers,
                myNumbers);
    }
}
