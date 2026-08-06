package com.cyberpunk.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class Mech {
    private String idMech;
    private String playerId;
    private String model;
    private Integer maxHealth;
    private int currentHealth;
    private Integer battery;
    private Integer attackPower;
    private String type;

    public void takeDamage(Integer damage) {
        int dmg = (damage == null) ? 0 : damage;
        this.currentHealth = Math.max(0, this.currentHealth - dmg);
    }

    public boolean isOperational() {
        return this.currentHealth > 0;
    }
}
