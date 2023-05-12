package net.mistrio.briscola.testutils;

import net.mistrio.briscola.model.TrumpCardStash;
import net.mistrio.briscola.model.TrumpDeck;
import net.mistrio.briscola.model.TrumpGameState;
import net.mistrio.briscola.model.TrumpPlayerHand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultTrumpStateFactory {

    public TrumpGameState createTrumpState() {
        var deck = new TrumpDeck();
        deck.initialize();

        var playerHand1 = new TrumpPlayerHand();
        playerHand1.setPlayerId(100);
        playerHand1.setCards(deck.draw(3));

        var playerHand2 = new TrumpPlayerHand();
        playerHand2.setPlayerId(101);
        playerHand2.setCards(deck.draw(3));

        var playerHands =
                Map.of(
                        100, (TrumpPlayerHand) playerHand1,
                        101, (TrumpPlayerHand) playerHand2);

        var playerStashes =
                Map.of(
                        100,
                        (TrumpCardStash) new TrumpCardStash(100, new ArrayList<>()),
                        101,
                        (TrumpCardStash) new TrumpCardStash(101, new ArrayList<>()));

        var defaultTrumpState = new TrumpGameState();
        defaultTrumpState.setTurn(0);
        defaultTrumpState.setTurnOrder(List.of(100, 101));
        defaultTrumpState.setDeck(deck);
        defaultTrumpState.setTrump(deck.peek(deck.size() - 1));
        defaultTrumpState.setPlayerHands(playerHands);
        defaultTrumpState.setPlayerStashes(playerStashes);

        return defaultTrumpState;
    }
}
