package com.cyberpunk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record PlayerRequest(
        @NotBlank(message = "O nome do jogador é obrigatório")
        String namePlayer,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String password,

        @PositiveOrZero(message = "As moedas não podem ser negativas")
        Integer coins
) {}