package com.revo.skyblock.world;

import com.google.inject.Singleton;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

@Singleton
public class EmptyChunkGenerator extends ChunkGenerator {
    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        return super.createChunkData(world);
    }
}
