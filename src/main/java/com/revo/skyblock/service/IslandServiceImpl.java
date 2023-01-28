package com.revo.skyblock.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.revo.skyblock.exception.SaveException;
import com.revo.skyblock.message.MessageManager;
import com.revo.skyblock.model.Island;
import com.revo.skyblock.model.Region;
import com.revo.skyblock.model.User;
import com.revo.skyblock.repository.IslandRepository;
import com.revo.skyblock.repository.UserRepository;
import com.revo.skyblock.scheduler.BlockCreateCommandScheduler;
import com.revo.skyblock.util.Utils;
import com.revo.skyblock.world.WorldManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Slf4j
public class IslandServiceImpl implements IslandService{
    
    private final WorldManager worldManager;

    private final IslandRepository islandRepository;

    private final MessageManager messageManager;

    private final Utils utils;
    private final BlockCreateCommandScheduler blocker;
    private final UserRepository userRepository;

    @Override
    public String createIsland(final String ownerName) {
        final Player player = Bukkit.getPlayer(ownerName);
        final UUID uuid = player.getUniqueId();
        if (isBlocked(uuid.toString())) {
            return messageManager.getCreateIslandSchedule();
        }
        if (hasIsland(ownerName)) {
            return messageManager.getCreateIslandHasIsland();
        }
        final Island island = Island.builder()
                .owner(User.of(player))
                .members(List.of(User.of(player)))
                .build();
        worldManager.generateIsland(island);
        island.setHome(island.getRegion().getCenter());
        try {
            islandRepository.save(island);
        } catch (Exception exception) {
            log.error("IslandServiceImpl - createIsland() - error", exception);
            return messageManager.databaseExceptionMessage();
        }
        player.teleport(island.getRegion().getCenter());
        blocker.runBlockSchedule(ownerName);
        return messageManager.getCreateIslandSuccess();
    }

    private boolean isBlocked(String uuid) {
        final Optional<User> userOptional = userRepository.findByUUID(uuid);
        if (userOptional.isPresent()) {
            final User user = userOptional.get();
            return user.isOnCooldown();
        }
        // TODO: Co jesli nie ma uzytkownika? Czy taka sytuacja moze wystapic?
        return true;
    }

    private boolean hasIsland(final String ownerName) {
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
            log.error("IslandServiceImpl - deleteIsland() - error", exception);
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
        } catch (SaveException exception) {
            log.error("IslandServiceImpl - addMember() - error", exception);
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
                } catch (SaveException exception) {
                    log.error("IslandServiceImpl - removeMember() - error", exception);
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
        final Region region = island.getRegion();
        for (Location target : region.getProtectedLocations()) {
            if (utils.isSameLocation(target, location)) {
                island.setHome(location);
                try {
                    islandRepository.save(island);
                } catch (SaveException exception) {
                    log.error("IslandServiceImpl - setHome() - error", exception);
                    return messageManager.databaseExceptionMessage();
                }
                return messageManager.getSetHomeSuccess();
            }
        }
        return messageManager.getSetHomeFailure();
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

    @Override
    public String ownerChange(final String ownerName, final String newOwner) {
        final Optional<Island> islandOptional = islandRepository.findByOwnerName(ownerName);
        if (islandOptional.isEmpty()) {
            return messageManager.getOwnerChangeNoIsland();
        }
        final Island island = islandOptional.get();
        final Player player = Bukkit.getPlayer(newOwner);
        if (player == null) {
            return messageManager.getOwnerChangePlayerNotFound();
        }
        final User user = User.of(player);
        island.setOwner(user);
        try {
            islandRepository.save(island);
        } catch (SaveException exception) {
            log.error("IslandServiceImpl - ownerChange() - error", exception);
            return messageManager.databaseExceptionMessage();
        }
        return messageManager.getOwnerChangeSuccess();
    }
}
