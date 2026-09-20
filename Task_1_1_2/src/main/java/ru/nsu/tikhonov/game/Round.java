package ru.nsu.tikhonov.game;

import ru.nsu.tikhonov.card.Card;
import ru.nsu.tikhonov.deck.Deck;
import ru.nsu.tikhonov.player.Dealer;
import ru.nsu.tikhonov.player.Player;
import ru.nsu.tikhonov.util.InputReader;

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

    public Round(Player player, Dealer dealer, Deck deck, InputReader inputReader) {
        this.player = player;
        this.dealer = dealer;
        this.deck = deck;
        this.inputReader = inputReader;
    }

    public int play() {
        dealInitialCards();

        if (player.isBlackjack() && dealer.isBlackjack()) {
            revealDealerCard();
            printCards();
            System.out.println("У обоих блэкджек. Ничья.");
            return 0;
        }

        if (player.isBlackjack()) {
            printCards();
            System.out.println("У вас блэкджек! Вы выиграли раунд.");
            return 1;
        }

        if (dealer.isBlackjack()) {
            revealDealerCard();
            printCards();
            System.out.println("У дилера блэкджек. Вы проиграли раунд.");
            return -1;
        }

        if (!playerTurn()) {
            return -1;
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

    private boolean playerTurn() {
        System.out.println();
        System.out.println("Ваш ход");
        System.out.println("-------");

        while (true) {
            System.out.println("Введите \"1\", чтобы взять карту, и \"0\", чтобы остановиться.");
            int choice = inputReader.readPlayerChoice();

            if (choice == 0) {
                return true;
            }

            Card card = deck.takeCard();
            player.addCard(card);

            System.out.println("Вы открыли карту " + card);
            printCards();

            if (player.isBust()) {
                System.out.println("Вы набрали больше 21. Вы проиграли раунд.");
                return false;
            }
        }
    }

    private void dealerTurn() {
        System.out.println();
        System.out.println("Ход дилера");
        System.out.println("-------");

        revealDealerCard();

        printCards();

        while (dealer.getScore() < 17) {
            Card card = deck.takeCard();
            dealer.addCard(card);

            System.out.println("Дилер открывает карту " + card);
            printCards();

            if (dealer.isBust()) {
                return;
            }
        }
    }

    private void revealDealerCard() {
        dealerCardRevealed = true;
        System.out.println("Дилер открывает закрытую карту " + hiddenDealerCard);
    }

    private void printCards() {
        System.out.println("Ваши карты: " + player.getCards() + " > " + player.getScore());

        if (!dealerCardRevealed) {
            System.out.println("Карты дилера: [" + dealer.getCards().get(0) + ", <закрытая карта>]");
        } else {
            System.out.println("Карты дилера: " +
                    dealer.getCards() + " > " + dealer.getScore());
        }
    }

    private int determineWinner() {
        System.out.println();

        if (dealer.isBust()) {
            System.out.println("Дилер набрал больше 21. Вы выиграли раунд!");
            return 1;
        }

        if (player.getScore() > dealer.getScore()) {
            System.out.println("Вы выиграли раунд!");
            return 1;
        }

        if (player.getScore() < dealer.getScore()) {
            System.out.println("Вы проиграли раунд.");
            return -1;
        }

        System.out.println("Ничья.");
        return 0;
    }
}