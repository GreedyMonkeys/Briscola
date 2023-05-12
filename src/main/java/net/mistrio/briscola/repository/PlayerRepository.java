package net.mistrio.briscola.repository;

import net.mistrio.briscola.model.Player;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Integer> {}
