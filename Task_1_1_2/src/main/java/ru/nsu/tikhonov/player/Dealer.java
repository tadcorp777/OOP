package ru.nsu.tikhonov.player;

import ru.nsu.tikhonov.card.Card;
import ru.nsu.tikhonov.deck.Deck;


/**
 * Представляет дилера в игре Blackjack.
 */
public class Dealer extends Player {

    public Dealer() {
        super("Дилер");
    }

    /**
     * Выполняет автоматический ход дилера.
     *
     */
    public void playTurn(Deck deck) {
        while (getScore() < 17) {
            Card card = deck.takeCard();
            addCard(card);

            System.out.println("Дилер открывает карту " + card);
        }
    }
}