package com.cyberpunk.controller;

import com.cyberpunk.dto.AuthResponse;
import com.cyberpunk.dto.PlayerLoginRequest;
import com.cyberpunk.dto.PlayerRequest;
import com.cyberpunk.dto.PlayerResponse;
import com.cyberpunk.exception.PlayerNotFoundException;
import com.cyberpunk.security.JwtService;
import com.cyberpunk.service.PlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlayerController.class)
class PlayerControllerTest {

    private static final String VALID_TOKEN = "valid-token";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PlayerService playerService;

    @MockitoBean
    private JwtService jwtService;

    @BeforeEach
    void setUpAuth() {
        when(jwtService.isValid(VALID_TOKEN)).thenReturn(true);
        when(jwtService.extractPlayerId(VALID_TOKEN)).thenReturn("player-01");
    }

    private PlayerResponse samplePlayerResponse() {
        return new PlayerResponse("player-01", "CyberSamurai", 50, 0, 0, 0, List.of());
    }

    @Test
    void createPlayer_withValidRequest_shouldReturn201() throws Exception {
        PlayerRequest request = new PlayerRequest("CyberSamurai", "senha123", null);
        AuthResponse response = new AuthResponse("fake-jwt-token", samplePlayerResponse());

        when(playerService.savePlayer(any())).thenReturn(response);

        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"))
                .andExpect(jsonPath("$.player.namePlayer").value("CyberSamurai"));
    }

    @Test
    void createPlayer_whenNameAlreadyInUse_shouldReturn409() throws Exception {
        PlayerRequest request = new PlayerRequest("CyberSamurai", "senha123", null);

        when(playerService.savePlayer(any()))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Nome já está em uso."));

        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void createPlayer_withInvalidBody_shouldReturn400() throws Exception {
        String invalidJson = """
                {"namePlayer": "", "password": "123"}
                """;

        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_withValidCredentials_shouldReturn200() throws Exception {
        PlayerLoginRequest request = new PlayerLoginRequest("CyberSamurai", "senha123");
        AuthResponse response = new AuthResponse("fake-jwt-token", samplePlayerResponse());

        when(playerService.login(any())).thenReturn(response);

        mockMvc.perform(post("/players/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    void login_withInvalidCredentials_shouldReturn401() throws Exception {
        PlayerLoginRequest request = new PlayerLoginRequest("CyberSamurai", "senhaErrada");

        when(playerService.login(any()))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Piloto ou senha inválidos."));

        mockMvc.perform(post("/players/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getPlayer_whenFound_shouldReturn200() throws Exception {
        when(playerService.getPlayerById("player-01")).thenReturn(samplePlayerResponse());

        mockMvc.perform(get("/players/player-01")
                        .header("Authorization", "Bearer " + VALID_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPlayer").value("player-01"));
    }

    @Test
    void getPlayer_withoutToken_shouldReturn401() throws Exception {
        mockMvc.perform(get("/players/player-01"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getPlayer_whenNotFound_shouldReturn404() throws Exception {
        when(playerService.getPlayerById("player-inexistente"))
                .thenThrow(new PlayerNotFoundException("player-inexistente"));

        mockMvc.perform(get("/players/player-inexistente")
                        .header("Authorization", "Bearer " + VALID_TOKEN))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePlayer_whenOwner_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/players/player-01")
                        .header("Authorization", "Bearer " + VALID_TOKEN))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePlayer_whenNotOwner_shouldReturn403() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "Você só pode remover o próprio piloto."))
                .when(playerService).deletePlayer("player-02", "player-01");

        mockMvc.perform(delete("/players/player-02")
                        .header("Authorization", "Bearer " + VALID_TOKEN))
                .andExpect(status().isForbidden());
    }
}