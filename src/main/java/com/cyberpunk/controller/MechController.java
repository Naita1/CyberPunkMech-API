package com.cyberpunk.controller;

import com.cyberpunk.model.AttackMech;
import com.cyberpunk.model.DefensiveMech;
import com.cyberpunk.model.Mech;
import com.cyberpunk.service.MechService;
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
    public ResponseEntity<AttackMech> createAttackMech(@RequestBody AttackMech mech) throws ExecutionException, InterruptedException {
        mech.setType("ATTACK");
        mechService.saveMech(mech);
        return ResponseEntity.status(HttpStatus.CREATED).body(mech);
    }

    @PostMapping("/defensive")
    public ResponseEntity<DefensiveMech> createDefensiveMech(@RequestBody DefensiveMech mech) throws ExecutionException, InterruptedException {
        mech.setType("DEFENSIVE");
        mechService.saveMech(mech);
        return ResponseEntity.status(HttpStatus.CREATED).body(mech);
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
        List<Mech> mechs = mechService.getMechsByPlayerId(playerId);
        return ResponseEntity.ok(mechs);
    }

    @DeleteMapping("{idMech}")
    public ResponseEntity<Void> deleteMech(@PathVariable String idMech) throws ExecutionException, InterruptedException {
        mechService.deleteMech(idMech);
        return ResponseEntity.noContent().build();
    }

}