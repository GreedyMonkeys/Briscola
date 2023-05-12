package net.mistrio.briscola.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrumpCard {

    private TrumpCardRank rank;
    private TrumpCardSuite suite;

    public Boolean equalTo(TrumpCard card) {
        return this.rank == card.rank && this.suite == card.suite;
    }

    @JsonIgnore
    public Integer getRankAsInteger() {
        return rank.ordinal();
    }

    @JsonIgnore
    public Integer getScore() {
        return switch (rank) {
            case ACE -> 11;
            case THREE -> 10;
            case KING -> 4;
            case KNIGHT -> 3;
            case JACK -> 2;
            default -> 0;
        };
    }
}
