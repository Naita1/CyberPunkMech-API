package com.cyberpunk.exception;

public class MechNotFoundException extends RuntimeException {
    public MechNotFoundException(String idMech) {
        super("Mech não encontrado: " + idMech);
    }
}
