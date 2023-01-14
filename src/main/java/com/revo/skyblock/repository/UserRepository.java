package com.revo.skyblock.repository;

import com.revo.skyblock.exception.SaveException;
import com.revo.skyblock.model.User;

import java.util.Optional;

public interface UserRepository {
    User save(final User user) throws SaveException;
    Optional<User> findByUUID(final String uuid);
}
