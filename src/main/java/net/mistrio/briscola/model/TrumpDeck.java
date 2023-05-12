package net.mistrio.briscola.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class TrumpDeck {
    private List<TrumpCard> cards;

    public void initialize() {
        cards = new ArrayList<>();
        for (TrumpCardSuite suite : TrumpCardSuite.values()) {
            for (TrumpCardRank rank : TrumpCardRank.values()) {
                cards.add(new TrumpCard(rank, suite));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public TrumpCard draw() {
        return draw(1).get(0);
    }

    public List<TrumpCard> draw(Integer cardsNumber) {
        if (cards.size() < cardsNumber) {
            throw new IllegalStateException("Deck doesn't have enough cards");
        }
        var drawnCards = cards.stream().limit(cardsNumber).toList();
        cards = cards.subList(cardsNumber, cards.size());
        return drawnCards;
    }

    public Integer size() {
        return cards.size();
    }

    public TrumpCard peek(Integer index) {
        return cards.get(index);
    }

    @JsonIgnore
    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
