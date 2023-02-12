package com.revo.skyblock.listener;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revo.skyblock.exception.SaveException;
import com.revo.skyblock.model.User;
import com.revo.skyblock.repository.UserRepository;
import com.revo.skyblock.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
                log.error(Constants.TAG + " JoinListener - onPlayerJoin() - error", exception);
            }
        }
    }

    private boolean notExistsInBase(final Player player) {
        return !userRepository.existsByName(player.getName());
    }

}
