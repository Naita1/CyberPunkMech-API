package com.cyberpunk.model;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.Exclude;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String idPlayer;
    private String namePlayer;
    private Integer coins;
    private Integer wins;
    private Integer draws;
    private Integer losses;
    private Timestamp createdAt;
    private List<Mech> garage;

    public Player() {
        this.coins = 0;
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
        this.garage = new ArrayList<>();
    }

    public Integer getMechsCount() {
        return this.garage != null ? this.garage.size() : 0;
    }

    public Integer getWins() { return wins; }
    public void setWins(Integer wins) { this.wins = wins; }

    public Integer getDraws() { return draws; }
    public void setDraws(Integer draws) { this.draws = draws; }

    public Integer getLosses() { return losses; }
    public void setLosses(Integer losses) { this.losses = losses; }
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getIdPlayer() { return idPlayer; }
    public void setIdPlayer(String idPlayer) {
        this.idPlayer = idPlayer;
    }

    public String getNamePlayer() {
        return namePlayer;
    }

    public void setNamePlayer(String namePlayer) {
        this.namePlayer = namePlayer;
    }

    public Integer getCoins() {
        return coins;
    }
    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    @Exclude
    public List<Mech> getGarage() {
        return garage;
    }

    public void setGarage(List<Mech> garage) {
        this.garage = garage;
    }

    public void addCoins(int amount) {
        this.coins = (this.coins == null ? 0 : this.coins) + amount;
    }

    public void deductCoins(int amount) {
        this.coins = (this.coins == null ? 0 : this.coins) - amount;
    }
}