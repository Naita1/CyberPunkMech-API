package com.cyberpunk.dto;

import com.cyberpunk.model.DefensiveMech;

public record DefensiveMechSummaryResponse(
        String idMech,
        String playerId,
        String model,
        Integer maxHealth,
        int currentHealth,
        Integer battery,
        Integer attackPower,
        String type,
        Integer shieldArmor,
        boolean shieldActive
) implements MechSummaryResponse {

    public static DefensiveMechSummaryResponse from(DefensiveMech mech) {
        return new DefensiveMechSummaryResponse(
                mech.getIdMech(),
                mech.getPlayerId(),
                mech.getModel(),
                mech.getMaxHealth(),
                mech.getCurrentHealth(),
                mech.getBattery(),
                mech.getAttackPower(),
                mech.getType(),
                mech.getShieldArmor(),
                mech.isShieldActive()
        );
    }
}