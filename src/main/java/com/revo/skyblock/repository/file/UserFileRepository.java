package com.revo.skyblock.repository.file;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revo.skyblock.Plugin;
import com.revo.skyblock.exception.SaveException;
import com.revo.skyblock.model.User;
import com.revo.skyblock.repository.UserRepository;
import com.revo.skyblock.util.Constants;
import com.revo.skyblock.util.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserFileRepository implements UserRepository {
    
    private static final Logger log = Plugin.getApplicationContext().getLogger();

    private final FileManager fileManager;
    private final Utils utils;
    
    @Override
    public User save(final User user) throws SaveException{
        if(user.getId() == null) {
            user.setId(utils.getLastId(Constants.USERS_FOLDER));
        }
        final File file = new File(utils.getPluginPath() + Constants.MAIN_FOLDER + Constants.SLASH + Constants.USERS_FOLDER + Constants.SLASH + user.getId() + Constants.YAML_SUFFIX);
        fileManager.checkFile(file);
        final YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        yamlConfiguration.set("id", user.getId());
        yamlConfiguration.set("uuid", user.getUuid().toString());
        yamlConfiguration.set("name", user.getName());
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            log.info("UserFileRepository - save() - error");
            throw new SaveException(user);
        }
        return user;
    }

    @Override
    public Optional<User> findByUUID(final String uuid) {
        final File file = new File(utils.getPluginPath() + Constants.MAIN_FOLDER + Constants.SLASH + Constants.USERS_FOLDER);
        for (File target : file.listFiles()) {
            final YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(target);
            if(yamlConfiguration.get("uuid").equals(uuid)) {
                final User user = User.builder()
                        .id(yamlConfiguration.getLong("id"))
                        .uuid(UUID.fromString(yamlConfiguration.getString("uuid")))
                        .name(yamlConfiguration.getString("name"))
                        .build();
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByName(String name) {
        final File file = new File(utils.getPluginPath() + Constants.MAIN_FOLDER + Constants.SLASH + Constants.USERS_FOLDER);
        for (File target : file.listFiles()) {
            final YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(target);
            if(yamlConfiguration.get("name").equals(name)) {
                return true;
            }
        }
        return false;
    }
}
