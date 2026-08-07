package com.cyberpunk.service;

import com.cyberpunk.dto.AttackMechRequest;
import com.cyberpunk.dto.AttackMechResponse;
import com.cyberpunk.dto.DefensiveMechRequest;
import com.cyberpunk.dto.DefensiveMechResponse;
import com.cyberpunk.exception.MechNotFoundException;
import com.cyberpunk.model.AttackMech;
import com.cyberpunk.model.DefensiveMech;
import com.cyberpunk.model.Mech;
import com.cyberpunk.repository.MechRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MechServiceTest {

    @Mock
    private MechRepository mechRepository;

    @InjectMocks
    private MechService mechService;

    @Test
    void saveAttackMech_shouldReturnCorrectResponse() throws Exception {
        AttackMechRequest request = new AttackMechRequest("mech-01", "player-01", "Viper-X", 100, 80, 20, 100);

        AttackMechResponse response = mechService.saveAttackMech(request);

        assertEquals("mech-01", response.idMech());
        assertEquals("player-01", response.playerId());
        assertEquals("ATTACK", response.type());
        assertEquals(100, response.currentHealth());
        assertEquals(0, response.heatLevel());
        verify(mechRepository).save(any(AttackMech.class));
    }

    @Test
    void saveDefensiveMech_shouldReturnCorrectResponse() throws Exception {
        DefensiveMechRequest request = new DefensiveMechRequest("mech-02", "player-01", "Aegis", 150, 70, 10, 50);

        DefensiveMechResponse response = mechService.saveDefensiveMech(request);

        assertEquals("mech-02", response.idMech());
        assertEquals("DEFENSIVE", response.type());
        assertEquals(50, response.shieldArmor());
        assertFalse(response.shieldActive());
        verify(mechRepository).save(any(DefensiveMech.class));
    }

    @Test
    void getMechById_whenNotFound_shouldThrowMechNotFoundException() throws Exception {
        when(mechRepository.findById("mech-inexistente")).thenReturn(Optional.empty());

        assertThrows(MechNotFoundException.class, () -> mechService.getMechById("mech-inexistente"));
    }

    @Test
    void getMechById_whenFound_shouldReturnAttackMech() throws Exception {
        AttackMech attackMech = new AttackMech("mech-01", "player-01", "Viper-X", 100, 80, 20, 100);
        when(mechRepository.findById("mech-01")).thenReturn(Optional.of(attackMech));

        Mech result = mechService.getMechById("mech-01");

        assertNotNull(result);
        assertEquals("ATTACK", result.getType());
    }

    @Test
    void getMechsByPlayerId_shouldReturnEmptyList() throws Exception {
        when(mechRepository.findByPlayerId("player-sem-mechs")).thenReturn(List.of());

        List<Mech> result = mechService.getMechsByPlayerId("player-sem-mechs");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void deleteMech_whenNotFound_shouldThrowMechNotFoundException() throws Exception {
        when(mechRepository.existsById("mech-inexistente")).thenReturn(false);

        assertThrows(MechNotFoundException.class, () -> mechService.deleteMech("mech-inexistente"));
        verify(mechRepository, never()).deleteById(anyString());
    }

    @Test
    void deleteMech_whenFound_shouldDelete() throws Exception {
        when(mechRepository.existsById("mech-01")).thenReturn(true);

        mechService.deleteMech("mech-01");

        verify(mechRepository).deleteById("mech-01");
    }
}k,