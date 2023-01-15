package com.revo.skyblock.world;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revo.skyblock.Plugin;
import com.revo.skyblock.exception.GenerateException;
import com.revo.skyblock.model.Island;
import com.revo.skyblock.model.Region;
import com.revo.skyblock.repository.IslandRepository;
import com.revo.skyblock.util.Constants;
import lombok.RequiredArgsConstructor;
import org.bukkit.*;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class WorldManagerImpl implements WorldManager{

    private static final Logger log = Plugin.getApplicationContext().getLogger();
    
    private final EmptyChunkGenerator emptyChunkGenerator;
    private final IslandRepository islandRepository;

    @Override
    public void generateIsland(Island island) throws GenerateException {
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
        }
    }
}
