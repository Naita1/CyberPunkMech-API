package com.cyberpunk.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefensiveMechTest {

    private DefensiveMech mech;

    @BeforeEach
    void setUp() {
        mech = new DefensiveMech("mech-02", "player-01", "Aegis-Prime", 150, 70, 0, 50);
    }

    @Test
    void attack_shouldReturnBaseAttackPower() {
        mech = new DefensiveMech("mech-02", "player-01", "Aegis-Prime", 150, 70, 15, 50);
        assertEquals(15, mech.attack());
    }

    @Test
    void attack_withNullAttackPower_shouldReturnZero() {
        mech = new DefensiveMech("mech-02", "player-01", "Aegis-Prime", 150, 70, null, 50);
        assertEquals(0, mech.attack());
    }

    @Test
    void toggleShield_shouldActivateShield() {
        mech.toggleShield();
        assertTrue(mech.isShieldActive());
    }

    @Test
    void toggleShield_calledTwice_shouldDeactivateShield() {
        mech.toggleShield();
        mech.toggleShield();
        assertFalse(mech.isShieldActive());
    }

    @Test
    void takeDamage_withShieldActive_shouldAbsorbDamageFromShield() {
        mech.toggleShield();
        mech.takeDamage(30);
        assertEquals(20, mech.getShieldArmor());
        assertEquals(150, mech.getCurrentHealth());
    }

    @Test
    void takeDamage_withShieldActive_whenDamageExceedsShield_shouldApplyRestToHealth() {
        mech.toggleShield();
        mech.takeDamage(80);
        assertEquals(0, mech.getShieldArmor());
        assertEquals(120, mech.getCurrentHealth());
    }

    @Test
    void takeDamage_withShieldActive_whenDamageEqualsShield_shouldZeroShieldAndKeepHealth() {
        mech.toggleShield();
        mech.takeDamage(50);
        assertEquals(0, mech.getShieldArmor());
        assertEquals(150, mech.getCurrentHealth());
    }

    @Test
    void takeDamage_withShieldInactive_shouldReduceHealthDirectly() {
        mech.takeDamage(40);
        assertEquals(50, mech.getShieldArmor());
        assertEquals(110, mech.getCurrentHealth());
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
