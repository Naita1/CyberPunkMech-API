package com.cyberpunk.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AttackMech extends Mech {
    private int heatLevel;
    private int maxHeat;

    public AttackMech(String idMech, String playerId, String model, Integer maxHealth,
                      Integer battery, Integer attackPower, Integer maxHeat) {
        this.setIdMech(idMech);
        this.setPlayerId(playerId);
        this.setModel(model);
        this.setMaxHealth(maxHealth);
        this.setCurrentHealth(maxHealth == null ? 0 : maxHealth);
        this.setBattery(battery);
        this.setAttackPower(attackPower);
        this.maxHeat = (maxHeat == null) ? 0 : maxHeat;
        this.heatLevel = 0;
        this.setType("ATTACK");
    }

    @Override
    public int attack() {
        if (heatLevel >= maxHeat) {
            return 0;
        }
        int damage = (getAttackPower() == null ? 0 : getAttackPower()) * 2;
        heatLevel += 25;
        return damage;
    }

    public void coolDown() {
        this.heatLevel = Math.max(0, this.heatLevel - 30);
    }
}
