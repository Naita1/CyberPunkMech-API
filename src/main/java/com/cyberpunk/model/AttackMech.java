package com.cyberpunk.model;

public class AttackMech extends Mech{
    private Integer heatLevel;
    private Integer maxHeat;

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
        this.maxHeat = maxHeat;
        this.heatLevel = 0;
        this.setType("ATTACK");
    }

    public Integer getHeatLevel() {
        return heatLevel;
    }

    public void setHeatLevel(Integer heatLevel) {
        this.heatLevel = heatLevel;
    }

    public Integer getMaxHeat() {
        return maxHeat;
    }

    public void setMaxHeat(Integer maxHeat) {
        this.maxHeat = maxHeat;
    }

    public Integer shoot(){
        if(heatLevel >= maxHeat){
            return 0;
        }
        int damage = this.getAttackPower() * 2;
        heatLevel += 25;

        return damage;
    }

    public void coolDown() {
        this.heatLevel = Math.max(0, this.heatLevel - 30);
    }


}
