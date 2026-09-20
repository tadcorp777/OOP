package ru.nsu.tikhonov.player;

import ru.nsu.tikhonov.card.Card;

import java.util.ArrayList;
import java.util.List;


/**
 * Представляет игрока и его карты.
 */
public class Player {
    private final String name;
    private final List<Card> cards;

    public Player(String name) {
        this.name = name;
        cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void clearCards() {
        cards.clear();
    }

    public int getScore() {
        int score = 0;
        int aces = 0;

        for (Card card : cards) {
            score += card.getValue();

            if (card.getRank().isAce()) {
                aces++;
            }
        }

        while (score > 21 && aces > 0) {
            score -= 10;
            aces--;
        }

        return score;
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && getScore() == 21;
    }

    public boolean isBust() {
        return getScore() > 21;
    }

    public List<Card> getCards() {
        return cards;
    }

    public String getName() {
        return name;
    }
}