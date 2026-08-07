package com.cyberpunk.service;

import com.cyberpunk.dto.PlayerLoginRequest;
import com.cyberpunk.dto.PlayerRequest;
import com.cyberpunk.dto.PlayerResponse;
import com.cyberpunk.exception.PlayerNotFoundException;
import com.cyberpunk.model.Player;
import com.cyberpunk.repository.MechRepository;
import com.cyberpunk.repository.PlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final MechRepository mechRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public PlayerService(PlayerRepository playerRepository, MechRepository mechRepository) {
        this.playerRepository = playerRepository;
        this.mechRepository = mechRepository;
    }

    public PlayerResponse savePlayer(PlayerRequest request) throws ExecutionException, InterruptedException {
        if (playerRepository.findByName(request.namePlayer()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Nome já está em uso.");
        }

        Player player = new Player();
        player.setIdPlayer(UUID.randomUUID().toString());
        player.setNamePlayer(request.namePlayer());
        player.setPassword(passwordEncoder.encode(request.password()));
        if (request.coins() != null) player.setCoins(request.coins());

        playerRepository.save(player);
        return PlayerResponse.from(player);
    }

    public PlayerResponse login(PlayerLoginRequest request) throws ExecutionException, InterruptedException {
        Player player = playerRepository.findByName(request.namePlayer())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Piloto ou senha inválidos."));

        if (!passwordEncoder.matches(request.password(), player.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Piloto ou senha inválidos.");
        }

        player.setGarage(mechRepository.findByPlayerId(player.getIdPlayer()));
        return PlayerResponse.from(player);
    }

    public PlayerResponse getPlayerById(String idPlayer) throws ExecutionException, InterruptedException {
        Player player = playerRepository.findById(idPlayer)
                .orElseThrow(() -> new PlayerNotFoundException(idPlayer));

        player.setGarage(mechRepository.findByPlayerId(player.getIdPlayer()));
        return PlayerResponse.from(player);
    }

    public void deletePlayer(String idPlayer) throws ExecutionException, InterruptedException {
        if (!playerRepository.existsById(idPlayer)) {
            throw new PlayerNotFoundException(idPlayer);
        }
        mechRepository.deleteAllById(mechRepository.findByPlayerId(idPlayer)
                .stream()
                .map(com.cyberpunk.model.Mech::getIdMech)
                .toList());
        playerRepository.deleteById(idPlayer);
    }
}