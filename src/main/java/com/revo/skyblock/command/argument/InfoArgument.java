package com.revo.skyblock.command.argument;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revo.skyblock.message.MessageManager;
import com.revo.skyblock.model.Island;
import com.revo.skyblock.model.User;
import com.revo.skyblock.repository.IslandRepository;
import com.revo.skyblock.util.Constants;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class InfoArgument implements Argument{

    private final IslandRepository islandRepository;
    private final MessageManager messageManager;

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        final Optional<Island> islandOptional = islandRepository.findByOwnerName(args[1]);
        if (islandOptional.isEmpty()) {
            commandSender.sendMessage(messageManager.getInfoArgumentIslandNotFound());
            return true;
        }
        final Island island = islandOptional.get();
        final User owner = island.getOwner();
        commandSender.sendMessage(messageManager.getInfoArgumentOwnerHeader().formatted(owner.getName()));
        commandSender.sendMessage(messageManager.getInfoArgumentMembersHeader());
        island.getMembers().forEach(member -> commandSender.sendMessage(Constants.MEMBER_PREFIX + member.getName()));
        return true;
    }
}
