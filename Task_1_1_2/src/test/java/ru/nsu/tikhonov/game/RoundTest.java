package ru.nsu.tikhonov.game;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Deque;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import ru.nsu.tikhonov.card.Card;
import ru.nsu.tikhonov.card.Rank;
import ru.nsu.tikhonov.card.Suit;
import ru.nsu.tikhonov.deck.Deck;
import ru.nsu.tikhonov.player.Dealer;
import ru.nsu.tikhonov.player.Player;
import ru.nsu.tikhonov.util.InputReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тестирует Round.
 */
class RoundTest {

    private static final InputStream ORIGINAL_SYSTEM_IN = System.in;

    /**
     * Победа игрока при блэкджеке.
     */
    @Test
    void playerBlackjackShouldWinRound() {
        Player player = new Player("Игрок");
        Dealer dealer = new Dealer();

        TestDeck deck = new TestDeck(
                new Card(Suit.HEARTS, Rank.ACE),
                new Card(Suit.CLUBS, Rank.TEN),
                new Card(Suit.SPADES, Rank.KING),
                new Card(Suit.DIAMONDS, Rank.NINE));

        Round round = new Round(
                player,
                dealer,
                deck,
                new InputReader());

        RoundResult result = round.play();

        assertEquals(RoundResult.PLAYER_WIN, result);
    }

    /**
     * Победа дилера при блэкджеке.
     */
    @Test
    void dealerBlackjackShouldWinRound() {
        Player player = new Player("Игрок");
        Dealer dealer = new Dealer();

        TestDeck deck = new TestDeck(
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.CLUBS, Rank.ACE),
                new Card(Suit.SPADES, Rank.NINE),
                new Card(Suit.DIAMONDS, Rank.KING));

        Round round = new Round(
                player,
                dealer,
                deck,
                new InputReader());

        RoundResult result = round.play();

