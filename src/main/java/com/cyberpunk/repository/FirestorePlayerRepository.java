package com.cyberpunk.repository;

import com.cyberpunk.model.Player;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
public class FirestorePlayerRepository implements PlayerRepository {

    private static final String COLLECTION_NAME = "players";
    private final Firestore firestore;

    public FirestorePlayerRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    private CollectionReference getCollection() {
        return firestore.collection(COLLECTION_NAME);
    }

    @Override
    public void save(Player player) throws ExecutionException, InterruptedException {
        getCollection().document(player.getIdPlayer()).set(player).get();
    }

    @Override
    public Optional<Player> findById(String idPlayer) throws ExecutionException, InterruptedException {
        DocumentSnapshot snapshot = getCollection().document(idPlayer).get().get();
        if (!snapshot.exists()) {
            return Optional.empty();
        }
        return Optional.ofNullable(snapshot.toObject(Player.class));
    }

    @Override
    public boolean existsById(String idPlayer) throws ExecutionException, InterruptedException {
        return getCollection().document(idPlayer).get().get().exists();
    }

    @Override
    public void deleteById(String idPlayer) throws ExecutionException, InterruptedException {
        getCollection().document(idPlayer).delete().get();
    }
}