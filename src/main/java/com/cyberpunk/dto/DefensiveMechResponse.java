package com.cyberpunk.dto;

import com.cyberpunk.model.DefensiveMech;

public record DefensiveMechResponse(
        String idMech,
        String playerId,
        String model,
        String type,
        Integer maxHealth,
        Integer currentHealth,
        Integer battery,
        Integer attackPower,
        Integer shieldArmor,
        boolean shieldActive
) {
    public static DefensiveMechResponse from(DefensiveMech mech) {
        return new DefensiveMechResponse(
                mech.getIdMech(),
                mech.getPlayerId(),
                mech.getModel(),
                mech.getType(),
                mech.getMaxHealth(),
                mech.getCurrentHealth(),
                mech.getBattery(),
                mech.getAttackPower(),
                mech.getShieldArmor(),
                mech.isShieldActive()
        );
    }
}
