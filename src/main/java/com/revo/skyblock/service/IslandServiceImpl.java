package com.revo.skyblock.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revo.skyblock.Plugin;
import com.revo.skyblock.exception.DeleteException;
import com.revo.skyblock.message.MessageManager;
import com.revo.skyblock.model.Island;
import com.revo.skyblock.model.User;
import com.revo.skyblock.repository.IslandRepository;
import com.revo.skyblock.world.WorldManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class IslandServiceImpl implements IslandService{

    private static final Logger log = Plugin.getApplicationContext().getLogger();

    private final WorldManager worldManager;
    private final IslandRepository islandRepository;
    private final MessageManager messageManager;

    @Override
    public String createIsland(final String ownerName) {
        final Player player = Bukkit.getPlayer(ownerName);
        final Island island = Island.builder()
                .owner(User.of(player))
                .members(List.of(User.of(player)))
                .build();
        try {
            worldManager.generateIsland(island);
            islandRepository.save(island);
        } catch (Exception exception) {
            log.info("IslandServiceImpl - createIsland() - error");
            return messageManager.getCreateIslandFailure();
        }
        return messageManager.getCreateIslandSuccess();
    }

    @Override
    public String deleteIsland(final String ownerName) {
        try {
            islandRepository.deleteByOwnerName(ownerName);
        } catch (DeleteException exception) {
            log.info("IslandServiceImpl - deleteIsland() - error");
            return messageManager.getDeleteIslandFailure();
        }
        return messageManager.getDeleteIslandSuccess();
    }

    @Override
    public String addMember(final String ownerName, final String memberName) {
        final Player player = Bukkit.getPlayer(memberName);
        final Optional<Island> islandOptional = islandRepository.findByOwnerName(ownerName);
        if(islandOptional.isEmpty()) {
            return messageManager.getAddMemberIslandNotFound();
        }
        final Island island = islandOptional.get();
        final List<User> members = island.getMembers();
        for(User user : members) {
            if (user.getName().equals(memberName)) {
                return messageManager.getAddMemberIsMember();
            }
        }
        final User user = User.of(player);
        members.add(user);
        user.setIsland(island);
        return messageManager.getAddMemberSuccess();
    }

    @Override
    public String removeMember(final String ownerName, final String memberName) {
        final Player player = Bukkit.getPlayer(memberName);
        final Optional<Island> islandOptional = islandRepository.findByOwnerName(ownerName);
        if(islandOptional.isEmpty()) {
            return messageManager.getRemoveMemberIslandNotFound();
        }
        final Island island = islandOptional.get();
        final List<User> members = island.getMembers();
        for(User user : members) {
            if (user.getName().equals(memberName)) {
                user.setIsland(null);
                members.remove(user);
                return messageManager.getRemoveMemberSuccess();
            }
        }
        return messageManager.getRemoveMemberIsNotMember();
    }
}
