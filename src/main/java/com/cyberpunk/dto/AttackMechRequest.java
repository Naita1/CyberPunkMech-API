package com.cyberpunk.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AttackMechRequest(
        @NotBlank(message = "O ID do jogador é obrigatório")
        String playerId,

        @NotBlank(message = "O modelo é obrigatório")
        String model,

        @NotNull(message = "A vida máxima é obrigatória")
        @Min(value = 1, message = "A vida máxima deve ser maior que zero")
        Integer maxHealth,

        @NotNull(message = "A bateria é obrigatória")
        @Min(value = 0, message = "A bateria não pode ser negativa")
        Integer battery,

        @NotNull(message = "O poder de ataque é obrigatório")
        @Min(value = 1, message = "O poder de ataque deve ser maior que zero")
        Integer attackPower,

        @NotNull(message = "O calor máximo é obrigatório")
        @Min(value = 1, message = "O calor máximo deve ser maior que zero")
        Integer maxHeat
) {}
