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
    public ResponseEntity<Player> createPlayer(@RequestBody Player player){
        try{
            playerService.savePlayer(player);
            return ResponseEntity.status(HttpStatus.CREATED).body(player);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{idPlayer}")
    public ResponseEntity<Player> getPlayer(@PathVariable String idPlayer){
        try{
            Player player = playerService.getPlayerById(idPlayer);

            if (player == null){
                return ResponseEntity.notFound().build();
            }
            return  ResponseEntity.ok(player);
        } catch (ExecutionException | InterruptedException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{idPlayer}")
    public ResponseEntity<Void> deletePlayer(@PathVariable String idPlayer){
        try{
            playerService.deletePlayer(idPlayer);
            return ResponseEntity.noContent().build();
        } catch (ExecutionException | InterruptedException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
