package com.revo.skyblock.repository.file;

import com.revo.skyblock.Plugin;
import com.revo.skyblock.exception.DeleteException;
import com.revo.skyblock.exception.SaveException;
import com.revo.skyblock.model.Island;
import com.revo.skyblock.repository.IslandRepository;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;

public class IslandFileRepository implements IslandRepository {

    private static final Logger log = Plugin.getApplicationContext().getLogger();

    @Override
    public Island save(Island island) throws SaveException {
        if(island.getId() == null) {
            island.setId(getLastId());
        }
        final File folder = new File(getPluginPath() + "RSB");
        if (!folder.exists()) {
            folder.mkdir();
        }
        final File file = new File(getPluginPath() + "RSB/"+island.getId()+".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                log.info("save() - error");
                e.printStackTrace();
                return null;
            }
        }
        final YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        yamlConfiguration.set("id", island.getId());
        yamlConfiguration.set("center", island.getRegion().getCenter());
        yamlConfiguration.set("owner", island.getOwner().getUuid());
        yamlConfiguration.set("members", island.getMembers().stream().map(member -> member.getUuid()));
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            log.info("save() - error");
            e.printStackTrace();
            return null;
        }
        return island;
    }

    private Long getLastId() {
        return 1L;
    }

    private String getPluginPath() {
        try {
            final String path = this.getClass().getProtectionDomain().getCodeSource().getLocation()
                    .toURI().getPath();
            final StringBuilder stringBuilder = new StringBuilder();
            final String[] pathSplit = path.split("/");
            for (int x = 0; (x + 1) < pathSplit.length; x++) {
                stringBuilder.append(pathSplit[x] + "/");
            }
            return stringBuilder.toString().replaceFirst("/", "");
        } catch (Exception exception) {
            log.info("getPluginPath() - error");
            return null;
        }
    }

    @Override
    public void deleteByOwnerName(String ownerName) throws DeleteException {

    }

    @Override
    public Optional<Island> findByOwnerName(String ownerName) {
        return Optional.empty();
    }
}
