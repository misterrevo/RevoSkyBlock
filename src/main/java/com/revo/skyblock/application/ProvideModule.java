package com.revo.skyblock.application;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.revo.skyblock.message.MessageManager;
import com.revo.skyblock.repository.IslandRepository;
import com.revo.skyblock.repository.UserRepository;
import com.revo.skyblock.repository.file.FileManager;
import com.revo.skyblock.repository.file.IslandFileRepository;
import com.revo.skyblock.service.IslandService;
import com.revo.skyblock.service.IslandServiceImpl;
import com.revo.skyblock.world.WorldManager;

public class ProvideModule extends AbstractModule {

    @Provides
    IslandRepository islandRepository(final FileManager fileManager, final UserRepository userRepository) {
        return new IslandFileRepository(fileManager, userRepository);
    }

    @Provides
    IslandService islandService(final WorldManager worldManager, final IslandRepository islandRepository, final MessageManager messageManager) {
        return new IslandServiceImpl(worldManager, islandRepository, messageManager);
    }
}
