package com.github.gjong.advent.years.y2021;

import com.github.gjong.advent.Day;
import com.github.gjong.advent.DaySolver;
import com.github.gjong.advent.common.InputLoader;
import com.github.gjong.advent.common.Validator;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import static java.lang.Integer.parseInt;

@Day(day = 21, year = 2021, name = "Dirac Dice")
public class Day21 implements DaySolver {
    private final InputLoader inputLoader = DayLoader.inputDay21();
    private final Validator validator = DayLoader.validatorDay21();

    private static final List<QuantumDice> DICE_OPTIONS = List.of(
            new QuantumDice(() -> 3, 1),
            new QuantumDice(() -> 4, 3),
            new QuantumDice(() -> 5, 6),
            new QuantumDice(() -> 6, 7),
            new QuantumDice(() -> 7, 6),
            new QuantumDice(() -> 8, 3),
            new QuantumDice(() -> 9, 1));


    @Override
    public void part1() {
        var lines = inputLoader.splitOnNewLine().toList();
        var pawn1Position = parseInt(lines.getFirst().substring(28));
        var pawn2Position = parseInt(lines.getLast().substring(28));

        var lowestScore = new AtomicInteger();
        var totalThrows = new AtomicInteger();
        var diracDice = new DiracDice.SimpleDice();
        new DiracGame(player -> player.score() >= 1000) {
            @Override
            protected List<RoundResult> computeRound(RoundResult previousRound) {
                totalThrows.addAndGet(3);
                var nextState = previousRound.gameState().throwDice(diracDice);
                return List.of(new RoundResult(nextState, 1));
            }

            @Override
            protected void wonGame(RoundResult result) {
                lowestScore.set(Math.min(
                        result.gameState().players()[0].score(),
                        result.gameState().players()[1].score()));
            }
        }
                .setupBoard(new int[]{pawn1Position, pawn2Position})
                .playGame();

        validator.part1(totalThrows.get() * lowestScore.get());
    }

    @Override
    public void part2() {
        var lines = inputLoader.splitOnNewLine().toList();
        var pawn1Position = parseInt(lines.getFirst().substring(28));
        var pawn2Position = parseInt(lines.getLast().substring(28));

        var player1Wins = new AtomicLong();
        var player2Wins = new AtomicLong();

        new DiracGame(player -> player.score() >= 21) {
            @Override
            protected List<RoundResult> computeRound(RoundResult previousRound) {
                var originalState = previousRound.gameState();
                return DICE_OPTIONS.stream()
                        .map(dice -> new RoundResult(
                                originalState.throwDice(dice.diceValues),
                                previousRound.weight() * dice.odds))
                        .toList();
            }

            @Override
            protected void wonGame(RoundResult result) {
                if (result.gameState().players()[0].score() >= 21) {
                    player1Wins.addAndGet(result.weight());
                } else {
                    player2Wins.addAndGet(result.weight());
                }
            }
        }
                .setupBoard(new int[]{pawn1Position, pawn2Position})
                .playGame();

        validator.part2(Math.max(player1Wins.get(), player2Wins.get()));
    }

    interface DiracDice {

        int roll();

        class SimpleDice implements DiracDice {
            private int currentValue;

            @Override
            public int roll() {
                return increase() + increase() + increase();
            }

            private int increase() {
                currentValue++;
                if (currentValue > 100) {
                    currentValue -= 100;
                }
                return currentValue;
            }
        }
    }

    private record QuantumDice(DiracDice diceValues, int odds) {
    }

    private abstract class DiracGame {

        private final Predicate<Player> wonRule;
        private GameState startState;

        public DiracGame(Predicate<Player> wonRule) {
            this.wonRule = wonRule;
        }

        public DiracGame setupBoard(int[] startPositionPlayers) {
            var players = new Player[startPositionPlayers.length];
            for (var index = 0; index < startPositionPlayers.length; index++) {
                players[index] = new Player(startPositionPlayers[index] - 1, 0);
            }
            this.startState = new GameState(0, players);
            return this;
        }

        public void playGame() {
            computeGameState(new RoundResult(startState, 1));
        }

        private void computeGameState(RoundResult currentState) {
            var hasWinner = computeWinningPlayer(currentState.gameState);
            if (hasWinner.isPresent()) {
                wonGame(currentState);
            } else {
                computeRound(currentState)
                        .forEach(this::computeGameState);
            }
        }

        protected abstract void wonGame(RoundResult result);

        protected abstract List<RoundResult> computeRound(RoundResult previousRound);

        protected Optional<Player> computeWinningPlayer(GameState gameState) {
            for (var player : gameState.players()) {
                if (wonRule.test(player)) {
                    return Optional.of(player);
                }
            }

            return Optional.empty();
        }

        public record Player(int position, int score) {
            public Player move(int positions) {
                var newPosition = (position + positions) % 10;
                var newScore = score + (newPosition + 1);
                return new Player(newPosition, newScore);
            }
        }

        public record RoundResult(GameState gameState, long weight) {
        }

        public record GameState(int playerTurn, Player[] players) {
            public GameState throwDice(DiracDice dice) {
                var diceResults = dice.roll();
                var updatePlayers = Arrays.copyOf(players, players.length);
                updatePlayers[playerTurn] = players[playerTurn].move(diceResults);
                return new GameState((playerTurn + 1) % players.length, updatePlayers);
            }
        }
    }
}
