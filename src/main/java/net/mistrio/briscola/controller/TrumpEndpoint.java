package net.mistrio.briscola.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import net.mistrio.briscola.dto.TrumpTable;
import net.mistrio.briscola.model.TrumpCard;
import net.mistrio.briscola.model.TrumpGame;
import net.mistrio.briscola.service.TrumpGameService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Data
class TrumpPlay {
    private Integer gameId;
    private Integer playerId;
    private TrumpCard trumpCard;
}

@RestController
@RequestMapping("trump")
@RequiredArgsConstructor
public class TrumpEndpoint {

    private final TrumpGameService trumpGameService;

    @PostMapping("create/{playerId}")
    public TrumpGame create(@PathVariable Integer playerId) {
        return trumpGameService.create(playerId);
    }

    @PostMapping("join/{gameId}/{playerId}")
    public void join(@PathVariable Integer gameId, @PathVariable Integer playerId) {
        trumpGameService.join(gameId, playerId);
    }

    @PostMapping("start/{gameId}")
    public TrumpGame start(@PathVariable Integer gameId) {
        return trumpGameService.start(gameId);
    }

    @GetMapping("peek/{gameId}/{playerId}")
    public TrumpTable peek(@PathVariable Integer gameId, @PathVariable Integer playerId) {
        return trumpGameService.peek(gameId, playerId);
    }

    @GetMapping("score/{gameId}/{playerId}")
    public Integer getScore(@PathVariable Integer gameId, @PathVariable Integer playerId) {
        return trumpGameService.getScore(gameId, playerId);
    }

    @PostMapping("play-card")
    public TrumpTable playCard(@RequestBody TrumpPlay play) {
        return trumpGameService.playCard(play.getGameId(), play.getPlayerId(), play.getTrumpCard());
    }
}
