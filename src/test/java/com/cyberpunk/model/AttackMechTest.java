package com.cyberpunk.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttackMechTest {

    private AttackMech mech;

    @BeforeEach
    void setUp() {
        mech = new AttackMech("mech-01", "player-01", "Viper-X", 100, 80, 20, 100);
    }

    @Test
    void attack_shouldReturnDoubleDamageAndIncreaseHeat() {
        int damage = mech.attack();
        assertEquals(40, damage);
        assertEquals(25, mech.getHeatLevel());
    }

    @Test
    void attack_whenOverheated_shouldReturnZero() {
        mech.setHeatLevel(100);
        assertEquals(0, mech.attack());
    }

    @Test
    void attack_whenHeatReachesMax_shouldBlockNextAttack() {
        mech.attack();
        mech.attack();
        mech.attack();
        mech.attack();
        assertEquals(0, mech.attack());
    }

    @Test
    void coolDown_shouldReduceHeatByThirty() {
        mech.setHeatLevel(50);
        mech.coolDown();
        assertEquals(20, mech.getHeatLevel());
    }

    @Test
    void coolDown_shouldNotGoBelowZero() {
        mech.setHeatLevel(10);
        mech.coolDown();
        assertEquals(0, mech.getHeatLevel());
    }

    @Test
    void takeDamage_shouldReduceCurrentHealth() {
        mech.takeDamage(30);
        assertEquals(70, mech.getCurrentHealth());
    }

    @Test
    void takeDamage_shouldNotGoBelowZero() {
        mech.takeDamage(999);
        assertEquals(0, mech.getCurrentHealth());
    }

    @Test
    void isOperational_whenHealthAboveZero_shouldReturnTrue() {
        assertTrue(mech.isOperational());
    }

    @Test
    void isOperational_whenHealthIsZero_shouldReturnFalse() {
        mech.takeDamage(999);
        assertFalse(mech.isOperational());
    }
}
