package net.mistrio.briscola.dto;

import lombok.Data;

import net.mistrio.briscola.model.TrumpCard;
import net.mistrio.briscola.model.TrumpPlayerHand;

import java.util.List;
import java.util.Optional;

@Data
public class TrumpTable {
    private Integer deckSize;
    private TrumpCard trump;
    private Integer turn;
    private String playingPlayerName;
    private TrumpPlayerHand playerHand;
    private Optional<TrumpCard> opponentPlayedCard;
    private List<TrumpCard> lastPlayedHand;
}
