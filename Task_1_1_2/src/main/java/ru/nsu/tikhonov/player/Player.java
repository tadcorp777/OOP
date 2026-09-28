package ru.nsu.tikhonov.player;

import java.util.ArrayList;
import java.util.List;
import ru.nsu.tikhonov.card.Card;
import ru.nsu.tikhonov.deck.Deck;
import ru.nsu.tikhonov.util.InputReader;


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

    /**
     * Добавляет карту игроку.
     *
     */
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

    /**
     * Выполняет ход игрока.
     *
     */
    public boolean playTurn(Deck deck, InputReader inputReader) {
        while (true) {
            System.out.println(
                    "Введите \"1\", чтобы взять карту, и \"0\", чтобы остановиться.");

            int choice = inputReader.readPlayerChoice();

            if (choice == 0) {
                return true;
            }

            Card card = deck.takeCard();
            addCard(card);

            System.out.println("Вы открыли карту " + card);
            System.out.println("Ваш текущий счет: " + getScore());

            if (isBust()) {
                System.out.println("Вы набрали больше 21. Вы проиграли раунд.");
                return false;
            }
        }
    }
}