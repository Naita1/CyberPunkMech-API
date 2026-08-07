package com.cyberpunk.dto;

import com.cyberpunk.model.AttackMech;
import com.cyberpunk.model.DefensiveMech;
import com.cyberpunk.model.Mech;

public sealed interface MechSummaryResponse permits AttackMechSummaryResponse, DefensiveMechSummaryResponse {

    static MechSummaryResponse from(Mech mech) {
        if (mech instanceof AttackMech attackMech) {
            return AttackMechSummaryResponse.from(attackMech);
        }
        if (mech instanceof DefensiveMech defensiveMech) {
            return DefensiveMechSummaryResponse.from(defensiveMech);
        }
        throw new IllegalStateException("Unknown mech type: " + mech.getClass().getSimpleName());
    }
}