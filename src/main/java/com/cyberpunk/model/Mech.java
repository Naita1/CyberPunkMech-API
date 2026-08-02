package com.cyberpunk.model;

public abstract class Mech {
    private String idMech;
    private String playerId;
    private String model;
    private Integer maxHealth;
    private Integer currentHealth;
    private Integer battery;
    private Integer attackPower;
    private String type;

    public Mech() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getIdMech() {
        return idMech;
    }

    public void setIdMech(String idMech) {
        this.idMech = idMech;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(Integer maxHealth) {
        this.maxHealth = maxHealth;
    }

    public Integer getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(Integer currentHealth) {
        this.currentHealth = currentHealth;
    }

    public Integer getBattery() {
        return battery;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }

    public Integer getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(Integer attackPower) {
        this.attackPower = attackPower;
    }

    public void takeDamage(Integer damage){
        this.currentHealth = Math.max(0, this.currentHealth - damage);
    }

    public boolean isOperational(){
        return this.currentHealth > 0;
    }
}