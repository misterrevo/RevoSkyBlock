package com.revo.skyblock.command.argument;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revo.skyblock.service.IslandService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TeleportToHomeArgument implements Argument{

    private final IslandService islandService;

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        final Player member = (Player) commandSender;
        islandService.teleportToHome(member);
        return true;
    }
}
