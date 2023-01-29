package com.revo.skyblock.command.argument;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revo.skyblock.message.MessageManager;
import com.revo.skyblock.model.Island;
import com.revo.skyblock.model.User;
import com.revo.skyblock.repository.IslandRepository;
import com.revo.skyblock.service.IslandService;
import com.revo.skyblock.util.Constants;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class InfoArgument implements Argument{

    private final IslandService islandService;

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        final Player sender = (Player) commandSender;
        islandService.info(sender, args[1]);
        return true;
    }
}
