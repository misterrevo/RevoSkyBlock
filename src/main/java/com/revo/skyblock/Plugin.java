package com.revo.skyblock;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.revo.skyblock.application.ProvideModule;
import com.revo.skyblock.service.IslandService;
import com.revo.skyblock.service.IslandServiceImpl;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

    private final Injector injector = Guice.createInjector(new ProvideModule());
    private static Plugin applicationContext;

    @Override
    public void onEnable() {
        applicationContext = this;
        getLogger().info("Enabling SkyBlock plugin!");
    }

    public Injector getInjector() {
        return injector;
    }

    public static Plugin getApplicationContext() {
        return applicationContext;
    }
}
