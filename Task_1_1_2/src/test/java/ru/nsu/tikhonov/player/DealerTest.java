package ru.nsu.tikhonov.player;

import java.util.ArrayDeque;
import java.util.Deque;

import org.junit.jupiter.api.Test;

import ru.nsu.tikhonov.card.Card;
import ru.nsu.tikhonov.card.Rank;
import ru.nsu.tikhonov.card.Suit;
import ru.nsu.tikhonov.deck.Deck;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тестирует Dealer.
 */
class DealerTest {

    /**
     * Дилер берет карту, если у него меньше 17 очков.
     */
    @Test
    void dealerShouldTakeCardWhenScoreIsLessThan17() {
        Dealer dealer = new Dealer();
        TestDeck deck = new TestDeck(
                new Card(Suit.HEARTS, Rank.TWO));

        dealer.addCard(new Card(Suit.CLUBS, Rank.TEN));
        dealer.addCard(new Card(Suit.SPADES, Rank.SIX));

        dealer.playTurn(deck);

        assertEquals(18, dealer.getScore());
    }

    /**
     * Дилер останавливается на 17 очках.
     */
    @Test
    void dealerShouldStopAt17() {
        Dealer dealer = new Dealer();
        TestDeck deck = new TestDeck();

        dealer.addCard(new Card(Suit.HEARTS, Rank.TEN));
        dealer.addCard(new Card(Suit.CLUBS, Rank.SEVEN));

        dealer.playTurn(deck);

        assertEquals(17, dealer.getScore());
    }

    /**
     * Дилер не берет карту, если у него больше 17 очков.
     */
    @Test
    void dealerShouldStopWhenScoreIsMoreThan17() {
        Dealer dealer = new Dealer();
        TestDeck deck = new TestDeck(
                new Card(Suit.HEARTS, Rank.TWO));

        dealer.addCard(new Card(Suit.CLUBS, Rank.TEN));
        dealer.addCard(new Card(Suit.SPADES, Rank.NINE));

        dealer.playTurn(deck);

        assertEquals(19, dealer.getScore());
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
         * Возвращает следующую заранее заданную карту.
         */
        @Override
        public Card takeCard() {
            return cards.removeFirst();
        }
    }
}