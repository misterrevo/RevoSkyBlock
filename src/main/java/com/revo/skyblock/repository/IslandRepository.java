package com.revo.skyblock.repository;

import com.revo.skyblock.exception.DeleteException;
import com.revo.skyblock.exception.SaveException;
import com.revo.skyblock.model.Island;

import java.util.Optional;

public interface IslandRepository {

    Island save(final Island island) throws SaveException;

    void deleteByOwnerName(final String ownerName) throws DeleteException;

    Optional<Island> findByOwnerName(final String ownerName);
}
