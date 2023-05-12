package net.mistrio.briscola.repository;

import net.mistrio.briscola.model.GamePlayer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GamePlayerRepository extends JpaRepository<GamePlayer, Integer> {
    List<GamePlayer> findByGameId(Integer gameId);

    List<GamePlayer> findByPlayerId(Integer playerId);
}
