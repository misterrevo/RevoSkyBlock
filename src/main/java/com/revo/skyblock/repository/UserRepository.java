package com.revo.skyblock.repository;

import com.revo.skyblock.exception.SaveException;
import com.revo.skyblock.model.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user) throws SaveException;
    Optional<User> findByUUID(String uuid);
}
