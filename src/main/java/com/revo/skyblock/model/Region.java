package com.revo.skyblock.model;

import com.revo.skyblock.util.Constants;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Region {

    private Location center;

    public List<Location> getProtectedLocations() {
        final List<Location> locations = new ArrayList<>();
        final int startZ = center.getBlockZ();
        final int startX = center.getBlockX();
        final int size = (int) Constants.MAX_ISLAND_ONE_SIDE_SIZE;
        for (int x = (startX - size); x < (startX + size) ; x++) {
            for (int z = (startZ - size); z < (startZ + size); z++) {
                for (int y = 0; y < 255; y++) {
                    locations.add(new Location(Bukkit.getWorld(Constants.SKYBLOCK_WORLD), x, y, z));
                }
            }
        }
        return locations;
    }
}
