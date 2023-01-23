package com.revo.skyblock;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.revo.skyblock.application.ProvideModule;
import com.revo.skyblock.command.IslandExecutor;
import com.revo.skyblock.config.Config;
import com.revo.skyblock.listener.BuildListener;
import com.revo.skyblock.listener.PvpListener;
import com.revo.skyblock.listener.SavePlayerListener;
import com.revo.skyblock.repository.file.FileManager;
import com.revo.skyblock.util.Constants;
import com.revo.skyblock.world.WorldManager;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Slf4j
public class Plugin extends JavaPlugin {

    private final Injector injector = Guice.createInjector(new ProvideModule() {});
    private static Plugin applicationContext;

    @Override
    public void onEnable() {
        log.info("Plugin - onEnable() - init application context");

        applicationContext = this;

        injector.getInstance(FileManager.class).checkFiles();
        injector.getInstance(WorldManager.class).checkWorld();
        injector.getInstance(Config.class).checkConfig();

        log.info("Plugin - onEnable() - register events");
        Bukkit.getServer().getPluginManager().registerEvents(injector.getInstance(SavePlayerListener.class), this);
        Bukkit.getServer().getPluginManager().registerEvents(injector.getInstance(BuildListener.class), this);
        Bukkit.getServer().getPluginManager().registerEvents(injector.getInstance(PvpListener.class), this);

        log.info("Plugin - onEnable() - register commands");
        getCommand(Constants.COMMAND).setExecutor(injector.getInstance(IslandExecutor.class));
    }

    public Injector getInjector() {
        return injector;
    }

    public static Plugin getApplicationContext() {
        return applicationContext;
    }
}
