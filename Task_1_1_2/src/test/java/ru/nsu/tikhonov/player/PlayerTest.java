package ru.nsu.tikhonov.player;

import java.io.ByteArrayInputStream;
import java.util.ArrayDeque;
import java.util.Deque;

import org.junit.jupiter.api.Test;

import ru.nsu.tikhonov.card.Card;
import ru.nsu.tikhonov.card.Rank;
import ru.nsu.tikhonov.card.Suit;
import ru.nsu.tikhonov.deck.Deck;
import ru.nsu.tikhonov.util.InputReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тестирует Player.
 */
class PlayerTest {

    /**
     * Создание игрока и получение его имени.
     */
    @Test
    void getNameShouldReturnCorrectName() {
        Player player = new Player("Игрок");

        assertEquals("Игрок", player.getName());
    }

    /**
     * Добавление карты игроку.
     */
    @Test
    void addCardShouldAddCardToPlayer() {
        Player player = new Player("Игрок");
        Card card = new Card(Suit.HEARTS, Rank.ACE);

        player.addCard(card);

        assertEquals(1, player.getCards().size());
        assertEquals(card, player.getCards().get(0));
    }

    /**
     * Удаление всех карт игрока.
     */
    @Test
    void clearCardsShouldRemoveAllCards() {
        Player player = new Player("Игрок");

        player.addCard(new Card(Suit.HEARTS, Rank.TWO));
        player.addCard(new Card(Suit.CLUBS, Rank.KING));

        player.clearCards();

        assertTrue(player.getCards().isEmpty());
    }

    /**
     * Подсчёт очков обычных карт.
     */
    @Test
    void getScoreShouldCountCardsCorrectly() {
        Player player = new Player("Игрок");

        player.addCard(new Card(Suit.HEARTS, Rank.TWO));
        player.addCard(new Card(Suit.CLUBS, Rank.SEVEN));
        player.addCard(new Card(Suit.SPADES, Rank.KING));

        assertEquals(19, player.getScore());
    }

    /**
     * Превращение туза из 11 очков в 1 при переборе.
     */
    @Test
    void getScoreShouldChangeAceValueWhenNeeded() {
        Player player = new Player("Игрок");

        player.addCard(new Card(Suit.HEARTS, Rank.ACE));
        player.addCard(new Card(Suit.CLUBS, Rank.KING));
        player.addCard(new Card(Suit.DIAMONDS, Rank.FIVE));

        assertEquals(16, player.getScore());
    }

    /**
     * Использование нескольких тузов.
     */
    @Test
    void getScoreShouldAdjustSeveralAces() {
        Player player = new Player("Игрок");

        player.addCard(new Card(Suit.HEARTS, Rank.ACE));
        player.addCard(new Card(Suit.CLUBS, Rank.ACE));
        player.addCard(new Card(Suit.SPADES, Rank.NINE));

        assertEquals(21, player.getScore());
    }

    /**
     * Определение блэкджека.
     */
    @Test
    void isBlackjackShouldReturnTrueForTwoCardsAnd21Points() {
        Player player = new Player("Игрок");

        player.addCard(new Card(Suit.HEARTS, Rank.ACE));
        player.addCard(new Card(Suit.CLUBS, Rank.KING));

        assertTrue(player.isBlackjack());
    }

    /**
     * 21 очко с тремя картами не является блэкджеком.
     */
    @Test
    void isBlackjackShouldReturnFalseForThreeCards() {
        Player player = new Player("Игрок");

        player.addCard(new Card(Suit.HEARTS, Rank.SEVEN));
        player.addCard(new Card(Suit.CLUBS, Rank.SEVEN));
        player.addCard(new Card(Suit.SPADES, Rank.SEVEN));

        assertFalse(player.isBlackjack());
    }

    /**
     * Две карты без 21 очка не являются блэкджеком.
     */
    @Test
    void isBlackjackShouldReturnFalseWhenScoreIsNot21() {
        Player player = new Player("Игрок");

        player.addCard(new Card(Suit.HEARTS, Rank.TEN));
        player.addCard(new Card(Suit.CLUBS, Rank.NINE));

        assertFalse(player.isBlackjack());
    }

    /**
     * Отсутствие перебора при счете не больше 21.
     */
    @Test
    void isBustShouldReturnFalseWhenScoreIs21OrLess() {
        Player player = new Player("Игрок");

        player.addCard(new Card(Suit.HEARTS, Rank.TEN));
        player.addCard(new Card(Suit.CLUBS, Rank.NINE));

        assertFalse(player.isBust());
    }

    /**
     * Перебор при счете больше 21.
     */
    @Test
    void isBustShouldReturnTrueWhenScoreIsOver21() {
        Player player = new Player("Игрок");

        player.addCard(new Card(Suit.HEARTS, Rank.KING));
        player.addCard(new Card(Suit.CLUBS, Rank.NINE));
        player.addCard(new Card(Suit.SPADES, Rank.FIVE));

        assertTrue(player.isBust());
    }

    /**
     * Остановку игрока без взятия карты.
     */
    @Test
    void playTurnShouldStopWhenPlayerChoosesZero() {
        Player player = new Player("Игрок");
        TestDeck deck = new TestDeck(
                new Card(Suit.HEARTS, Rank.TWO));

        System.setIn(new ByteArrayInputStream("0\n".getBytes()));
        InputReader inputReader = new InputReader();

        boolean result = player.playTurn(deck, inputReader);

        assertTrue(result);
        assertEquals(0, player.getCards().size());
    }

    /**
     * Взятие карты игроком и последующую остановку.
     */
    @Test
    void playTurnShouldTakeCardAndThenStop() {
        Player player = new Player("Игрок");
        TestDeck deck = new TestDeck(
                new Card(Suit.HEARTS, Rank.FIVE));

        player.addCard(new Card(Suit.CLUBS, Rank.TEN));

        System.setIn(new ByteArrayInputStream("1\n0\n".getBytes()));
        InputReader inputReader = new InputReader();

        boolean result = player.playTurn(deck, inputReader);

        assertTrue(result);
        assertEquals(2, player.getCards().size());
        assertEquals(15, player.getScore());
    }

    /**
     * Проигрыш игрока при переборе во время хода.
     */
    @Test
    void playTurnShouldReturnFalseWhenPlayerBusts() {
        Player player = new Player("Игрок");
        TestDeck deck = new TestDeck(
                new Card(Suit.HEARTS, Rank.FIVE));

        player.addCard(new Card(Suit.CLUBS, Rank.TEN));
        player.addCard(new Card(Suit.SPADES, Rank.NINE));

        System.setIn(new ByteArrayInputStream("1\n".getBytes()));
        InputReader inputReader = new InputReader();

        boolean result = player.playTurn(deck, inputReader);

        assertFalse(result);
        assertEquals(3, player.getCards().size());
        assertEquals(24, player.getScore());
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