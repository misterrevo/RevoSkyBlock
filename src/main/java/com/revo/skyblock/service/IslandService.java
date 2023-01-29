package com.revo.skyblock.service;

import org.bukkit.entity.Player;

public interface IslandService {
    void createIsland(final Player owner);
    void deleteIsland(final Player owner);
    void addMember(final Player owner, final Player member);
    void removeMember(final Player owner, final Player member);
    void setHome(final Player owner);
    void teleportToHome(final Player member);
    void ownerChange(final Player owner, final Player newOwner);
    void info(final Player sender, final String islandOwner);
}
