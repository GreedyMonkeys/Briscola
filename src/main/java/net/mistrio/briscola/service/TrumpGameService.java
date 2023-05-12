package net.mistrio.briscola.service;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import net.mistrio.briscola.dto.TrumpTable;
import net.mistrio.briscola.exception.GameNotFoundException;
import net.mistrio.briscola.model.GamePlayer;
import net.mistrio.briscola.model.TrumpCard;
import net.mistrio.briscola.model.TrumpCardStash;
import net.mistrio.briscola.model.TrumpDeck;
import net.mistrio.briscola.model.TrumpGame;
import net.mistrio.briscola.repository.GamePlayerRepository;
import net.mistrio.briscola.repository.PlayerRepository;
import net.mistrio.briscola.repository.TrumpGameRepository;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrumpGameService {

    private final TrumpSetup trumpSetup;
    private final TrumpPlayCard trumpPlayCard;
    private final TrumpGameStateToTrumpTable trumpGameStateToTrumpTable;

    private final TrumpGameRepository gameRepository;
    private final GamePlayerRepository gamePlayerRepository;
    private final PlayerRepository playerRepository;

    @Transactional
    public TrumpGame create(Integer playerId) {
        var game = new TrumpGame();
        game.setCreatedAt(Instant.now());
        gameRepository.save(game);

        var gamePlayer = new GamePlayer();
        gamePlayer.setGameId(game.getId());
        gamePlayer.setPlayerId(playerId);
        gamePlayerRepository.save(gamePlayer);

        return game;
    }

    public void join(Integer gameId, Integer playerId) {
        var player = playerRepository.findById(playerId).orElseThrow(IllegalStateException::new);
        var game = gameRepository.findById(gameId).orElseThrow(IllegalStateException::new);

        var gamePlayers = gamePlayerRepository.findByGameId(gameId);

        if (gamePlayers.size() > 1) {
            throw new IllegalStateException("Game is full");
        }

        var playerHasAlreadyJoined =
                gamePlayers.stream()
                        .map(GamePlayer::getPlayerId)
                        .anyMatch(id -> id.equals(playerId));

        if (playerHasAlreadyJoined) {
            throw new IllegalStateException("Player " + playerId + " already joined");
        }

        var gamePlayer = new GamePlayer();
        gamePlayer.setGameId(game.getId());
        gamePlayer.setPlayerId(player.getId());

        gamePlayerRepository.save(gamePlayer);
    }

    public TrumpGame start(Integer gameId) {
        return gameRepository
                .findById(gameId)
                .filter(this::gameIsFull)
                .map(this::prepareGame)
                .map(gameRepository::save)
                .orElseThrow(GameNotFoundException::new);
    }

    public TrumpTable peek(Integer gameId, Integer playerId) {
        return gameRepository
                .findById(gameId)
                .map(TrumpGame::getGameState)
                .map(gs -> trumpGameStateToTrumpTable.convert(gs, playerId))
                .orElseThrow(GameNotFoundException::new);
    }

    public TrumpTable playCard(Integer gameId, Integer playerId, TrumpCard card) {
        return gameRepository
                .findById(gameId)
                .map(game -> playCard(game, playerId, card))
                .map(gameRepository::save)
                .map(TrumpGame::getGameState)
                .map(gs -> trumpGameStateToTrumpTable.convert(gs, playerId))
                .orElseThrow(GameNotFoundException::new);
    }

    public Integer getScore(Integer gameId, Integer playerId) {
        return gameRepository
                .findById(gameId)
                .map(TrumpGame::getGameState)
                .map(gs -> gs.getPlayerStashes().get(playerId))
                .map(TrumpCardStash::getCards)
                .stream()
                .flatMap(Collection::stream)
                .map(TrumpCard::getScore)
                .mapToInt(Integer::intValue)
                .sum();
    }

    private TrumpGame prepareGame(TrumpGame game) {
        var playerIds =
                gamePlayerRepository.findByGameId(game.getId()).stream()
                        .map(GamePlayer::getPlayerId)
                        .collect(Collectors.toList());

        Collections.shuffle(playerIds);
        game.setGameState(trumpSetup.setup(playerIds, getShuffledDeck()));

        return game;
    }

    private boolean gameIsFull(TrumpGame game) {
        var gamePlayers = gamePlayerRepository.findByGameId(game.getId());
        return gamePlayers.size() == 2;
    }

    private TrumpGame playCard(TrumpGame game, Integer playerId, TrumpCard card) {
        var gameState = game.getGameState();
        var trumpState = gameState;

        trumpPlayCard.playCard(trumpState, playerId, card);

        return game;
    }

    private TrumpDeck getShuffledDeck() {
        var deck = new TrumpDeck();
        deck.initialize();
        deck.shuffle();
        return deck;
    }
}
