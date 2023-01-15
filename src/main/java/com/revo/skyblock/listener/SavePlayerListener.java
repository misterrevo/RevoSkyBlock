package com.revo.skyblock.listener;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revo.skyblock.Plugin;
import com.revo.skyblock.exception.SaveException;
import com.revo.skyblock.model.User;
import com.revo.skyblock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.logging.Logger;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class SavePlayerListener implements Listener {

    private static final Logger log = Plugin.getApplicationContext().getLogger();

    private final UserRepository userRepository;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (notExistsInBase(player)) {
            try {
                userRepository.save(User.of(player));
            } catch (SaveException e) {
                log.info("JoinListener - onPlayerJoin() - error");
            }
        }
    }

    private boolean notExistsInBase(Player player) {
        return !userRepository.existsByName(player.getName());
    }

}
