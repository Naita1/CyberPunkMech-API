package com.cyberpunk.repository;

import com.cyberpunk.model.AttackMech;
import com.cyberpunk.model.DefensiveMech;
import com.cyberpunk.model.Mech;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.WriteBatch;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Repository
public class FirestoreMechRepository implements MechRepository {

    private static final String COLLECTION_NAME = "mechs";
    private final Firestore firestore;

    public FirestoreMechRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    private CollectionReference getCollection() {
        return firestore.collection(COLLECTION_NAME);
    }

    @Override
    public void save(Mech mech) throws ExecutionException, InterruptedException {
        getCollection().document(mech.getIdMech()).set(mech).get();
    }

    @Override
    public Optional<Mech> findById(String idMech) throws ExecutionException, InterruptedException {
        DocumentSnapshot snapshot = getCollection().document(idMech).get().get();
        if (!snapshot.exists()) {
            return Optional.empty();
        }
        return Optional.of(convertToMech(snapshot));
    }

    @Override
    public List<Mech> findByPlayerId(String idPlayer) throws ExecutionException, InterruptedException {
        List<Mech> mechs = new ArrayList<>();
        List<QueryDocumentSnapshot> docs = getCollection()
                .whereEqualTo("playerId", idPlayer)
                .get()
                .get()
                .getDocuments();

        for (QueryDocumentSnapshot doc : docs) {
            mechs.add(convertToMech(doc));
        }
        return mechs;
    }

    @Override
    public boolean existsById(String idMech) throws ExecutionException, InterruptedException {
        return getCollection().document(idMech).get().get().exists();
    }

    @Override
    public void deleteById(String idMech) throws ExecutionException, InterruptedException {
        getCollection().document(idMech).delete().get();
    }

    @Override
    public void deleteAllById(List<String> idMechs) throws ExecutionException, InterruptedException {
        if (idMechs.isEmpty()) {
            return;
        }
        WriteBatch batch = firestore.batch();
        idMechs.forEach(id -> batch.delete(getCollection().document(id)));
        batch.commit().get();
    }

    private Mech convertToMech(DocumentSnapshot snapshot) {
        String type = snapshot.getString("type");

        if (type == null) {
            throw new IllegalArgumentException("Documento sem campo 'type': " + snapshot.getId());
        }

        return switch (type) {
            case "ATTACK" -> snapshot.toObject(AttackMech.class);
            case "DEFENSIVE" -> snapshot.toObject(DefensiveMech.class);
            default -> throw new IllegalArgumentException("Tipo de Mech desconhecido: " + snapshot.getId());
        };
    }
}