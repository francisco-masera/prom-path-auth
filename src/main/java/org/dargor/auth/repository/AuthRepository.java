package org.dargor.auth.repository;

import java.util.Optional;

import org.dargor.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<User, String> {

    Optional<User> getUserByEmail(String email);

}
