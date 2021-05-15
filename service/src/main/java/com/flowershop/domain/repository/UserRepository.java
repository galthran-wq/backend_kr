 
package com.flowershop.domain.repository;

import com.flowershop.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    User save(User user);
}
