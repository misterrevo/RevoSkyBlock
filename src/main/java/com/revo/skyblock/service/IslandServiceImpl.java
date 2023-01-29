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
    public void createIsland(final Player owner) {
        final UUID uuid = owner.getUniqueId();
        if (isBlocked(uuid.toString())) {
            owner.sendMessage(messageManager.getCreateIslandSchedule());
            return;
        }
        if (hasIsland(owner.getName())) {
            owner.sendMessage(messageManager.getCreateIslandHasIsland());
            return;
        }
        final Island island = Island.builder()
                .owner(User.of(owner))
                .members(List.of(User.of(owner)))
                .build();
        worldManager.generateIsland(island);
        island.setHome(island.getRegion().getCenter());
        try {
            islandRepository.save(island);
        } catch (Exception exception) {
            log.error("[RSB] IslandServiceImpl - createIsland() - error", exception);
            owner.sendMessage(messageManager.databaseExceptionMessage());
            return;
        }
        owner.teleport(island.getRegion().getCenter());
        blocker.runBlockSchedule(owner.getName());
        owner.sendMessage(messageManager.getCreateIslandSuccess());
    }

    private boolean isBlocked(final String uuid) {
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
    public void deleteIsland(final Player owner) {
        try {
            final Island island = islandRepository.findByOwnerName(owner.getName()).get();
            final Region region = island.getRegion();
            for (Location location : region.getProtectedLocations()) {
                location.getBlock().setType(Material.AIR);
            }
            islandRepository.deleteByOwnerName(owner.getName());
            owner.sendMessage(messageManager.getDeleteIslandSuccess());
        } catch (Exception exception) {
            log.error("[RSB] IslandServiceImpl - deleteIsland() - error", exception);
            owner.sendMessage(messageManager.getDeleteIslandFailure());
        }
    }

    @Override
    public void addMember(final Player owner, final Player member) {
        final Optional<Island> islandOptional = islandRepository.findByOwnerName(owner.getName());
        if(islandOptional.isEmpty()) {
            owner.sendMessage(messageManager.getAddMemberIslandNotFound());
            return;
        }
        final Island island = islandOptional.get();
        final List<User> members = island.getMembers();
        for(User user : members) {
            if (user.getName().equals(member.getName())) {
                owner.sendMessage(messageManager.getAddMemberIsMember());
                return;
            }
        }
        final User user = User.of(member);
        members.add(user);
        try {
            islandRepository.save(island);
        } catch (SaveException exception) {
            log.error("[RSB] IslandServiceImpl - addMember() - error", exception);
            owner.sendMessage(messageManager.databaseExceptionMessage());
            return;
        }
        owner.sendMessage(messageManager.getAddMemberSuccess());
    }

    @Override
    public void removeMember(final Player owner, final Player member) {
        final Optional<Island> islandOptional = islandRepository.findByOwnerName(owner.getName());
        if(islandOptional.isEmpty()) {
            owner.sendMessage(messageManager.getRemoveMemberIslandNotFound());
            return;
        }
        final Island island = islandOptional.get();
        final List<User> members = island.getMembers();
        for(User user : members) {
            if (user.getName().equals(member.getName())) {
                members.remove(user);
                try {
                    islandRepository.save(island);
                } catch (SaveException exception) {
                    log.error("[RSB] IslandServiceImpl - removeMember() - error", exception);
                    owner.sendMessage(messageManager.databaseExceptionMessage());
                    return;
                }
                owner.sendMessage(messageManager.getRemoveMemberSuccess());
                return;
            }
        }
        owner.sendMessage(messageManager.getRemoveMemberIsNotMember());
    }

    @Override
    public void setHome(final Player owner) {
        final Optional<Island> islandOptional = islandRepository.findByOwnerName(owner.getName());
        if (islandOptional.isEmpty()) {
            owner.sendMessage(messageManager.getSetHomeIslandNotFound());
            return;
        }
        final Island island = islandOptional.get();
        final Region region = island.getRegion();
        final Location location = owner.getLocation();
        for (Location target : region.getProtectedLocations()) {
            if (utils.isSameLocation(target, location)) {
                island.setHome(location);
                try {
                    islandRepository.save(island);
                } catch (SaveException exception) {
                    log.error("[RSB] IslandServiceImpl - setHome() - error", exception);
                    owner.sendMessage(messageManager.databaseExceptionMessage());
                    return;
                }
                owner.sendMessage(messageManager.getSetHomeSuccess());
                return;
            }
        }
        owner.sendMessage(messageManager.getSetHomeFailure());
    }

    @Override
    public void teleportToHome(final Player member) {
        final List<Island> islands = islandRepository.findAll();
        for (Island island : islands) {
            if (island.getMembers().contains(User.of(member))) {
                member.teleport(island.getHome());
                member.sendMessage(messageManager.getTeleportToHomeSuccess());
                return;
            }
        }
        member.sendMessage(messageManager.getTeleportToHomeFailure());
    }

    @Override
    public void ownerChange(final Player owner, final Player newOwner) {
        final Optional<Island> islandOptional = islandRepository.findByOwnerName(owner.getName());
        if (islandOptional.isEmpty()) {
            owner.sendMessage(messageManager.getOwnerChangeNoIsland());
            return;
        }
        final Island island = islandOptional.get();
        if (newOwner == null) {
            owner.sendMessage(messageManager.getOwnerChangePlayerNotFound());
            return;
        }
        final User user = User.of(newOwner);
        island.setOwner(user);
        try {
            islandRepository.save(island);
        } catch (SaveException exception) {
            log.error("[RSB] IslandServiceImpl - ownerChange() - error", exception);
            owner.sendMessage(messageManager.databaseExceptionMessage());
            return;
        }
        owner.sendMessage(messageManager.getOwnerChangeSuccess());
    }
}
