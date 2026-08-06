package com.cyberpunk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record PlayerRequest(
        @NotBlank(message = "O ID do jogador é obrigatório")
        String idPlayer,

        @NotBlank(message = "O nome do jogador é obrigatório")
        String namePlayer,

        @PositiveOrZero(message = "As moedas não podem ser negativas")
        Integer coins
) {}
