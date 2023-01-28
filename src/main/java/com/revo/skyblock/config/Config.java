package com.revo.skyblock.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revo.skyblock.Plugin;
import com.revo.skyblock.util.Constants;
import com.revo.skyblock.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.File;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Slf4j
public class Config {

    private final Utils utils;

    public void checkConfig() {
        final File file = new File(utils.getPluginPath() + Constants.MAIN_FOLDER + Constants.SLASH + Constants.CONFIG_FILE);
        if (!file.exists()) {
            final Plugin plugin = Plugin.getApplicationContext();
            plugin.saveDefaultConfig();
            log.info("[RSB] Config - checkConfig() - save default config");
        }
    }

    public int getCreateCommandCooldown() {
        final YamlConfiguration configuration = getConfig();
        return configuration.getInt(Constants.CONFIG_COMMAND_CREATE_COOLDOWN);
    }

    public boolean getPvpOnIsland() {
        final YamlConfiguration configuration = getConfig();
        return configuration.getBoolean(Constants.CONFIG_PVP);
    }

    private YamlConfiguration getConfig() {
        final File file = new File(utils.getPluginPath() + Constants.MAIN_FOLDER + Constants.SLASH + Constants.CONFIG_FILE);
        return YamlConfiguration.loadConfiguration(file);
    }
}
