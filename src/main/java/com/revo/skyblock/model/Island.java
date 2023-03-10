package com.revo.skyblock.model;

import lombok.Builder;
import lombok.Data;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Island {

    private Long id;
    @Builder.Default
    private Region region = Region.builder().build();
    private User owner;
    @Builder.Default
    private List<User> members = new ArrayList<>();
    @Builder.Default
    private List<User> invited = new ArrayList<>();
    private Location home;
}
