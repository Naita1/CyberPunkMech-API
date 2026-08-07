package com.cyberpunk.dto;

import com.cyberpunk.model.AttackMech;

public record AttackMechSummaryResponse(
        String idMech,
        String playerId,
        String model,
        Integer maxHealth,
        int currentHealth,
        Integer battery,
        Integer attackPower,
        String type,
        int heatLevel,
        int maxHeat
) implements MechSummaryResponse {

    public static AttackMechSummaryResponse from(AttackMech mech) {
        return new AttackMechSummaryResponse(
                mech.getIdMech(),
                mech.getPlayerId(),
                mech.getModel(),
                mech.getMaxHealth(),
                mech.getCurrentHealth(),
                mech.getBattery(),
                mech.getAttackPower(),
                mech.getType(),
                mech.getHeatLevel(),
                mech.getMaxHeat()
        );
    }
}