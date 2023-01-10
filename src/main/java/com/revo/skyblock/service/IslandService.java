package com.revo.skyblock.service;

import com.revo.skyblock.model.Island;

public interface IslandService {

    Island createIsland(String ownerName);
    void deleteIsland(String ownerName);
    Island addMember(String memberName);
    Island removeMember(String memberName);
}
