package com.cyberpunk.exception;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String idPlayer) {
        super("Player não encontrado: " + idPlayer);
    }
}
