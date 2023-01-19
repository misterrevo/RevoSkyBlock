package com.revo.skyblock.service;

import org.bukkit.Location;

public interface IslandService {
    String createIsland(final String ownerName);
    String deleteIsland(final String ownerName);
    String addMember(final String ownerName, final String memberName);
    String removeMember(final String ownerName, final String memberName);
    String setHome(final String ownerName, final Location location);
    String teleportToHome(final String memberName);
}
