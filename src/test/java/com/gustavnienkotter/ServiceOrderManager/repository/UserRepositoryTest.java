package com.gustavnienkotter.ServiceOrderManager.repository;

import com.gustavnienkotter.ServiceOrderManager.enums.AuthoritieRoleEnum;
import com.gustavnienkotter.ServiceOrderManager.exception.BadRequestException;
import com.gustavnienkotter.ServiceOrderManager.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for User Repository")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Save persists User when Successful")
    void savePersistUserWhenSuccessful() {
        User userToBeSaved = createUser();
        User savedUser = this.userRepository.save(userToBeSaved);

        Assertions.assertThat(savedUser).isNotNull();

        Assertions.assertThat(savedUser.getId()).isNotNull();

        Assertions.assertThat(savedUser.getName()).isEqualTo(userToBeSaved.getName());

        Assertions.assertThat(savedUser.getUsername()).isEqualTo(userToBeSaved.getUsername());

        Assertions.assertThat(savedUser.getPassword()).isEqualTo(userToBeSaved.getPassword());

        Assertions.assertThat(savedUser.getAuthoritiesRoles()).isEqualTo(userToBeSaved.getAuthoritiesRoles());
    }

    @Test
    @DisplayName("Save not persists when username not included")
    void saveNotPersistsWhenUsernameNotInformed() {
        User userToBeSaved = createUser();

        userToBeSaved.setUsername(null);

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.userRepository.save(userToBeSaved));
    }

    @Test
    @DisplayName("Save not persists when Name not included")
    void saveNotPersistsWhenNameNotInformed() {
        User userToBeSaved = createUser();

        userToBeSaved.setName(null);

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.userRepository.save(userToBeSaved));
    }

    @Test
    @DisplayName("Save not persists when Password not included")
    void saveNotPersistsWhenPasswordNotInformed() {
        User userToBeSaved = createUser();

        userToBeSaved.setPassword(null);

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.userRepository.save(userToBeSaved));
    }

    @Test
    @DisplayName("Save update Optional User when Successful")
    void saveUpdatesOptionalUserWhenSuccessful() {
        User userToBeSaved = createUser();
        User savedUser = this.userRepository.save(userToBeSaved);

        savedUser.setName("user");

        User userUpdated = this.userRepository.save(savedUser);

        Assertions.assertThat(userUpdated).isNotNull();

        Assertions.assertThat(userUpdated.getId()).isNotNull();

        Assertions.assertThat(userUpdated.getName()).isEqualTo(savedUser.getName());

        Assertions.assertThat(userUpdated.getUsername()).isEqualTo(userToBeSaved.getUsername());

        Assertions.assertThat(userUpdated.getPassword()).isEqualTo(userToBeSaved.getPassword());

        Assertions.assertThat(userUpdated.getAuthoritiesRoles()).isEqualTo(userToBeSaved.getAuthoritiesRoles());
    }

    @Test
    @DisplayName("Delete removes User when Successful")
    void deleteRemovesUserWhenSuccessful() {
        User userToBeSaved = createUser();
        User savedUser = this.userRepository.save(userToBeSaved);

        this.userRepository.delete(savedUser);

        Optional<User> userOptional = this.userRepository.findById(savedUser.getId());

        Assertions.assertThat(userOptional).isEmpty();
    }

    @Test
    @DisplayName("Find by Username returns User when Successful")
    void findbyUsernameReturnsUserWhenSuccessful() {
        User userToBeSaved = createUser();
        User savedUser = this.userRepository.save(userToBeSaved);

        Optional<User> userOptional = this.userRepository.findByUsername(savedUser.getUsername());

        Assertions.assertThat(userOptional).isNotNull();

        Assertions.assertThat(userOptional).isNotEmpty();

        Assertions.assertThat(userOptional.get().getUsername()).isEqualTo(savedUser.getUsername());
    }

    @Test
    @DisplayName("Find by Username returns Null when no User found")
    void findbyUsernameReturnsNullWhenSuccessful() {
        Optional<User> userOptional = this.userRepository.findByUsername("null.username");

        Assertions.assertThat(userOptional).isEmpty();

    }

    @Test
    @DisplayName("Exists by Username returns true if user found")
    void existsByUsernameReturnsTrueIfUserFound() {
        User userToSave = createUser();
        userRepository.save(userToSave);

        Assertions.assertThat(userRepository.existsByUsername(userToSave.getUsername())).isTrue();

    }

    @Test
    @DisplayName("Exists by Username returns false if user not found")
    void existsByUsernameReturnsFalseIfUserNotFound() {
        User userToSave = createUser();
        userRepository.save(userToSave);

        Assertions.assertThat(userRepository.existsByUsername("random.username")).isFalse();

    }

    @Test
    @DisplayName("Count Admin users returns 1 if successful")
    void countAdminUsersReturnsOneIfSuccessful() {
        User userToSave = createUser();
        User adminToSave = createAdminUser();
        userRepository.save(userToSave);
        userRepository.save(adminToSave);

        Assertions.assertThat(userRepository.countAdminUsers()).isEqualTo(1L);

    }

    @Test
    @DisplayName("Count Admin users returns 0 if not found")
    void countAdminUsersReturnsZeroIfNotFound() {
        User userToSave = createUser();
        userRepository.save(userToSave);

        Assertions.assertThat(userRepository.countAdminUsers()).isEqualTo(1L);

    }

    private User createUser(){
        return User.builder()
                .name("Test user")
                .username("user.test")
                .password("test")
                .authoritiesRoles(AuthoritieRoleEnum.ROLE_USER.name())
                .build();
    }

    private User createAdminUser(){
        return User.builder()
                .name("Test user")
                .username("user.test")
                .password("test")
                .authoritiesRoles(AuthoritieRoleEnum.ROLE_ADMIN.name())
                .build();
    }
}