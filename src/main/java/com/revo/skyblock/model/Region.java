package com.revo.skyblock.model;

import lombok.Builder;
import lombok.Data;
import org.bukkit.Location;

import java.util.List;

@Data
@Builder
public class Region {

    private Location center;

    public List<Location> getProtectedLocations(){
        return null;
    }
}
