package com.cyberpunk.controller;

import com.cyberpunk.dto.AttackMechRequest;
import com.cyberpunk.dto.AttackMechResponse;
import com.cyberpunk.dto.DefensiveMechRequest;
import com.cyberpunk.dto.DefensiveMechResponse;
import com.cyberpunk.model.Mech;
import com.cyberpunk.service.MechService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/mechs")
@Tag(name = "Mechs", description = "Mech management endpoints")
public class MechController {

    private final MechService mechService;

    public MechController(MechService mechService) {
        this.mechService = mechService;
    }

    @Operation(summary = "Register an Attack Mech")
    @ApiResponse(responseCode = "201", description = "Attack Mech created successfully")
    @PostMapping("/attack")
    public ResponseEntity<AttackMechResponse> createAttackMech(@Valid @RequestBody AttackMechRequest request) throws ExecutionException, InterruptedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(mechService.saveAttackMech(request));
    }

    @Operation(summary = "Register a Defensive Mech")
    @ApiResponse(responseCode = "201", description = "Defensive Mech created successfully")
    @PostMapping("/defensive")
    public ResponseEntity<DefensiveMechResponse> createDefensiveMech(@Valid @RequestBody DefensiveMechRequest request) throws ExecutionException, InterruptedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(mechService.saveDefensiveMech(request));
    }

    @Operation(summary = "Get a Mech by ID")
    @ApiResponse(responseCode = "200", description = "Mech found")
    @ApiResponse(responseCode = "404", description = "Mech not found")
    @GetMapping("/{idMech}")
    public ResponseEntity<Mech> getMech(@PathVariable String idMech) throws ExecutionException, InterruptedException {
        Mech mech = mechService.getMechById(idMech);
        if (mech == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mech);
    }

    @Operation(summary = "List all Mechs belonging to a player")
    @ApiResponse(responseCode = "200", description = "List returned successfully")
    @GetMapping
    public ResponseEntity<List<Mech>> getMechByPlayer(@RequestParam String playerId) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(mechService.getMechsByPlayerId(playerId));
    }

    @Operation(summary = "Delete a Mech by ID")
    @ApiResponse(responseCode = "204", description = "Mech deleted successfully")
    @DeleteMapping("/{idMech}")
    public ResponseEntity<Void> deleteMech(@PathVariable String idMech) throws ExecutionException, InterruptedException {
        mechService.deleteMech(idMech);
        return ResponseEntity.noContent().build();
    }
}
