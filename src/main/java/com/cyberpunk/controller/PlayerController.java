package com.cyberpunk.controller;

import com.cyberpunk.dto.AuthResponse;
import com.cyberpunk.dto.PlayerLoginRequest;
import com.cyberpunk.dto.PlayerRequest;
import com.cyberpunk.dto.PlayerResponse;
import com.cyberpunk.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/players")
@Tag(name = "Players", description = "Player management endpoints")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Operation(summary = "Register a new player")
    @ApiResponse(responseCode = "201", description = "Player created successfully")
    @ApiResponse(responseCode = "409", description = "Name already in use")
    @PostMapping
    public ResponseEntity<AuthResponse> createPlayer(@Valid @RequestBody PlayerRequest request) throws ExecutionException, InterruptedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(playerService.savePlayer(request));
    }

    @Operation(summary = "Login with player credentials")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody PlayerLoginRequest request) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(playerService.login(request));
    }

    @Operation(summary = "Get a player by ID with their Mech garage")
    @ApiResponse(responseCode = "200", description = "Player found")
    @ApiResponse(responseCode = "404", description = "Player not found")
    @GetMapping("/{idPlayer}")
    public ResponseEntity<PlayerResponse> getPlayer(@PathVariable String idPlayer) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(playerService.getPlayerById(idPlayer));
    }

    @Operation(summary = "Delete a player and all their Mechs")
    @ApiResponse(responseCode = "204", description = "Player deleted successfully")
    @ApiResponse(responseCode = "404", description = "Player not found")
    @ApiResponse(responseCode = "403", description = "Not allowed to delete another player")
    @DeleteMapping("/{idPlayer}")
    public ResponseEntity<Void> deletePlayer(@PathVariable String idPlayer,
                                             @RequestAttribute("authPlayerId") String authPlayerId) throws ExecutionException, InterruptedException {
        playerService.deletePlayer(idPlayer, authPlayerId);
        return ResponseEntity.noContent().build();
    }
}