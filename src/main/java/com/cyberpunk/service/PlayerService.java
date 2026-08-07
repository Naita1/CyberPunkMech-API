package com.cyberpunk.service;

import com.cyberpunk.dto.PlayerRequest;
import com.cyberpunk.dto.PlayerResponse;
import com.cyberpunk.exception.PlayerNotFoundException;
import com.cyberpunk.model.Player;
import com.cyberpunk.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final MechService mechService;

    public PlayerService(PlayerRepository playerRepository, MechService mechService) {
        this.playerRepository = playerRepository;
        this.mechService = mechService;
    }

    public PlayerResponse savePlayer(PlayerRequest request) throws ExecutionException, InterruptedException {
        Player player = new Player();
        player.setIdPlayer(request.idPlayer());
        player.setNamePlayer(request.namePlayer());
        if (request.coins() != null) player.setCoins(request.coins());

        playerRepository.save(player);
        return PlayerResponse.from(player);
    }

    public PlayerResponse getPlayerById(String idPlayer) throws ExecutionException, InterruptedException {
        Player player = playerRepository.findById(idPlayer)
                .orElseThrow(() -> new PlayerNotFoundException(idPlayer));

        player.setGarage(mechService.getMechsByPlayerId(player.getIdPlayer()));
        return PlayerResponse.from(player);
    }

    public void deletePlayer(String idPlayer) throws ExecutionException, InterruptedException {
        if (!playerRepository.existsById(idPlayer)) {
            throw new PlayerNotFoundException(idPlayer);
        }

        mechService.deleteMechsByPlayerId(idPlayer);
        playerRepository.deleteById(idPlayer);
    }
}