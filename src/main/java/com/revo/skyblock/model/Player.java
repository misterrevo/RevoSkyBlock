package com.revo.skyblock.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@Builder
class Player {

    private Long id;
    private UUID uuid;
    private Island island;
}
