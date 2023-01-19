package com.revo.skyblock.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revo.skyblock.Plugin;
import com.revo.skyblock.exception.SaveException;
import com.revo.skyblock.message.MessageManager;
import com.revo.skyblock.model.Island;
import com.revo.skyblock.model.Region;
import com.revo.skyblock.model.User;
import com.revo.skyblock.repository.IslandRepository;
import com.revo.skyblock.world.WorldManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
        if (hasIsland(ownerName)) {
            return messageManager.getCreateIslandHasIsland();
        }
        final Island island = Island.builder()
                .owner(User.of(player))
                .members(List.of(User.of(player)))
                .build();
        worldManager.generateIsland(island);
        try {
            islandRepository.save(island);
        } catch (Exception exception) {
            log.info("IslandServiceImpl - createIsland() - error");
            return messageManager.databaseExceptionMessage();
        }
        player.teleport(island.getRegion().getCenter());
        return messageManager.getCreateIslandSuccess();
    }

    private boolean hasIsland(String ownerName) {
        return islandRepository.findByOwnerName(ownerName).isPresent();
    }

    @Override
    public String deleteIsland(final String ownerName) {
        try {
            final Island island = islandRepository.findByOwnerName(ownerName).get();
            final Region region = island.getRegion();
            for (Location location : region.getProtectedLocations()) {
                location.getBlock().setType(Material.AIR);
            }
            islandRepository.deleteByOwnerName(ownerName);
        } catch (Exception exception) {
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
        try {
            islandRepository.save(island);
        } catch (SaveException e) {
            log.info("IslandServiceImpl - addMember() - error");
            return messageManager.databaseExceptionMessage();
        }
        return messageManager.getAddMemberSuccess();
    }

    @Override
    public String removeMember(final String ownerName, final String memberName) {
        final Optional<Island> islandOptional = islandRepository.findByOwnerName(ownerName);
        if(islandOptional.isEmpty()) {
            return messageManager.getRemoveMemberIslandNotFound();
        }
        final Island island = islandOptional.get();
        final List<User> members = island.getMembers();
        for(User user : members) {
            if (user.getName().equals(memberName)) {
                members.remove(user);
                try {
                    islandRepository.save(island);
                } catch (SaveException e) {
                    log.info("IslandServiceImpl - removeMember() - error");
                    return messageManager.databaseExceptionMessage();
                }
                return messageManager.getRemoveMemberSuccess();
            }
        }
        return messageManager.getRemoveMemberIsNotMember();
    }

    @Override
    public String setHome(final String ownerName, final Location location) {
        final Optional<Island> islandOptional = islandRepository.findByOwnerName(ownerName);
        if (islandOptional.isEmpty()) {
            return messageManager.getSetHomeIslandNotFound();
        }
        final Island island = islandOptional.get();
        island.setHome(location);
        try {
            islandRepository.save(island);
        } catch (SaveException e) {
            log.info("IslandServiceImpl - setHome() - error");
            return messageManager.databaseExceptionMessage();
        }
        return messageManager.getSetHomeSuccess();
    }

    @Override
    public String teleportToHome(final String memberName) {
        final List<Island> islands = islandRepository.findAll();
        for (Island island : islands) {
            final Player player = Bukkit.getPlayer(memberName);
            if (island.getMembers().contains(User.of(player))) {
                player.teleport(island.getHome());
                return messageManager.getTeleportToHomeSuccess();
            }
        }
        return messageManager.getTeleportToHomeFailure();
    }
}
