package com.revo.skyblock.scheduler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revo.skyblock.Plugin;
import com.revo.skyblock.config.Config;
import com.revo.skyblock.exception.SaveException;
import com.revo.skyblock.model.User;
import com.revo.skyblock.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
@Data
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Slf4j
public class BlockCreateCommandScheduler {

    private final Config config;
    private final UserRepository userRepository;


    public void runBlockSchedule(final String name){
        final Player player = Bukkit.getPlayer(name);
        final UUID uuid = player.getUniqueId();
        final Optional<User> userOptional = userRepository.findByUUID(uuid.toString());
        if (userOptional.isPresent()) {
            final User user = userOptional.get();
            user.setOnCooldown(true);
            try {
                userRepository.save(user);
            } catch (SaveException exception) {
                log.error("BlockCreateCommandScheduler - runBlockSchedule() - error", exception);
                return;
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getApplicationContext(), new Runnable() {
                @Override
                public void run() {
                    user.setOnCooldown(false);
                    try {
                        userRepository.save(user);
                    } catch (SaveException exception) {
                        log.error("Runnable - run() - error", exception);
                    }
                }
            }, 20L * 60 * config.getCreateCommandCooldown());
        }
    }

}
