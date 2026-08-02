package com.cyberpunk.model;

public class DefensiveMech extends Mech{
    private Integer shieldArmor;
    private boolean isShieldActive;

    public DefensiveMech(){
        super();
    }

    public DefensiveMech(String idMech, String playerId, String model, Integer maxHealth,
                         Integer battery, Integer attackPower, Integer shieldArmor) {
        super();
        this.setIdMech(idMech);
        this.setPlayerId(playerId);
        this.setModel(model);
        this.setMaxHealth(maxHealth);
        this.setCurrentHealth(maxHealth);
        this.setBattery(battery);
        this.setAttackPower(attackPower);
        this.shieldArmor = shieldArmor;
        this.isShieldActive = false;
        this.setType("DEFENSIVE");
    }

    public Integer getShieldArmor() {
        return shieldArmor;
    }

    public void setShieldArmor(Integer shieldArmor) {
        this.shieldArmor = shieldArmor;
    }

    public boolean isShieldActive() {
        return isShieldActive;
    }

    public void setShieldActive(boolean shieldActive) {
        isShieldActive = shieldActive;
    }
    public void toggleShield(){
        this.setShieldActive(!this.isShieldActive);
    }

    @Override
    public void takeDamage(Integer damage) {
        if (isShieldActive && shieldArmor != null && shieldArmor > 0) {
            if (damage <= shieldArmor) {
                shieldArmor -= damage;
            } else {
                int rest = damage - shieldArmor;
                shieldArmor = 0;
                super.takeDamage(rest);
            }
        } else {
            super.takeDamage(damage); 
        }
    }
}
