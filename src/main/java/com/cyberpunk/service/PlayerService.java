package com.cyberpunk.service;

import java.util.List;
import com.cyberpunk.dto.PlayerRequest;
import com.cyberpunk.dto.PlayerResponse;
import com.cyberpunk.exception.PlayerNotFoundException;
import com.cyberpunk.model.Mech;
import com.cyberpunk.model.Player;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteBatch;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class PlayerService {

    private static final String COLLECTION_NAME = "players";
    private final Firestore firestore;
    private final MechService mechService;

    public PlayerService(Firestore firestore, MechService mechService) {
        this.firestore = firestore;
        this.mechService = mechService;
    }

    public PlayerResponse savePlayer(PlayerRequest request) throws ExecutionException, InterruptedException {
        Player player = new Player();
        player.setIdPlayer(request.idPlayer());
        player.setNamePlayer(request.namePlayer());
        if (request.coins() != null) player.setCoins(request.coins());

        firestore.collection(COLLECTION_NAME).document(player.getIdPlayer()).set(player).get();
        return PlayerResponse.from(player);
    }

    public PlayerResponse getPlayerById(String idPlayer) throws ExecutionException, InterruptedException {
        DocumentSnapshot snapshot = firestore.collection(COLLECTION_NAME).document(idPlayer).get().get();

        if (!snapshot.exists()) {
            throw new PlayerNotFoundException(idPlayer);
        }

        Player player = snapshot.toObject(Player.class);
        player.setGarage(mechService.getMechsByPlayerId(player.getIdPlayer()));
        return PlayerResponse.from(player);
    }

    public void deletePlayer(String idPlayer) throws ExecutionException, InterruptedException {
        DocumentSnapshot snapshot = firestore.collection(COLLECTION_NAME).document(idPlayer).get().get();
        if (!snapshot.exists()) {
            throw new PlayerNotFoundException(idPlayer);
        }

        List<Mech> mechs = mechService.getMechsByPlayerId(idPlayer);

        WriteBatch batch = firestore.batch();
        mechs.forEach(mech -> batch.delete(firestore.collection("mechs").document(mech.getIdMech())));
        batch.delete(firestore.collection(COLLECTION_NAME).document(idPlayer));
        batch.commit().get();
    }


}
