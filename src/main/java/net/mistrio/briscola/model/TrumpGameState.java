package net.mistrio.briscola.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TrumpGameState {
    private Integer turn;
    private List<Integer> turnOrder;
    private TrumpDeck deck;
    private TrumpCard trump;
    private Map<Integer, TrumpPlayerHand> playerHands;
    private Map<Integer, TrumpCardStash> playerStashes;
}
