package com.cyberpunk.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DefensiveMech extends Mech {
    private Integer shieldArmor;
    private boolean shieldActive;

    public DefensiveMech(String idMech, String playerId, String model, Integer maxHealth,
                         Integer battery, Integer attackPower, Integer shieldArmor) {
        this.setIdMech(idMech);
        this.setPlayerId(playerId);
        this.setModel(model);
        this.setMaxHealth(maxHealth);
        this.setCurrentHealth(maxHealth == null ? 0 : maxHealth);
        this.setBattery(battery);
        this.setAttackPower(attackPower);
        this.shieldArmor = shieldArmor;
        this.shieldActive = false;
        this.setType("DEFENSIVE");
    }

    public void toggleShield() {
        this.shieldActive = !this.shieldActive;
    }

    @Override
    public void takeDamage(Integer damage) {
        int dmg = (damage == null) ? 0 : damage;
        if (shieldActive && shieldArmor != null && shieldArmor > 0) {
            if (dmg <= shieldArmor) {
                shieldArmor -= dmg;
            } else {
                int rest = dmg - shieldArmor;
                shieldArmor = 0;
                super.takeDamage(rest);
            }
        } else {
            super.takeDamage(dmg);
        }
    }
}