        assertEquals(RoundResult.DEALER_WIN, result);
    }

    /**
     * Проигрыш игрока при переборе во время его хода.
     */
    @Test
    void playerBustShouldLoseRound() {
        Player player = new Player("Игрок");
        Dealer dealer = new Dealer();

        TestDeck deck = new TestDeck(
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.CLUBS, Rank.TEN),
                new Card(Suit.SPADES, Rank.NINE),
                new Card(Suit.DIAMONDS, Rank.SEVEN),
                new Card(Suit.HEARTS, Rank.FIVE));

        setInput("1\n");

        Round round = new Round(
                player,
                dealer,
                deck,
                new InputReader());

        RoundResult result = round.play();

        assertEquals(RoundResult.DEALER_WIN, result);
    }

    /**
     * Победа игрока, если дилер перебрал.
     */
    @Test
    void dealerBustShouldGivePlayerWin() {
        Player player = new Player("Игрок");
        Dealer dealer = new Dealer();

        TestDeck deck = new TestDeck(
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.CLUBS, Rank.TEN),
                new Card(Suit.SPADES, Rank.NINE),
                new Card(Suit.DIAMONDS, Rank.SIX),
                new Card(Suit.HEARTS, Rank.TEN));

        setInput("0\n");

        Round round = new Round(
                player,
                dealer,
                deck,
                new InputReader());

        RoundResult result = round.play();

        assertEquals(RoundResult.PLAYER_WIN, result);
    }

    /**
     * Победа игрока по количеству очков.
     */
    @Test
    void playerShouldWinWithHigherScore() {
        Player player = new Player("Игрок");
        Dealer dealer = new Dealer();

        TestDeck deck = new TestDeck(
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.CLUBS, Rank.TEN),
                new Card(Suit.SPADES, Rank.NINE),
                new Card(Suit.DIAMONDS, Rank.EIGHT));

        setInput("0\n");

        Round round = new Round(
                player,
                dealer,
                deck,
                new InputReader());

        RoundResult result = round.play();

        assertEquals(RoundResult.PLAYER_WIN, result);
    }

    /**
     * Победа дилера по количеству очков.
     */
    @Test
    void dealerShouldWinWithHigherScore() {
        Player player = new Player("Игрок");
        Dealer dealer = new Dealer();

        TestDeck deck = new TestDeck(
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.CLUBS, Rank.TEN),
                new Card(Suit.SPADES, Rank.EIGHT),
                new Card(Suit.DIAMONDS, Rank.NINE));

        setInput("0\n");

        Round round = new Round(
                player,
                dealer,
                deck,
                new InputReader());

        RoundResult result = round.play();

        assertEquals(RoundResult.DEALER_WIN, result);
    }

    /**
     * Ничья при одинаковом количестве очков.
     */
    @Test
    void equalScoresShouldResultInDraw() {
        Player player = new Player("Игрок");
        Dealer dealer = new Dealer();

        TestDeck deck = new TestDeck(
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.CLUBS, Rank.TEN),
                new Card(Suit.SPADES, Rank.EIGHT),
                new Card(Suit.DIAMONDS, Rank.EIGHT));

        setInput("0\n");

        Round round = new Round(
                player,
                dealer,
                deck,
                new InputReader());

        RoundResult result = round.play();

        assertEquals(RoundResult.DRAW, result);
    }

    /**
     * Дилер берёт карту при счете меньше 17.
     */
    @Test
    void dealerShouldTakeCardWhenScoreIsLessThan17() {
        Player player = new Player("Игрок");
        Dealer dealer = new Dealer();

        TestDeck deck = new TestDeck(
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.CLUBS, Rank.TEN),
                new Card(Suit.SPADES, Rank.NINE),
                new Card(Suit.DIAMONDS, Rank.SIX),
                new Card(Suit.HEARTS, Rank.TWO));

        setInput("0\n");

        Round round = new Round(
                player,
                dealer,
                deck,
                new InputReader());

        RoundResult result = round.play();

        assertEquals(RoundResult.PLAYER_WIN, result);
        assertEquals(18, dealer.getScore());
    }

    /**
     * Несколько последовательных взятий карт дилером.
     */
    @Test
    void dealerShouldTakeSeveralCardsUntil17() {
        Player player = new Player("Игрок");
        Dealer dealer = new Dealer();

        TestDeck deck = new TestDeck(
                new Card(Suit.HEARTS, Rank.TEN),
                new Card(Suit.CLUBS, Rank.TEN),
                new Card(Suit.SPADES, Rank.NINE),
                new Card(Suit.DIAMONDS, Rank.TWO),
                new Card(Suit.HEARTS, Rank.TWO),
                new Card(Suit.CLUBS, Rank.THREE));

        setInput("0\n");

        Round round = new Round(
                player,
                dealer,
                deck,
                new InputReader());

        RoundResult result = round.play();

        assertEquals(RoundResult.PLAYER_WIN, result);
        assertEquals(17, dealer.getScore());
    }

    /**
     * Ситуация, когда оба игрока получают блэкджек.
     */
    @Test
    void bothBlackjacksShouldResultInDraw() {
        Player player = new Player("Игрок");
        Dealer dealer = new Dealer();

        TestDeck deck = new TestDeck(
                new Card(Suit.HEARTS, Rank.ACE),
                new Card(Suit.CLUBS, Rank.ACE),
                new Card(Suit.SPADES, Rank.KING),
                new Card(Suit.DIAMONDS, Rank.KING));

        Round round = new Round(
                player,
                dealer,
                deck,
                new InputReader());

        RoundResult result = round.play();

        assertEquals(RoundResult.DRAW, result);
    }

    /**
     * Тестовый ввод.
     */
    private static void setInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    /**
     * Тестовая колода.
     */
    private static class TestDeck extends Deck {
        private final Deque<Card> cards;

        /**
         * Создает тестовую колоду.
         */
        TestDeck(Card... testCards) {
            super(1);
            cards = new ArrayDeque<>();

            for (Card card : testCards) {
                cards.addLast(card);
            }
        }

        /**
         * Возвращает следующую заданную карту.
         */
        @Override
        public Card takeCard() {
            return cards.removeFirst();
        }
    }
}