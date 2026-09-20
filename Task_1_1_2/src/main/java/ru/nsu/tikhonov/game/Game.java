package ru.nsu.tikhonov.game;

import ru.nsu.tikhonov.deck.Deck;
import ru.nsu.tikhonov.player.Dealer;
import ru.nsu.tikhonov.player.Player;
import ru.nsu.tikhonov.util.InputReader;

public class Game {
    private final Player player;
    private final Dealer dealer;
    private final InputReader inputReader;

    private int playerWins;
    private int dealerWins;

    public Game() {
        player = new Player("Игрок");
        dealer = new Dealer();
        inputReader = new InputReader();
    }

    public void start() {
        System.out.println("Добро пожаловать в Блэкджек!");

        int roundNumber = 1;

        while (true) {
            System.out.println();
            System.out.println("Раунд " + roundNumber);

            player.clearCards();
            dealer.clearCards();

            Deck deck = new Deck(4);

            Round round = new Round(player, dealer, deck, inputReader);
            int result = round.play();

            if (result == 1) {
                playerWins++;
            } else if (result == -1) {
                dealerWins++;
            }

            System.out.println();
            System.out.println("Текущий счет: " + playerWins + ":" + dealerWins);

            roundNumber++;

            System.out.println();
            System.out.println("Чтобы сыграть следующий раунд, введите 1.");
            System.out.println("Чтобы завершить игру, введите 0.");

            int choice = inputReader.readPlayerChoice();

            if (choice == 0) {
                break;
            }
        }

        System.out.println();
        System.out.println("Игра окончена.");
        System.out.println("Итоговый счет: " + playerWins + ":" + dealerWins);
    }
}