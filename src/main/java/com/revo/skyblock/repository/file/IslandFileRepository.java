package com.revo.skyblock.repository.file;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revo.skyblock.Plugin;
import com.revo.skyblock.exception.DeleteException;
import com.revo.skyblock.exception.SaveException;
import com.revo.skyblock.model.Island;
import com.revo.skyblock.model.Region;
import com.revo.skyblock.model.User;
import com.revo.skyblock.repository.IslandRepository;
import com.revo.skyblock.repository.UserRepository;
import com.revo.skyblock.util.Constants;
import com.revo.skyblock.util.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class IslandFileRepository implements IslandRepository {

    private static final Logger log = Plugin.getApplicationContext().getLogger();

    private final FileManager fileManager;
    private final UserRepository userRepository;
    private final Utils utils;

    @Override
    public Island save(final Island island) throws SaveException {
        if(island.getId() == null) {
            island.setId(utils.getLastId(Constants.ISLANDS_FOLDER));
        }
        final File file = new File(utils.getPluginPath() + Constants.MAIN_FOLDER + Constants.SLASH + Constants.ISLANDS_FOLDER + Constants.SLASH + island.getId() + Constants.YAML_SUFFIX);
        fileManager.checkFile(file);
        final YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);

        yamlConfiguration.set("id", island.getId());
        yamlConfiguration.set("center", locationToYaml(island.getRegion().getCenter()));
        yamlConfiguration.set("owner", island.getOwner().getUuid().toString());
        yamlConfiguration.set("members", island.getMembers().stream().map(member -> member.getUuid().toString()));
        yamlConfiguration.set("home", locationToYaml(island.getHome()));
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            log.info("IslandFileRepository - save() - error");
            throw new SaveException(island);
        }
        return island;
    }

    private String locationToYaml(final Location center) {
        return center.getX() + Constants.LOCATION_SEPARATOR + center.getY() + Constants.LOCATION_SEPARATOR + center.getZ();
    }

    @Override
    public void deleteByOwnerName(final String ownerName) throws DeleteException {
        final File file = new File(utils.getPluginPath() + Constants.MAIN_FOLDER + Constants.SLASH + Constants.ISLANDS_FOLDER);
        for (File target : file.listFiles()) {
            final YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(target);
            if(yamlConfiguration.get("owner").equals(Bukkit.getPlayer(ownerName).getUniqueId())) {
                target.delete();
                return;
            }
        }
        log.info("IslandFileRepository - deleteByOwnerName() - error key = %s".formatted(ownerName));
        throw new DeleteException(ownerName);
    }

    @Override
    public Optional<Island> findByOwnerName(final String ownerName) {
        final File file = new File(utils.getPluginPath() + Constants.MAIN_FOLDER + Constants.SLASH + Constants.ISLANDS_FOLDER);
        for (File target : file.listFiles()) {
            final YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(target);
            if(yamlConfiguration.get("owner").equals(Bukkit.getPlayer(ownerName).getUniqueId())) {
                final Island island = buildIsland(yamlConfiguration);
                return Optional.of(island);
            }
        }
        return Optional.empty();
    }

    private Island buildIsland(YamlConfiguration yamlConfiguration) {
        return Island.builder()
                .owner(userRepository.findByUUID(yamlConfiguration.getString("owner")).get())
                .members(getUsersFromYaml(yamlConfiguration.getStringList("members")))
                .region(Region.builder()
                        .center(getLocationFromYaml(yamlConfiguration.getString("center")))
                        .build())
                .id(yamlConfiguration.getLong("id"))
                .home(getLocationFromYaml(yamlConfiguration.getString("home")))
                .build();
    }

    @Override
    public List<Island> findAll() {
        final File file = new File(utils.getPluginPath() + Constants.MAIN_FOLDER + Constants.SLASH + Constants.ISLANDS_FOLDER);
        final List<Island> islands = new ArrayList<>();
        for (File target : file.listFiles()) {
            final YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(target);
            final Island island = buildIsland(yamlConfiguration);
            islands.add(island);
        }
        return islands;
    }

    private Location getLocationFromYaml(final String center) {
        final String[] points = center.split(Constants.LOCATION_SEPARATOR);
        return new Location(Bukkit.getWorld(Constants.SKYBLOCK_WORLD), Double.valueOf(points[0]), Double.valueOf(points[1]), Double.valueOf(points[2]));
    }

    private List<User> getUsersFromYaml(final List<String> uuids) {
        return uuids.stream()
                .map(uuid -> userRepository.findByUUID(uuid).get())
                .collect(Collectors.toList());
    }
}
