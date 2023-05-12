package net.mistrio.briscola.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import net.mistrio.briscola.model.TrumpCard;
import net.mistrio.briscola.model.TrumpCardSuite;
import net.mistrio.briscola.model.TrumpGameState;
import net.mistrio.briscola.model.TrumpPlayerHand;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class TrumpPlayCard {

    public TrumpGameState playCard(
            TrumpGameState gameState, Integer playerId, TrumpCard playedCard) {
        log.debug("Resolving hand for player {} with card {}", playerId, playedCard);

        if (!playIsPermitted(gameState, playerId, playedCard)) {
            throw new IllegalStateException("Illegal play");
        }

        var playerHand = gameState.getPlayerHands().get(playerId);
        playerHand.setPlayedCard(Optional.of(playedCard));
        gameState.setTurn(gameState.getTurn() + 1);

        if (gameState.getTurnOrder().size() == gameState.getTurn()) {
            resolveGameState(gameState);
        }

        return gameState;
    }

    private void resolveGameState(TrumpGameState gameState) {
        var handWinnerId = getWinner(gameState);

        fillPlayerStash(gameState, handWinnerId);
        resetTurn(gameState, handWinnerId);
        refillHands(gameState);
    }

    private void refillHands(TrumpGameState gameState) {
        var deck = gameState.getDeck();
        if (deck.isEmpty()) {
            return;
        }

        var playerHands = gameState.getPlayerHands();
        var turnOrder = gameState.getTurnOrder();

        for (var playerId : turnOrder) {
            var playerHand = playerHands.get(playerId);
            var drawnCard = deck.draw();
            playerHand.addCard(drawnCard);
        }
    }

    // turnOrder[123, 124, 125, 126] | winnerId 125 -> newTurnOrder[125, 126, 123, 124]
    private void resetTurn(TrumpGameState gameState, Integer handWinnerId) {
        gameState.setTurn(0);
        var turnOrder = gameState.getTurnOrder();
        var winnerIndex = turnOrder.indexOf(handWinnerId);
        var head = turnOrder.subList(winnerIndex, turnOrder.size());
        var tail = turnOrder.subList(0, winnerIndex);
        var newOrder = Stream.concat(head.stream(), tail.stream()).toList();
        gameState.setTurnOrder(newOrder);
    }

    private void fillPlayerStash(TrumpGameState gameState, Integer playerId) {
        var cards = popPlayedCardsFromHands(gameState);
        var winnerStash = gameState.getPlayerStashes().get(playerId);
        winnerStash.addCards(cards);
    }

    private List<TrumpCard> popPlayedCardsFromHands(TrumpGameState gameState) {
        return gameState.getPlayerHands().values().stream()
                .map(TrumpPlayerHand::popPlayedCard)
                .collect(Collectors.toList());
    }

    private Integer getWinner(TrumpGameState gameState) {

        var winningSuite = getWinningSuite(gameState);

        return gameState.getPlayerHands().values().stream()
                .map(TrumpPlayerCard::fromPlayerHand)
                .filter(playerCard -> playerCard.hasSuite(winningSuite))
                .max(Comparator.comparing(TrumpPlayerCard::getRank))
                .map(TrumpPlayerCard::getPlayerId)
                .orElseThrow(IllegalStateException::new);
    }

    private TrumpCardSuite getWinningSuite(TrumpGameState gameState) {
        var initialSuite =
                gameState
                        .getPlayerHands()
                        .get(gameState.getTurnOrder().get(0))
                        .getPlayedCard()
                        .map(TrumpCard::getSuite)
                        .orElseThrow(IllegalStateException::new);

        // check if there's atleast a trump, otherwise return initial suite
        return gameState.getPlayerHands().values().stream()
                .map(TrumpPlayerHand::getPlayedCard)
                .map(Optional::get)
                .map(TrumpCard::getSuite)
                .filter(suite -> suite.equals((gameState).getTrump().getSuite()))
                .findAny()
                .orElse(initialSuite);
    }

    private boolean playIsPermitted(
            TrumpGameState gameState, Integer playerId, TrumpCard playedCard) {
        var playerCards = gameState.getPlayerHands().get(playerId).getCards();
        var playingPlayer = gameState.getTurnOrder().get(gameState.getTurn());

        // Check if it's player's turn
        if (!playingPlayer.equals(playerId)) {
            log.debug("player {} attempted to play before {}", playerId, playingPlayer);
            return false;
        }

        // Check if player has the card
        if (!playerCards.contains(playedCard)) {
            log.debug("player {} attempted to play card not possesed {}", playerId, playedCard);
            return false;
        }

        return true;
    }
}

@Data
class TrumpPlayerCard {

    private Integer playerId;
    private TrumpCard card;

    static TrumpPlayerCard fromPlayerHand(TrumpPlayerHand playerHand) {
        var playerCard = new TrumpPlayerCard();

        playerCard.setCard(playerHand.getPlayedCard().orElseThrow(IllegalStateException::new));
        playerCard.setPlayerId(playerHand.getPlayerId());

        return playerCard;
    }

    boolean hasSuite(TrumpCardSuite suite) {
        return card.getSuite().equals(suite);
    }

    Integer getRank() {
        return card.getRankAsInteger();
    }
}
