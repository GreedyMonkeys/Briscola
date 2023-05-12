package net.mistrio.briscola.service.trump;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.mistrio.briscola.model.TrumpDeck;
import net.mistrio.briscola.service.TrumpSetup;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class TrumpSetupTest {

    @Test
    void setup() {
        var players = List.of(100, 101);
        var deck = new TrumpDeck();
        deck.initialize();

        var trumpSetup = new TrumpSetup();
        var trumpState = trumpSetup.setup(players, deck);

        assertEquals(0, trumpState.getTurn());
        assertEquals(players, trumpState.getTurnOrder());

        assertEquals(34, trumpState.getDeck().size());

        assertEquals(100, trumpState.getPlayerHands().get(100).getPlayerId());
        assertEquals(101, trumpState.getPlayerHands().get(101).getPlayerId());

        assertEquals(100, trumpState.getPlayerStashes().get(100).getPlayerId());
        assertEquals(101, trumpState.getPlayerStashes().get(101).getPlayerId());

        assertEquals(3, trumpState.getPlayerHands().get(100).getCards().size());
        assertEquals(3, trumpState.getPlayerHands().get(101).getCards().size());
    }
}
