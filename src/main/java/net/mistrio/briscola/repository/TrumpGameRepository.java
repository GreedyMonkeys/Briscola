package net.mistrio.briscola.repository;

import net.mistrio.briscola.model.TrumpGame;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TrumpGameRepository extends JpaRepository<TrumpGame, Integer> {}
