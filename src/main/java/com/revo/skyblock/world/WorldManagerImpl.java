package com.revo.skyblock.world;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revo.skyblock.exception.GenerateException;
import com.revo.skyblock.model.Island;
import com.revo.skyblock.util.Constants;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class WorldManagerImpl implements WorldManager{

    private final EmptyChunkGenerator emptyChunkGenerator;

    @Override
    public void generateIsland(Island island) throws GenerateException {

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
