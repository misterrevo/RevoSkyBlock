package com.revo.skyblock.application;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.revo.skyblock.service.IslandService;
import com.revo.skyblock.service.IslandServiceImpl;

public class ProvideModule extends AbstractModule {

    @Provides
    IslandService provideIslandService() {
        return new IslandServiceImpl();
    }

}
