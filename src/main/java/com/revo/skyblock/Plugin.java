package com.revo.skyblock;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.revo.skyblock.application.ProvideModule;
import com.revo.skyblock.listener.SavePlayerListener;
import com.revo.skyblock.repository.file.FileManager;
import com.revo.skyblock.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

    private final Injector injector = Guice.createInjector(new ProvideModule() {});
    private static Plugin applicationContext;

    @Override
    public void onEnable() {
        applicationContext = this;
        getLogger().info("Enabling SkyBlock plugin!");
        injector.getInstance(FileManager.class).checkFiles();
        injector.getInstance(WorldManager.class).checkWorld();
        Bukkit.getServer().getPluginManager().registerEvents(injector.getInstance(SavePlayerListener.class), this);
    }

    public Injector getInjector() {
        return injector;
    }

    public static Plugin getApplicationContext() {
        return applicationContext;
    }
}
