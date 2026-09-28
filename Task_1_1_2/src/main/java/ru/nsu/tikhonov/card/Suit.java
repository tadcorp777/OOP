package ru.nsu.tikhonov.card;

/**
 * Представляет масть игральной карты.
 */
public enum Suit {
    HEARTS("Червы"),
    DIAMONDS("Бубны"),
    CLUBS("Трефы"),
    SPADES("Пики");

    private final String name;

    Suit(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}