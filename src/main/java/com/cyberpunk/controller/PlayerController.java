package com.cyberpunk.controller;

import com.cyberpunk.dto.PlayerRequest;
import com.cyberpunk.dto.PlayerResponse;
import com.cyberpunk.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/players")
@CrossOrigin(origins = "*")
@Tag(name = "Players", description = "Player management endpoints")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Operation(summary = "Register a new player")
    @ApiResponse(responseCode = "201", description = "Player created successfully")
    @PostMapping
    public ResponseEntity<PlayerResponse> createPlayer(@Valid @RequestBody PlayerRequest request) throws ExecutionException, InterruptedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(playerService.savePlayer(request));
    }

    @Operation(summary = "Get a player by ID with their Mech garage")
    @ApiResponse(responseCode = "200", description = "Player found")
    @ApiResponse(responseCode = "404", description = "Player not found")
    @GetMapping("/{idPlayer}")
    public ResponseEntity<PlayerResponse> getPlayer(@PathVariable String idPlayer) throws ExecutionException, InterruptedException {
        PlayerResponse player = playerService.getPlayerById(idPlayer);
        if (player == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(player);
    }

    @Operation(summary = "Delete a player and all their Mechs")
    @ApiResponse(responseCode = "204", description = "Player deleted successfully")
    @DeleteMapping("/{idPlayer}")
    public ResponseEntity<Void> deletePlayer(@PathVariable String idPlayer) throws ExecutionException, InterruptedException {
        playerService.deletePlayer(idPlayer);
        return ResponseEntity.noContent().build();
    }
}
