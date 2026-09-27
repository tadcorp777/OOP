package ru.nsu.tikhonov.game;

import ru.nsu.tikhonov.card.Card;
import ru.nsu.tikhonov.deck.Deck;
import ru.nsu.tikhonov.player.Dealer;
import ru.nsu.tikhonov.player.Player;
import ru.nsu.tikhonov.util.InputReader;
import ru.nsu.tikhonov.game.RoundResult;

/**
 * Отвечает за раздачу карт, ход игрока, ход дилера и определение результата раунда.
 */
public class Round {
    private final Player player;
    private final Dealer dealer;
    private final Deck deck;
    private final InputReader inputReader;

    private Card hiddenDealerCard;
    private boolean dealerCardRevealed;

    /**
     * Создает новый раунд.
     *
     */
    public Round(Player player, Dealer dealer, Deck deck, InputReader inputReader) {
        this.player = player;
        this.dealer = dealer;
        this.deck = deck;
        this.inputReader = inputReader;
    }

    /**
     * Запускает раунд.
     *
     */
    public RoundResult play() {
        dealInitialCards();

        if (player.isBlackjack() && dealer.isBlackjack()) {
            revealDealerCard();
            printCards();
            System.out.println("У обоих блэкджек. Ничья.");
            return RoundResult.DRAW;
        }

        if (player.isBlackjack()) {
            printCards();
            System.out.println("У вас блэкджек! Вы выиграли раунд.");
            return RoundResult.PLAYER_WIN;
        }

        if (dealer.isBlackjack()) {
            revealDealerCard();
            printCards();
            System.out.println("У дилера блэкджек. Вы проиграли раунд.");
            return RoundResult.DEALER_WIN;
        }

        System.out.println();
        System.out.println("Ваш ход");
        System.out.println("-------");

        if (!player.playTurn(deck, inputReader)) {
            return RoundResult.DEALER_WIN;
        }

        dealerTurn();

        return determineWinner();
    }

    private void dealInitialCards() {
        dealerCardRevealed = false;

        player.addCard(deck.takeCard());
        dealer.addCard(deck.takeCard());
        player.addCard(deck.takeCard());

        hiddenDealerCard = deck.takeCard();
        dealer.addCard(hiddenDealerCard);

        System.out.println("Дилер раздал карты");
        printCards();
    }

    private void dealerTurn() {
        System.out.println();
        System.out.println("Ход дилера");
        System.out.println("-------");

        revealDealerCard();
        printCards();

        dealer.playTurn(deck);

        printCards();
    }

    private void revealDealerCard() {
        dealerCardRevealed = true;
        System.out.println("Дилер открывает закрытую карту " + hiddenDealerCard);
    }

    private void printCards() {
        System.out.println("Ваши карты: " + player.getCards() + " > " + player.getScore());

        if (!dealerCardRevealed) {
            System.out.println("Карты дилера: ["
                    + dealer.getCards().get(0)
                    + ", <закрытая карта>]");
        } else {
            System.out.println("Карты дилера: "
                    + dealer.getCards()
                    + " > "
                    + dealer.getScore());
        }
    }

    private RoundResult determineWinner() {
        System.out.println();

        if (dealer.isBust()) {
            System.out.println("Дилер набрал больше 21. Вы выиграли раунд!");
            return RoundResult.PLAYER_WIN;
        }

        if (player.getScore() > dealer.getScore()) {
            System.out.println("Вы выиграли раунд!");
            return RoundResult.PLAYER_WIN;
        }

        if (player.getScore() < dealer.getScore()) {
            System.out.println("Вы проиграли раунд.");
            return RoundResult.DEALER_WIN;
        }

        System.out.println("Ничья.");
        return RoundResult.DRAW;
    }
}