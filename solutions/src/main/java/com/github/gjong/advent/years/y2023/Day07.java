package com.github.gjong.advent.years.y2023;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Day(day = 7, year = 2023)
public class Day07 implements DaySolver {

    private final InputLoader inputLoader = new InputLoader(2023, 7);
    private final Validator validator = new Validator(2023, 7);

    enum HandResult {
        FIVE_OF_A_KIND(7),
        FOUR_OF_A_KIND(6),
        FULL_HOUSE(5),
        THREE_OF_A_KIND(4),
        TWO_PAIR(3),
        ONE_PAIR(2),
        HIGH_CARD(1);
        private final int score;

        HandResult(int score) {
            this.score = score;
        }
    }

    static class Card implements Comparable<Card> {
        private final int strength;

        Card(Character card) {
            this.strength = switch(card) {
                case 'A' -> 14;
                case 'K' -> 13;
                case 'Q' -> 12;
                case 'J' -> 11;
                case 'T' -> 10;
                case 'Z' -> 1;
                default -> Integer.parseInt(String.valueOf(card));
            };
        }

        @Override
        public int compareTo(Card o) {
            return this.strength - o.strength;
        }
    }

    record Move(Hand hand, int bed) {
    }

    record Hand(String cards, int score) implements Comparable<Hand> {
        @Override
        public int compareTo(Hand o) {
            for (var i = 0; i < 5; i++) {
                var thisCard = new Card(cards.charAt(i));
                var otherCard = new Card(o.cards.charAt(i));

                if (thisCard.compareTo(otherCard) != 0) {
                    return thisCard.compareTo(otherCard);
                }
            }
            return 0;
        }
    }

    @Override
    public void part1() {
        var list = inputLoader.splitOnNewLine()
                .map(line -> readLine(line, this::computeNormalHand))
                .collect(Collectors.groupingBy(m -> m.hand().score(), Collectors.toList()));

        validator.part1(computeScore(list));
    }

    @Override
    public void part2() {
        var list = inputLoader.splitOnNewLine()
                .map(line -> readLine(line, this::computeJokerHand))
                .map(move -> new Move(
                        new Hand(
                                move.hand.cards.replaceAll("J", "Z"),
                                move.hand.score),
                        move.bed()))
                .collect(Collectors.groupingBy(m -> m.hand().score(), Collectors.toList()));

        validator.part2(computeScore(list));
    }

    private long computeScore(Map<Integer, List<Move>> list) {
        var score = 0L;
        var rank = 1;
        for (var entry : list.entrySet()) {
            if (entry.getValue().size() == 1) {
                score += (long) entry.getValue().get(0).bed() * rank;
                rank++;
            } else {
                var sorted = entry.getValue()
                        .stream()
                        .sorted(Comparator.comparing(Move::hand))
                        .toList();
                for (var move : sorted) {
                    score += (long) move.bed() * rank;
                    rank++;
                }
            }
        }
        return score;
    }

    private int computeNormalHand(String hand) {
        var groupedHand = Arrays.stream(hand.split(""))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        var handScore = HandResult.HIGH_CARD;
        for (var count : groupedHand.values()) {
            handScore = switch (count.intValue()) {
                case 5 -> HandResult.FIVE_OF_A_KIND;
                case 4 -> HandResult.FOUR_OF_A_KIND;
                case 3 -> handScore == HandResult.ONE_PAIR
                        ? HandResult.FULL_HOUSE
                        : HandResult.THREE_OF_A_KIND;
                case 2 -> handScore == HandResult.ONE_PAIR
                        ? HandResult.TWO_PAIR
                        : handScore == HandResult.THREE_OF_A_KIND
                        ? HandResult.FULL_HOUSE : HandResult.ONE_PAIR;
                default -> handScore;
            };
        }
        return handScore.score;
    }

    private int computeJokerHand(String hand) {
        var handWithoutJokers = hand.replaceAll("J", "");
        var handScore = computeNormalHand(handWithoutJokers);
        var nrJokers = hand.length() - handWithoutJokers.length();

        if (nrJokers > 0) {
            if (handScore == HandResult.FOUR_OF_A_KIND.score) {
                return HandResult.FIVE_OF_A_KIND.score;
            }

            if (handScore == HandResult.THREE_OF_A_KIND.score) {
                return HandResult.FULL_HOUSE.score + nrJokers;
            }

            if (handScore == HandResult.TWO_PAIR.score) {
                return HandResult.FULL_HOUSE.score;
            }

            if (handScore == HandResult.ONE_PAIR.score) {
                // 2 cards are the same
                if (nrJokers == 1) {
                    return HandResult.THREE_OF_A_KIND.score;
                }
                if (nrJokers == 2) {
                    return HandResult.FOUR_OF_A_KIND.score;
                }
                if (nrJokers == 3) {
                    return HandResult.FIVE_OF_A_KIND.score;
                }
            }

            if (handScore == HandResult.HIGH_CARD.score) {
                if (nrJokers == 1) {
                    return HandResult.ONE_PAIR.score;
                }
                if (nrJokers == 2) {
                    return HandResult.THREE_OF_A_KIND.score;
                }
                if (nrJokers == 3) {
                    return HandResult.FOUR_OF_A_KIND.score;
                }
                if (nrJokers == 4 || nrJokers == 5) {
                    return HandResult.FIVE_OF_A_KIND.score;
                }
            }

            throw new IllegalStateException("Unexpected hand score: " + handScore + " with jokers: " + nrJokers);
        }

        return handScore;
    }

    private Move readLine(String line, Function<String, Integer> handRanker) {
        var hand = line.substring(0, 5);
        var bed = Integer.parseInt(line.substring(6));

        return new Move(new Hand(hand, handRanker.apply(hand)), bed);
    }
}
