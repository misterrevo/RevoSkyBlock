package com.revo.skyblock.repository;

import com.revo.skyblock.exception.DeleteException;
import com.revo.skyblock.exception.SaveException;
import com.revo.skyblock.model.Island;

import java.util.List;
import java.util.Optional;

public interface IslandRepository {

    Island save(final Island island) throws SaveException;

    void deleteByOwner(final String uuid) throws DeleteException;

    Optional<Island> findByOwner(final String uuid);

    List<Island> findAll();

    Optional<Island> findByMember(final String uuid);
}
