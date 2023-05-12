package net.mistrio.briscola.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrumpCardStash {
    private Integer playerId;
    private List<TrumpCard> cards;

    public void addCards(List<TrumpCard> cards) {
        this.cards.addAll(cards);
    }
}
