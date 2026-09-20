package ru.nsu.tikhonov.player;

import org.junit.jupiter.api.Test;
import ru.nsu.tikhonov.card.Card;
import ru.nsu.tikhonov.card.Rank;
import ru.nsu.tikhonov.card.Suit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerTest {

    @Test
    void aceShouldCountAsOneWhenNeeded() {
        Player player = new Player("Игрок");

        player.addCard(new Card(Suit.HEARTS, Rank.ACE));
        player.addCard(new Card(Suit.CLUBS, Rank.KING));
        player.addCard(new Card(Suit.DIAMONDS, Rank.FIVE));

        assertEquals(16, player.getScore());
    }
}