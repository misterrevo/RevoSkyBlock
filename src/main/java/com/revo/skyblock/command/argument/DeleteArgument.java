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
public class DeleteArgument implements Argument{

    private final IslandService islandService;

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        final Player owner = (Player) commandSender;
        islandService.deleteIsland(owner);
        return true;
    }
}
