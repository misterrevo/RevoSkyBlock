package com.revo.skyblock.listener;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revo.skyblock.model.Island;
import com.revo.skyblock.model.Region;
import com.revo.skyblock.repository.IslandRepository;
import com.revo.skyblock.util.Constants;
import com.revo.skyblock.util.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Optional;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class BuildListener implements Listener {

    private final IslandRepository islandRepository;

    private final Utils utils;

    @EventHandler
    public void onBreakBlock(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        if (isSkyblockWorld(player)) {
            final Optional<Island> islandOptional = islandRepository.findByOwnerName(player.getName());
            if (islandOptional.isEmpty()) {
                event.setCancelled(true);
                return;
            }
            final Island island = islandOptional.get();
            if (isOutOfIsland(island, player.getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    private boolean isOutOfIsland(final Island island, final Location playerLocation) {
        final Region region = island.getRegion();
        for (Location location : region.getProtectedLocations()) {
            if (utils.isSameLocation(location, playerLocation)) {
                return false;
            }
        }
        return true;
    }

    private boolean isSkyblockWorld(final Player player) {
        return player.getWorld().getName().equals(Constants.SKYBLOCK_WORLD);
    }
}
