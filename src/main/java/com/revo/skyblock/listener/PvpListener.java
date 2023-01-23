package com.revo.skyblock.listener;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revo.skyblock.config.Config;
import com.revo.skyblock.util.Constants;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PvpListener implements Listener {

    private final Config config;

    @EventHandler
    public void onPvp(final EntityDamageByEntityEvent event) {
        final Entity entity = event.getEntity();
        final Entity damager = event.getDamager();
        if (arePlayers(entity, damager)) {
            if (isSkyblock(entity)) {
                if (pvpIsOff()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    private boolean pvpIsOff() {
        return !config.getPvpOnIsland();
    }

    private boolean isSkyblock(Entity entity) {
        return entity.getWorld().getName().equals(Constants.SKYBLOCK_WORLD);
    }

    private boolean arePlayers(Entity entity, Entity damager) {
        return entity instanceof Player && damager instanceof Player;
    }
}
