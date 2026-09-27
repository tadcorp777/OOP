package ru.nsu.tikhonov.card;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тестирует Card.
 */
class CardTest {

    /**
     * Получение масти карты.
     */
    @Test
    void getSuitShouldReturnCorrectSuit() {
        Card card = new Card(Suit.HEARTS, Rank.ACE);

        assertEquals(Suit.HEARTS, card.getSuit());
    }

    /**
     * Получение достоинства карты.
     */
    @Test
    void getRankShouldReturnCorrectRank() {
        Card card = new Card(Suit.SPADES, Rank.KING);

        assertEquals(Rank.KING, card.getRank());
    }

    /**
     * Получение значения карты.
     */
    @Test
    void getValueShouldReturnRankValue() {
        Card card = new Card(Suit.DIAMONDS, Rank.QUEEN);

        assertEquals(10, card.getValue());
    }

    /**
     * Текстовое представление карты.
     */
    @Test
    void toStringShouldReturnCorrectRepresentation() {
        Card card = new Card(Suit.CLUBS, Rank.SEVEN);

        assertEquals("Семерка Трефы (7)", card.toString());
    }
}