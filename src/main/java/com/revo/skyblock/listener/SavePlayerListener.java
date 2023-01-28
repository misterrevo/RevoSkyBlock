package com.revo.skyblock.listener;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revo.skyblock.Plugin;
import com.revo.skyblock.exception.SaveException;
import com.revo.skyblock.model.User;
import com.revo.skyblock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.logging.Logger;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Slf4j
public class SavePlayerListener implements Listener {

    private final UserRepository userRepository;

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (notExistsInBase(player)) {
            try {
                userRepository.save(User.of(player));
            } catch (SaveException exception) {
                log.error("[RSB] JoinListener - onPlayerJoin() - error", exception);
            }
        }
    }

    private boolean notExistsInBase(Player player) {
        return !userRepository.existsByName(player.getName());
    }

}
