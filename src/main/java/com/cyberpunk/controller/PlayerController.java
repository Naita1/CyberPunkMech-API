package com.cyberpunk.controller;

import com.cyberpunk.dto.PlayerRequest;
import com.cyberpunk.dto.PlayerResponse;
import com.cyberpunk.service.PlayerService;
import jakarta.validation.Valid;
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
    public ResponseEntity<PlayerResponse> createPlayer(@Valid @RequestBody PlayerRequest request) throws ExecutionException, InterruptedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(playerService.savePlayer(request));
    }

    @GetMapping("/{idPlayer}")
    public ResponseEntity<PlayerResponse> getPlayer(@PathVariable String idPlayer) throws ExecutionException, InterruptedException {
        PlayerResponse player = playerService.getPlayerById(idPlayer);
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
