package ru.nsu.tikhonov;

import ru.nsu.tikhonov.game.Game;

/**
 * Точка входа в консольную игру.
 */
public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}