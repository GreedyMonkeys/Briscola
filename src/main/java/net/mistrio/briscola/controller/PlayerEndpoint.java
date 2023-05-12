package net.mistrio.briscola.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import net.mistrio.briscola.model.Player;
import net.mistrio.briscola.service.PlayerService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Data
class PlayerCreateRequest {
    private String name;
}

@RestController
@RequestMapping("players")
@RequiredArgsConstructor
public class PlayerEndpoint {

    private final PlayerService playerService;

    // create player
    @PostMapping
    public Player create(@RequestBody PlayerCreateRequest playerCreateRequest) {
        return playerService.create(playerCreateRequest.getName());
    }
}
