package net.mistrio.briscola.service;

import lombok.RequiredArgsConstructor;

import net.mistrio.briscola.model.Player;
import net.mistrio.briscola.repository.PlayerRepository;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    public Player create(String name) {
        var player = new Player();
        player.setName(name);
        player.setCreatedAt(Instant.now());
        return playerRepository.save(player);
    }

    public void delete(Integer playerId) {
        playerRepository.deleteById(playerId);
    }

    public void update(Integer playerId, String name) {
        playerRepository
                .findById(playerId)
                .map(player -> updateName(player, name))
                .ifPresent(playerRepository::save);
    }

    public Optional<Player> findById(Integer playerId) {
        return playerRepository.findById(playerId);
    }

    private Player updateName(Player player, String name) {
        player.setName(name);
        return player;
    }
}
