package com.cyberpunk.service;

import com.cyberpunk.model.AttackMech;
import com.cyberpunk.model.DefensiveMech;
import com.cyberpunk.model.Mech;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class MechService {
    private static final String COLLECTION_NAME = "Mechs";
    private final Firestore firestore;

    public MechService(Firestore firestore) {
        this.firestore = firestore;
    }

    private CollectionReference getCollection() {
        return firestore.collection(COLLECTION_NAME);
    }

    public void saveMech(Mech mech) throws ExecutionException, InterruptedException {
        getCollection().document(mech.getIdMech()).set(mech).get();
    }

    public Mech getMechById(String idMech) throws ExecutionException, InterruptedException {
        DocumentSnapshot snapshot = getCollection().document(idMech).get().get();

        if (!snapshot.exists()) {
            return null;
        }

        return convertToMech(snapshot);
    }

    public List<Mech> getMechsByPlayerId(String idPlayer) throws ExecutionException, InterruptedException {
        List<Mech> mechs = new ArrayList<>();
        List<QueryDocumentSnapshot> docs = getCollection()
                .whereEqualTo("playerId", idPlayer)
                .get()
                .get()
                .getDocuments();

        for(QueryDocumentSnapshot doc : docs) {
            mechs.add(convertToMech(doc));
        }
        return mechs;
    }

    public void deleteMech(String idMech) throws ExecutionException, InterruptedException {
        getCollection().document(idMech).delete().get();
    }

    private Mech convertToMech(DocumentSnapshot snapshot){
        String type = snapshot.getString("type");

        if(type == null){
            throw  new IllegalArgumentException("Documento sem campo 'type: " + snapshot.getId());

        }

        switch (type){
            case "ATTACK":
                return snapshot.toObject(AttackMech.class);
            case "DEFENSIVE":
                return snapshot.toObject(DefensiveMech.class);
            default:
                throw new IllegalArgumentException("Tipo de Mech desconhecido " + snapshot.getId());
        }
    }

}

