package com.revo.skyblock.scheduler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revo.skyblock.Plugin;
import com.revo.skyblock.config.Config;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

@Singleton
@Data
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class BlockCreateCommandScheduler {

    private final Config config;

    private List<String> blocked = new ArrayList<>();

    public void runBlockSchedule(final String name){
        blocked.add(name);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getApplicationContext(), new Runnable() {
            @Override
            public void run() {
                blocked.remove(name);
            }
        }, 20L * 60 * config.getCreateCommandCooldown());
    }

}
