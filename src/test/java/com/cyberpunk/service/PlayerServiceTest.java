package com.cyberpunk.service;

import com.cyberpunk.dto.AuthResponse;
import com.cyberpunk.dto.PlayerRequest;
import com.cyberpunk.dto.PlayerResponse;
import com.cyberpunk.exception.PlayerNotFoundException;
import com.cyberpunk.model.Player;
import com.cyberpunk.repository.MechRepository;
import com.cyberpunk.repository.PlayerRepository;
import com.cyberpunk.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private MechRepository mechRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private PlayerService playerService;

    @Test
    void savePlayer_shouldReturnCorrectResponse() throws Exception {
        PlayerRequest request = new PlayerRequest("CyberSamurai", "senha123", 500);
        when(playerRepository.findByName("CyberSamurai")).thenReturn(Optional.empty());
        when(jwtService.generateToken(anyString())).thenReturn("fake-jwt-token");

        AuthResponse response = playerService.savePlayer(request);

        assertEquals("CyberSamurai", response.player().namePlayer());
        assertEquals(500, response.player().coins());
        assertEquals("fake-jwt-token", response.token());
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    void savePlayer_withNullCoins_shouldUseDefaultCoins() throws Exception {
        PlayerRequest request = new PlayerRequest("CyberSamurai", "senha123", null);
        when(playerRepository.findByName("CyberSamurai")).thenReturn(Optional.empty());
        when(jwtService.generateToken(anyString())).thenReturn("fake-jwt-token");

        AuthResponse response = playerService.savePlayer(request);

        assertEquals(50, response.player().coins());
    }

    @Test
    void savePlayer_whenNameAlreadyExists_shouldThrowConflict() throws Exception {
        PlayerRequest request = new PlayerRequest("CyberSamurai", "senha123", null);
        when(playerRepository.findByName("CyberSamurai")).thenReturn(Optional.of(new Player()));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> playerService.savePlayer(request));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(playerRepository, never()).save(any(Player.class));
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

        assertThrows(PlayerNotFoundException.class,
                () -> playerService.deletePlayer("player-inexistente", "player-inexistente"));
    }

    @Test
    void deletePlayer_whenNotOwner_shouldThrowForbidden() throws Exception {
        when(playerRepository.existsById("player-01")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> playerService.deletePlayer("player-01", "player-02"));

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
        verify(playerRepository, never()).deleteById(anyString());
    }

    @Test
    void deletePlayer_shouldDeleteMechsThenPlayer() throws Exception {
        when(playerRepository.existsById("player-01")).thenReturn(true);
        when(mechRepository.findByPlayerId("player-01")).thenReturn(List.of());

        playerService.deletePlayer("player-01", "player-01");

        verify(mechRepository).findByPlayerId("player-01");
        verify(mechRepository).deleteAllById(List.of());
        verify(playerRepository).deleteById("player-01");
    }
}