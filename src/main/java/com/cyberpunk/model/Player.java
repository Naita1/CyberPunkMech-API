package com.cyberpunk.model;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.Exclude;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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
        this.coins = 50;
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
        this.createdAt = Timestamp.now();
        this.garage = new ArrayList<>();
    }

    @Exclude
    public Integer getMechsCount() {
        return this.garage != null ? this.garage.size() : 0;
    }

    @Exclude
    public List<Mech> getGarage() {
        return garage;
    }

    public void addCoins(int amount) {
        this.coins = (this.coins == null ? 0 : this.coins) + amount;
    }

    public void deductCoins(int amount) {
        this.coins = (this.coins == null ? 0 : this.coins) - amount;
    }
}
