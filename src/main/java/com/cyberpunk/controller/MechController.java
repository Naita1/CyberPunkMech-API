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
    public ResponseEntity<AttackMech> createAttackMech(@RequestBody AttackMech mech) {
        try{
            mech.setType("ATTACK"); 
            mechService.saveMech(mech);
            return ResponseEntity.status(HttpStatus.CREATED).body(mech);
        } catch (ExecutionException | InterruptedException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/defensive")
    public ResponseEntity<DefensiveMech> createDefensiveMech(@RequestBody DefensiveMech mech){
        try{
            mech.setType("DEFENSIVE");
            mechService.saveMech(mech);
            return ResponseEntity.status(HttpStatus.CREATED).body(mech);
        } catch (ExecutionException | InterruptedException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{idMech}")
    public ResponseEntity<Mech> getMech(@PathVariable String idMech){
        try{
            Mech mech = mechService.getMechById(idMech);
            if(mech == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(mech);
        } catch (ExecutionException | InterruptedException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Mech>> getMechByPlayer(@RequestParam String playerId){
        try{
            List<Mech> mechs = mechService.getMechsByPlayerId(playerId);
            return ResponseEntity.ok(mechs);
        } catch (ExecutionException | InterruptedException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("{idMech}")
    public ResponseEntity<Void> deleteMech(@PathVariable String idMech){
        try{
            mechService.deleteMech(idMech);
            return ResponseEntity.noContent().build();
        }
        catch (ExecutionException | InterruptedException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}
