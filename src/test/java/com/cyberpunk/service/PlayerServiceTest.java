package com.cyberpunk.service;

import com.cyberpunk.dto.PlayerRequest;
import com.cyberpunk.dto.PlayerResponse;
import com.cyberpunk.exception.PlayerNotFoundException;
import com.cyberpunk.model.Player;
import com.cyberpunk.repository.MechRepository;
import com.cyberpunk.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private MechRepository mechRepository;

    @InjectMocks
    private PlayerService playerService;

    @Test
    void savePlayer_shouldReturnCorrectResponse() throws Exception {
        PlayerRequest request = new PlayerRequest("player-01", "CyberSamurai", 500);

        PlayerResponse response = playerService.savePlayer(request);

        assertEquals("player-01", response.idPlayer());
        assertEquals("CyberSamurai", response.namePlayer());
        assertEquals(500, response.coins());
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    void savePlayer_withNullCoins_shouldUseDefaultCoins() throws Exception {
        PlayerRequest request = new PlayerRequest("player-01", "CyberSamurai", null);

        PlayerResponse response = playerService.savePlayer(request);

        assertEquals(50, response.coins());
    }

    @Test
    void getPlayerById_whenNotFound_shouldThrowPlayerNotFoundException() throws Exception {
        when(playerRepository.findById("player-inexistente")).thenReturn(Optional.empty());

        assertThrows(PlayerNotFoundException.class, () -> playerService.getPlayerById("player-inexistente"));
    }

    @Test
    void getPlayerById_whenFound_shouldReturnPlayerWithGarage() throws Exception {
        Player player = new Player();
        player.setIdPlayer("player-01");
        player.setNamePlayer("CyberSamurai");

        when(playerRepository.findById("player-01")).thenReturn(Optional.of(player));
        when(mechRepository.findByPlayerId("player-01")).thenReturn(List.of());

        PlayerResponse result = playerService.getPlayerById("player-01");

        assertNotNull(result);
        assertEquals("player-01", result.idPlayer());
        verify(mechRepository).findByPlayerId("player-01");
    }

    @Test
    void deletePlayer_whenNotFound_shouldThrowPlayerNotFoundException() throws Exception {
        when(playerRepository.existsById("player-inexistente")).thenReturn(false);

        assertThrows(PlayerNotFoundException.class, () -> playerService.deletePlayer("player-inexistente"));
    }

    @Test
    void deletePlayer_shouldDeleteMechsThenPlayer() throws Exception {
        when(playerRepository.existsById("player-01")).thenReturn(true);
        when(mechRepository.findByPlayerId("player-01")).thenReturn(List.of());

        playerService.deletePlayer("player-01");

        verify(mechRepository).findByPlayerId("player-01");
        verify(mechRepository).deleteAllById(List.of());
        verify(playerRepository).deleteById("player-01");
    }
}