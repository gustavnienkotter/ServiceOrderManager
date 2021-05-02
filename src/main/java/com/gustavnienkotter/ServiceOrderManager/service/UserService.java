package com.gustavnienkotter.ServiceOrderManager.service;

import com.gustavnienkotter.ServiceOrderManager.dto.user.UserDTO;
import com.gustavnienkotter.ServiceOrderManager.enums.AuthoritieRoleEnum;
import com.gustavnienkotter.ServiceOrderManager.enums.ErrorResponseEnum;
import com.gustavnienkotter.ServiceOrderManager.exception.BadRequestException;
import com.gustavnienkotter.ServiceOrderManager.model.User;
import com.gustavnienkotter.ServiceOrderManager.repository.UserRepository;
import com.gustavnienkotter.ServiceOrderManager.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService  {


    private final UserRepository userRepository;
    private final DateUtil dateUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException(ErrorResponseEnum.USER_NOT_FOUND.getValue()));
    }

    public Page<User> listAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Long countAdminUsersInDatabase() {
        return userRepository.countAdminUsers();
    }

    @Transactional
    public User create(UserDTO userDTO) {
        if (validUsername(userDTO.getUsername())) {
            throw new BadRequestException(ErrorResponseEnum.USERNAME_UNAVAILABLE.getValue());
        }
        userDTO.setId(null);
        userDTO.setAuthorities(AuthoritieRoleEnum.ROLE_USER.name());
        return save(userDTO);
    }

    public void createAdminUser() {
        User user = User.builder()
                .name("Admin")
                .username("admin")
                .password("admin")
                .registerDate(dateUtil.timestampNow())
                .authoritiesRoles(AuthoritieRoleEnum.ROLE_ADMIN.name())
                .build();
        userRepository.save(user);
    }

    private boolean validUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public User update(UserDTO userDTO) {
        User userInDatabase = findByIdOrThrowBadRequest(userDTO.getId());
        userDTO.setAuthorities(userInDatabase.getAuthoritiesRoles());
        return save(userDTO);
    }

    public User findByIdOrThrowBadRequest(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ErrorResponseEnum.USER_NOT_FOUND.getValue()));
    }

    @Transactional
    public void delete(Long id) {
        userRepository.delete(findFirstById(id));
    }

    public User findFirstById(Long id) {
        return userRepository.findFirstById(id);
    }

    private User save(UserDTO userDTO) {
        User user = userBuilder(userDTO);
        return userRepository.save(user);
    }

    private User userBuilder(UserDTO userDTO) {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return User.builder()
                .id(userDTO.getId())
                .name(userDTO.getName())
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .authoritiesRoles(userDTO.getAuthorities())
                .build();
    }
}
