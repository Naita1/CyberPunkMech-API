package com.cyberpunk.repository;

import com.cyberpunk.model.Player;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface PlayerRepository {
    void save(Player player) throws ExecutionException, InterruptedException;
    Optional<Player> findById(String idPlayer) throws ExecutionException, InterruptedException;
    Optional<Player> findByName(String namePlayer) throws ExecutionException, InterruptedException;
    boolean existsById(String idPlayer) throws ExecutionException, InterruptedException;
    void deleteById(String idPlayer) throws ExecutionException, InterruptedException;
}