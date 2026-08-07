package com.cyberpunk.repository;

import com.cyberpunk.model.Mech;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface MechRepository {
    void save(Mech mech) throws ExecutionException, InterruptedException;
    Optional<Mech> findById(String idMech) throws ExecutionException, InterruptedException;
    List<Mech> findByPlayerId(String idPlayer) throws ExecutionException, InterruptedException;
    boolean existsById(String idMech) throws ExecutionException, InterruptedException;
    void deleteById(String idMech) throws ExecutionException, InterruptedException;
    void deleteAllById(List<String> idMechs) throws ExecutionException, InterruptedException;
}