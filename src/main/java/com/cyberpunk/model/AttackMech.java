package com.cyberpunk.model;

public class AttackMech extends Mech{
    private int heatLevel = 0;
    private int maxHeat;

    public AttackMech(){
        super();
    }

    public AttackMech(String idMech, String playerId, String model, Integer maxHealth,
                      Integer battery, Integer attackPower, Integer maxHeat) {
        super();
        this.setIdMech(idMech);
        this.setPlayerId(playerId);
        this.setModel(model);
        this.setMaxHealth(maxHealth);
        this.setCurrentHealth(maxHealth);
        this.setBattery(battery);
        this.setAttackPower(attackPower);
        this.maxHeat = (maxHeat == null) ? 0 : maxHeat;
        this.heatLevel = 0;
        this.setType("ATTACK");
    }

    public int getHeatLevel() {
        return heatLevel;
    }

    public void setHeatLevel(int heatLevel) {
        this.heatLevel = heatLevel;
    }

    public int getMaxHeat() {
        return maxHeat;
    }

    public void setMaxHeat(int maxHeat) {
        this.maxHeat = maxHeat;
    }

    public Integer shoot(){
        if(heatLevel >= maxHeat){
            return 0;
        }
        Integer attackPower = this.getAttackPower();
        int power = (attackPower == null) ? 0 : attackPower;
        int damage = power * 2;
        heatLevel += 25;

        return damage;
    }

    public void coolDown() {
        this.heatLevel = Math.max(0, this.heatLevel - 30);
    }
}