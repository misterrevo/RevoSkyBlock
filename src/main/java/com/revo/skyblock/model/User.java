package com.revo.skyblock.model;

import lombok.Builder;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.UUID;

@Data
@Builder
public class User {

    private Long id;
    private UUID uuid;
    private String name;

    public static User of(Player player) {
        return User.builder()
                .uuid(player.getUniqueId())
                .name(player.getDisplayName())
                .build();
    }
}
