package com.cyberpunk.dto;

import com.cyberpunk.model.AttackMech;

public record AttackMechResponse(
        String idMech,
        String playerId,
        String model,
        String type,
        Integer maxHealth,
        Integer currentHealth,
        Integer battery,
        Integer attackPower,
        Integer heatLevel,
        Integer maxHeat
) {
    public static AttackMechResponse from(AttackMech mech) {
        return new AttackMechResponse(
                mech.getIdMech(),
                mech.getPlayerId(),
                mech.getModel(),
                mech.getType(),
                mech.getMaxHealth(),
                mech.getCurrentHealth(),
                mech.getBattery(),
                mech.getAttackPower(),
                mech.getHeatLevel(),
                mech.getMaxHeat()
        );
    }
}
