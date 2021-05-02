package com.gustavnienkotter.ServiceOrderManager.repository;

import com.gustavnienkotter.ServiceOrderManager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    User findFirstById(Long id);
    boolean existsByUsername(String username);

    @Query(value = "select count(id) from m_user where authorities_roles LIKE '%ROLE_ADMIN%'", nativeQuery = true)
    Long countAdminUsers();
}
