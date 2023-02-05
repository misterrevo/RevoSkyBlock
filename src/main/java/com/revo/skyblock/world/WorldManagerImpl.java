package com.revo.skyblock.world;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revo.skyblock.model.Island;
import com.revo.skyblock.model.Region;
import com.revo.skyblock.repository.IslandRepository;
import com.revo.skyblock.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import java.util.List;
import java.util.Random;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Slf4j
public class WorldManagerImpl implements WorldManager{

    private final EmptyChunkGenerator emptyChunkGenerator;

    private final IslandRepository islandRepository;

    @Override
    public void generateIsland(Island island) {
        final List<Island> islands = islandRepository.findAll();
        final Location location = getCenterLocation(islands);
        final Region region = island.getRegion();
        region.setCenter(location);
        location.getBlock().setType(Material.DIRT);
    }

    private Location getCenterLocation(List<Island> islands) {
        final Random random = new Random();
        double lastX = 0;
        for (Island island : islands) {
            final Region region = island.getRegion();
            final Location center = region.getCenter();
            if (center.getX() > lastX) {
                lastX = center.getX();
            }
        }
        return new Location(Bukkit.getWorld(Constants.SKYBLOCK_WORLD), lastX + (Constants.MAX_ISLAND_ONE_SIDE_SIZE * 2), 100, 0);
    }

    @Override
    public void checkWorld() {
        World world = Bukkit.getWorld(Constants.SKYBLOCK_WORLD);
        if (world == null) {
            final WorldCreator worldCreator = new WorldCreator(Constants.SKYBLOCK_WORLD);
            worldCreator.generateStructures(false);
            worldCreator.type(WorldType.FLAT);
            worldCreator.generator(emptyChunkGenerator);
            Bukkit.createWorld(worldCreator);
            log.info(Constants.TAG + " WorldManagerImpl - checkWorld() - created skyblock world");
        }
    }
}
