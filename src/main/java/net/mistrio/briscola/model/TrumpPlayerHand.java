package net.mistrio.briscola.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrumpPlayerHand {
    private Integer playerId;
    private List<TrumpCard> cards;
    private Optional<TrumpCard> playedCard = Optional.empty();
    private Instant updatedAt;

    public TrumpCard popPlayedCard() {
        var card = playedCard.orElseThrow(IllegalStateException::new);
        cards = cards.stream().filter(c -> !c.equalTo(card)).toList();
        playedCard = Optional.empty();
        return card;
    }

    public void addCard(TrumpCard card) {
        cards = Stream.concat(cards.stream(), Stream.of(card)).toList();
    }
}
