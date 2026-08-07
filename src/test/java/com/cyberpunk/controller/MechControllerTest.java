package com.cyberpunk.controller;

import com.cyberpunk.dto.AttackMechRequest;
import com.cyberpunk.dto.AttackMechResponse;
import com.cyberpunk.dto.DefensiveMechRequest;
import com.cyberpunk.dto.DefensiveMechResponse;
import com.cyberpunk.exception.MechNotFoundException;
import com.cyberpunk.model.AttackMech;
import com.cyberpunk.security.JwtService;
import com.cyberpunk.service.MechService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MechController.class)
class MechControllerTest {

    private static final String VALID_TOKEN = "valid-token";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MechService mechService;

    @MockitoBean
    private JwtService jwtService;

    @BeforeEach
    void setUpAuth() {
        when(jwtService.isValid(VALID_TOKEN)).thenReturn(true);
        when(jwtService.extractPlayerId(VALID_TOKEN)).thenReturn("player-01");
    }

    @Test
    void createAttackMech_withValidRequest_shouldReturn201() throws Exception {
        AttackMechRequest request = new AttackMechRequest("Viper-X", 100, 80, 20, 100);
        AttackMechResponse response = new AttackMechResponse(
                "mech-01", "player-01", "Viper-X", "ATTACK", 100, 100, 80, 20, 0, 100
        );

        when(mechService.saveAttackMech(any(), eq())).thenReturn(response);

        mockMvc.perform(post("/mechs/attack")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idMech").value("mech-01"))
                .andExpect(jsonPath("$.type").value("ATTACK"));
    }

    @Test
    void createAttackMech_withoutToken_shouldReturn401() throws Exception {
        AttackMechRequest request = new AttackMechRequest("Viper-X", 100, 80, 20, 100);

        mockMvc.perform(post("/mechs/attack")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createAttackMech_withInvalidBody_shouldReturn400() throws Exception {
        String invalidJson = """
                {"model": "", "maxHealth": null, "battery": 80, "attackPower": 20, "maxHeat": 100}
                """;

        mockMvc.perform(post("/mechs/attack")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createDefensiveMech_withValidRequest_shouldReturn201() throws Exception {
        DefensiveMechRequest request = new DefensiveMechRequest("Aegis", 150, 70, 10, 50);
        DefensiveMechResponse response = new DefensiveMechResponse(
                "mech-02", "player-01", "Aegis", "DEFENSIVE", 150, 150, 70, 10, 50, false
        );

        when(mechService.saveDefensiveMech(any(), eq())).thenReturn(response);

        mockMvc.perform(post("/mechs/defensive")
                        .header("Authorization", "Bearer " + VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("DEFENSIVE"));
    }

    @Test
    void getMech_whenFound_shouldReturn200() throws Exception {
        AttackMech mech = new AttackMech("mech-01", "player-01", "Viper-X", 100, 80, 20, 100);
        when(mechService.getMechById("mech-01")).thenReturn(mech);

        mockMvc.perform(get("/mechs/mech-01")
                        .header("Authorization", "Bearer " + VALID_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idMech").value("mech-01"));
    }

    @Test
    void getMech_whenNotFound_shouldReturn404() throws Exception {
        when(mechService.getMechById("mech-inexistente"))
                .thenThrow(new MechNotFoundException("mech-inexistente"));

        mockMvc.perform(get("/mechs/mech-inexistente")
                        .header("Authorization", "Bearer " + VALID_TOKEN))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMechByPlayer_shouldReturn200WithList() throws Exception {
        AttackMech mech = new AttackMech("mech-01", "player-01", "Viper-X", 100, 80, 20, 100);
        when(mechService.getMechsByPlayerId("player-01")).thenReturn(List.of(mech));

        mockMvc.perform(get("/mechs").param("playerId", "player-01")
                        .header("Authorization", "Bearer " + VALID_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idMech").value("mech-01"));
    }

    @Test
    void deleteMech_whenOwner_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/mechs/mech-01")
                        .header("Authorization", "Bearer " + VALID_TOKEN))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteMech_whenNotFound_shouldReturn404() throws Exception {
        org.mockito.Mockito.doThrow(new MechNotFoundException("mech-inexistente"))
                .when(mechService).deleteMech("mech-inexistente", "player-01");

        mockMvc.perform(delete("/mechs/mech-inexistente")
                        .header("Authorization", "Bearer " + VALID_TOKEN))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMech_whenNotOwner_shouldReturn403() throws Exception {
        org.mockito.Mockito.doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não pode remover um mech de outro piloto."))
                .when(mechService).deleteMech("mech-01", "player-01");

        mockMvc.perform(delete("/mechs/mech-01")
                        .header("Authorization", "Bearer " + VALID_TOKEN))
                .andExpect(status().isForbidden());
    }

    private static <T> T any() { return org.mockito.ArgumentMatchers.any(); }
    private static String eq() { return org.mockito.ArgumentMatchers.eq("player-01"); }
}