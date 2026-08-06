package com.cyberpunk.controller;

import com.cyberpunk.model.Player;
import com.cyberpunk.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/players")
@CrossOrigin(origins = "*")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) throws ExecutionException, InterruptedException {
        playerService.savePlayer(player);
        return ResponseEntity.status(HttpStatus.CREATED).body(player);
    }

    @GetMapping("/{idPlayer}")
    public ResponseEntity<Player> getPlayer(@PathVariable String idPlayer) throws ExecutionException, InterruptedException {
        Player player = playerService.getPlayerById(idPlayer);
        if (player == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(player);
    }

    @DeleteMapping("/{idPlayer}")
    public ResponseEntity<Void> deletePlayer(@PathVariable String idPlayer) throws ExecutionException, InterruptedException {
        playerService.deletePlayer(idPlayer);
        return ResponseEntity.noContent().build();
    }

}