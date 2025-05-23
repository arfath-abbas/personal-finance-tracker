package com.finance.tracker.arfath.repository;

import com.finance.tracker.arfath.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by username (useful for authentication)
    Optional<User> findByUsername(String username);

    // Find a user by email (optional, depending on your auth logic)
    Optional<User> findByEmail(String email);

    // Check if a username exists (useful for registration validation)
    boolean existsByUsername(String username);

    // Check if email exists
    boolean existsByEmail(String email);
}
