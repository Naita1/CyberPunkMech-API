package com.cyberpunk.service;

import com.cyberpunk.dto.AttackMechRequest;
import com.cyberpunk.dto.AttackMechResponse;
import com.cyberpunk.dto.DefensiveMechRequest;
import com.cyberpunk.dto.DefensiveMechResponse;
import com.cyberpunk.exception.MechNotFoundException;
import com.cyberpunk.model.AttackMech;
import com.cyberpunk.model.DefensiveMech;
import com.cyberpunk.model.Mech;
import com.cyberpunk.repository.MechRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class MechService {

    private final MechRepository mechRepository;

    public MechService(MechRepository mechRepository) {
        this.mechRepository = mechRepository;
    }

    public AttackMechResponse saveAttackMech(AttackMechRequest request, String playerId) throws ExecutionException, InterruptedException {
        AttackMech mech = new AttackMech(
                UUID.randomUUID().toString(), playerId, request.model(),
                request.maxHealth(), request.battery(), request.attackPower(), request.maxHeat()
        );
        mechRepository.save(mech);
        return AttackMechResponse.from(mech);
    }

    public DefensiveMechResponse saveDefensiveMech(DefensiveMechRequest request, String playerId) throws ExecutionException, InterruptedException {
        DefensiveMech mech = new DefensiveMech(
                UUID.randomUUID().toString(), playerId, request.model(),
                request.maxHealth(), request.battery(), request.attackPower(), request.shieldArmor()
        );
        mechRepository.save(mech);
        return DefensiveMechResponse.from(mech);
    }

    public Mech getMechById(String idMech) throws ExecutionException, InterruptedException {
        return mechRepository.findById(idMech)
                .orElseThrow(() -> new MechNotFoundException(idMech));
    }

    public List<Mech> getMechsByPlayerId(String idPlayer) throws ExecutionException, InterruptedException {
        return mechRepository.findByPlayerId(idPlayer);
    }

    public void deleteMech(String idMech, String authPlayerId) throws ExecutionException, InterruptedException {
        Mech mech = mechRepository.findById(idMech)
                .orElseThrow(() -> new MechNotFoundException(idMech));

        if (!mech.getPlayerId().equals(authPlayerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não pode remover um mech de outro piloto.");
        }

        mechRepository.deleteById(idMech);
    }

    public void deleteMechsByPlayerId(String idPlayer) throws ExecutionException, InterruptedException {
        List<Mech> mechs = mechRepository.findByPlayerId(idPlayer);
        List<String> idMechs = mechs.stream()
                .map(Mech::getIdMech)
                .toList();
        mechRepository.deleteAllById(idMechs);
    }
}