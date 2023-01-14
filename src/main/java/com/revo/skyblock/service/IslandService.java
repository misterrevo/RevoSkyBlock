package com.revo.skyblock.service;

public interface IslandService {
    String createIsland(final String ownerName);
    String deleteIsland(final String ownerName);
    String addMember(final String ownerName, final String memberName);
    String removeMember(final String ownerName, final String memberName);
}
