package com.revo.skyblock;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.revo.skyblock.application.ProvideModule;
import com.revo.skyblock.exception.SaveException;
import com.revo.skyblock.model.Island;
import com.revo.skyblock.model.Region;
import com.revo.skyblock.model.User;
import com.revo.skyblock.repository.IslandRepository;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class Plugin extends JavaPlugin {

    private final Injector injector = Guice.createInjector(new ProvideModule() {});
    private static Plugin applicationContext;

    @Override
    public void onEnable() {
        applicationContext = this;
        getLogger().info("Enabling SkyBlock plugin!");
        try {
            injector.getInstance(IslandRepository.class).save(Island.builder()
                            .owner(User.builder()
                                    .uuid(UUID.randomUUID())
                                    .name("name")
                                    .build())
                            .region(Region.builder()
                                    .center(Bukkit.getWorld("world").getSpawnLocation())
                                    .build())
                    .build());
        } catch (SaveException e) {
            throw new RuntimeException(e);
        }
    }

    public Injector getInjector() {
        return injector;
    }

    public static Plugin getApplicationContext() {
        return applicationContext;
    }
}
