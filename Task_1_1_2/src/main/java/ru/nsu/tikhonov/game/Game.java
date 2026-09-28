package ru.nsu.tikhonov.game;

import ru.nsu.tikhonov.deck.Deck;
import ru.nsu.tikhonov.player.Dealer;
import ru.nsu.tikhonov.player.Player;
import ru.nsu.tikhonov.util.InputReader;

/**
 * Управляет всей игрой и последовательностью раундов.
 */
public class Game {
    private final Player player;
    private final Dealer dealer;
    private final InputReader inputReader;

    private int playerWins;
    private int dealerWins;

    /**
     * Создает новую игру с одним игроком и одним дилером.
     */
    public Game() {
        player = new Player("Игрок");
        dealer = new Dealer();
        inputReader = new InputReader();
    }

    /**
     * Запускает игру и последовательно проводит раунды.
     */
    public void start() {
        System.out.println("Добро пожаловать в Блэкджек!");
        System.out.println("Введите количество колод:");
        int deckCount = inputReader.readDeckCount();
        int roundNumber = 1;

        while (true) {
            System.out.println();
            System.out.println("Раунд " + roundNumber);

            player.clearCards();
            dealer.clearCards();

            Deck deck = new Deck(deckCount);

            Round round = new Round(player, dealer, deck, inputReader);
            RoundResult result = round.play();

            switch (result) {
                case PLAYER_WIN -> playerWins++;
                case DEALER_WIN -> dealerWins++;
                case DRAW -> {
                }
                default -> throw new IllegalStateException(
                        "Неизвестный результат раунда.");
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