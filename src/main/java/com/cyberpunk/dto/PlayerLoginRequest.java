package com.cyberpunk.dto;

import jakarta.validation.constraints.NotBlank;

public record PlayerLoginRequest(
        @NotBlank(message = "O nome do jogador é obrigatório")
        String namePlayer,

        @NotBlank(message = "A senha é obrigatória")
        String password
) {}