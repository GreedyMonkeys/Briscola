package net.mistrio.briscola.service;

import lombok.RequiredArgsConstructor;

import net.mistrio.briscola.dto.TrumpTable;
import net.mistrio.briscola.model.TrumpCard;
import net.mistrio.briscola.model.TrumpGameState;
import net.mistrio.briscola.repository.PlayerRepository;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrumpGameStateToTrumpTable {
    private final PlayerRepository playerRepository;

    public TrumpTable convert(TrumpGameState gameState, Integer playerId) {
        var table = new TrumpTable();
        var playingPlayer =
                playerRepository
                        .findById(gameState.getTurnOrder().get(gameState.getTurn()))
                        .orElseThrow(IllegalStateException::new);

        Integer opponentId = getOpponentId(gameState, playerId);
        Integer handWinner = gameState.getTurnOrder().get(0);

        table.setDeckSize(gameState.getDeck().getCards().size());
        table.setTrump(gameState.getTrump());
        table.setTurn(gameState.getTurnOrder().indexOf(gameState.getTurn()) + 1);
        table.setPlayingPlayerName(playingPlayer.getName());
        table.setPlayerHand(gameState.getPlayerHands().get(playerId));
        table.setOpponentPlayedCard(gameState.getPlayerHands().get(opponentId).getPlayedCard());
        table.setLastPlayedHand(getLastPlayedHand(gameState, handWinner));

        return table;
    }

    private Integer getOpponentId(TrumpGameState gameState, Integer playerId) {
        return gameState.getPlayerHands().keySet().stream()
                .filter(id -> !id.equals(playerId))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private List<TrumpCard> getLastPlayedHand(TrumpGameState gameState, Integer handWinner) {
        var winnerStashCards = gameState.getPlayerStashes().get(handWinner).getCards();

        // used in case of first hand
        if (winnerStashCards.isEmpty()) {
            return winnerStashCards;
        }

        return winnerStashCards.stream().skip((long) winnerStashCards.size() - 2).toList();
    }
}
