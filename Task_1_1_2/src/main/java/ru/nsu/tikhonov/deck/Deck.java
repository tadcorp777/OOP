package ru.nsu.tikhonov.deck;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.nsu.tikhonov.card.Card;
import ru.nsu.tikhonov.card.Rank;
import ru.nsu.tikhonov.card.Suit;

/**
 * Представляет колоду игральных карт.
 *
 */
public class Deck {
    private final List<Card> cards;
    private final Random random;

    /**
     * Создает колоду из указанного количества стандартных колод и перемешивает.
     *
     */
    public Deck(int deckCount) {
        cards = new ArrayList<>();
        random = new Random();

        for (int i = 0; i < deckCount; i++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    cards.add(new Card(suit, rank));
                }
            }
        }

        shuffle();
    }

    private void shuffle() {
        for (int i = cards.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);

            Card temp = cards.get(i);
            cards.set(i, cards.get(j));
            cards.set(j, temp);
        }
    }

    public Card takeCard() {
        return cards.remove(cards.size() - 1);
    }

    public int size() {
        return cards.size();
    }
}