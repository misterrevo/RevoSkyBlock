package com.revo.skyblock.application;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.revo.skyblock.repository.IslandRepository;
import com.revo.skyblock.repository.file.IslandFileRepository;

public class ProvideModule extends AbstractModule {

    @Provides
    IslandRepository islandRepository(){
        return new IslandFileRepository();
    }
}
