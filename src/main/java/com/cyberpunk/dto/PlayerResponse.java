package com.cyberpunk.dto;

import com.cyberpunk.model.Player;

import java.util.List;

public record PlayerResponse(
        String idPlayer,
        String namePlayer,
        Integer coins,
        Integer wins,
        Integer draws,
        Integer losses,
        List<MechSummaryResponse> garage
) {
    public static PlayerResponse from(Player player) {
        return new PlayerResponse(
                player.getIdPlayer(),
                player.getNamePlayer(),
                player.getCoins(),
                player.getWins(),
                player.getDraws(),
                player.getLosses(),
                player.getGarage().stream()
                        .map(MechSummaryResponse::from)
                        .toList()
        );
    }
}