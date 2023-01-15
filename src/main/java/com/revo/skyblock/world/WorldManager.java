package com.revo.skyblock.world;

import com.revo.skyblock.exception.GenerateException;
import com.revo.skyblock.model.Island;

public interface WorldManager {
    void generateIsland(final Island island) throws GenerateException;
    void checkWorld();
}
