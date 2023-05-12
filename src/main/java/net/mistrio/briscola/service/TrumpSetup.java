package net.mistrio.briscola.service;

import net.mistrio.briscola.model.TrumpCardStash;
import net.mistrio.briscola.model.TrumpDeck;
import net.mistrio.briscola.model.TrumpGameState;
import net.mistrio.briscola.model.TrumpPlayerHand;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TrumpSetup {

    public TrumpGameState setup(List<Integer> playerIds, TrumpDeck deck) {
        var trumpState = new TrumpGameState();

        var playerHands = createPlayerHands(playerIds, deck);
        var playerStashes = createPlayerStashes(playerIds);
        var lastCard = deck.peek(deck.size() - 1);

        trumpState.setDeck(deck);
        trumpState.setTurn(0);
        trumpState.setTurnOrder(playerIds);
        trumpState.setTrump(lastCard);
        trumpState.setPlayerHands(playerHands);
        trumpState.setPlayerStashes(playerStashes);

        return trumpState;
    }

    private Map<Integer, TrumpPlayerHand> createPlayerHands(
            List<Integer> playerIds, TrumpDeck deck) {
        var playerHands = new HashMap<Integer, TrumpPlayerHand>();

        for (int i = 0; i < playerIds.size(); i++) {
            var playerId = playerIds.get(i);
            var playerHand = new TrumpPlayerHand();

            playerHand.setPlayerId(playerId);
            playerHand.setCards(deck.draw(3));
            playerHand.setUpdatedAt(Instant.now());

            playerHands.put(playerId, playerHand);
        }

        return playerHands;
    }

    private Map<Integer, TrumpCardStash> createPlayerStashes(List<Integer> playerIds) {
        var playerStashes = new HashMap<Integer, TrumpCardStash>();

        for (var playerId : playerIds) {
            var stash = new TrumpCardStash();
            stash.setPlayerId(playerId);
            stash.setCards(Collections.emptyList());
            playerStashes.put(playerId, stash);
        }

        return playerStashes;
    }
}
