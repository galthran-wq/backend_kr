
package com.flowershop.infrastructure.db.jpa;

import com.flowershop.domain.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DataUserRepository extends CrudRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

}
