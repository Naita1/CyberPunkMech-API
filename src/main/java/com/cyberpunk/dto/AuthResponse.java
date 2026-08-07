package com.cyberpunk.dto;

public record AuthResponse(
        String token,
        PlayerResponse player
) {}