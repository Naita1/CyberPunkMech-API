package com.cyberpunk.controller;

import com.cyberpunk.dto.AttackMechRequest;
import com.cyberpunk.dto.AttackMechResponse;
import com.cyberpunk.dto.DefensiveMechRequest;
import com.cyberpunk.dto.DefensiveMechResponse;
import com.cyberpunk.model.Mech;
import com.cyberpunk.service.MechService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/mechs")
@CrossOrigin(origins = "*")
public class MechController {

    private final MechService mechService;

    public MechController(MechService mechService) {
        this.mechService = mechService;
    }

    @PostMapping("/attack")
    public ResponseEntity<AttackMechResponse> createAttackMech(@Valid @RequestBody AttackMechRequest request) throws ExecutionException, InterruptedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(mechService.saveAttackMech(request));
    }

    @PostMapping("/defensive")
    public ResponseEntity<DefensiveMechResponse> createDefensiveMech(@Valid @RequestBody DefensiveMechRequest request) throws ExecutionException, InterruptedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(mechService.saveDefensiveMech(request));
    }

    @GetMapping("/{idMech}")
    public ResponseEntity<Mech> getMech(@PathVariable String idMech) throws ExecutionException, InterruptedException {
        Mech mech = mechService.getMechById(idMech);
        if (mech == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mech);
    }

    @GetMapping
    public ResponseEntity<List<Mech>> getMechByPlayer(@RequestParam String playerId) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(mechService.getMechsByPlayerId(playerId));
    }

    @DeleteMapping("/{idMech}")
    public ResponseEntity<Void> deleteMech(@PathVariable String idMech) throws ExecutionException, InterruptedException {
        mechService.deleteMech(idMech);
        return ResponseEntity.noContent().build();
    }
}
