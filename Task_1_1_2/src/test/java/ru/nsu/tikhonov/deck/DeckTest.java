package ru.nsu.tikhonov.deck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.tikhonov.card.Card;
import ru.nsu.tikhonov.card.Rank;
import ru.nsu.tikhonov.card.Suit;
/**
 * Тестирует Deck.
 */
class DeckTest {

    /**
     * Создание одной стандартной колоды из 52 карт.
     */
    @Test
    void oneDeckShouldContain52Cards() {
        Deck deck = new Deck(1);

        assertEquals(52, deck.size());
    }

    /**
     * Создание нескольких колод.
     */
    @Test
    void severalDecksShouldContainCorrectNumberOfCards() {
        Deck deck = new Deck(4);

        assertEquals(208, deck.size());
    }

    /**
     * Уменьшение размера колоды после взятия карты.
     */
    @Test
    void takeCardShouldDecreaseDeckSize() {
        Deck deck = new Deck(1);

        deck.takeCard();

        assertEquals(51, deck.size());
    }

    /**
     * Возвращение карты из колоды.
     */
    @Test
    void takeCardShouldReturnCard() {
        Deck deck = new Deck(1);

        Card card = deck.takeCard();

        assertNotNull(card);
    }

    /**
     * Из одной колоды можно получить все 52 карты.
     */
    @Test
    void shouldTakeAllCardsFromDeck() {
        Deck deck = new Deck(1);

        for (int i = 0; i < 52; i++) {
            assertNotNull(deck.takeCard());
        }

        assertEquals(0, deck.size());
    }

    /**
     * Колода содержит карты разных мастей и достоинств.
     */
    @Test
    void deckShouldContainAllCardTypes() {
        Deck deck = new Deck(1);

        boolean foundAceOfHearts = false;

        for (int i = 0; i < 52; i++) {
            Card card = deck.takeCard();

            if (card.getSuit() == Suit.HEARTS
                    && card.getRank() == Rank.ACE) {
                foundAceOfHearts = true;
            }
        }

        assertEquals(true, foundAceOfHearts);
    }
}