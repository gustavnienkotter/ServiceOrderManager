package com.gustavnienkotter.ServiceOrderManager.repository;

import com.gustavnienkotter.ServiceOrderManager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    User findFirstById(Long id);
    boolean existsByUsername(String username);
}
