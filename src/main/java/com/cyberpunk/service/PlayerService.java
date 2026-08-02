package com.cyberpunk.service;

import com.cyberpunk.model.Player;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
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

    public void savePlayer(Player player) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(player.getIdPlayer()).set(player).get();
    }

    public Player getPlayerById(String idPlayer) throws ExecutionException, InterruptedException {
        DocumentSnapshot snapshot = firestore.collection(COLLECTION_NAME).document(idPlayer).get().get();

        if(!snapshot.exists()){
            return null;
        }

        Player player = snapshot.toObject(Player.class);
        if(player != null){
            player.setGarage(mechService.getMechsByPlayerId(player.getIdPlayer()));
        }

        return player;
    }

    public void deletePlayer(String idPlayer) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(idPlayer).delete().get();
    }


}
