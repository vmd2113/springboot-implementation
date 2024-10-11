package com.duongw.sbsecurity.service.impl;

import com.duongw.sbsecurity.entity.Role;
import com.duongw.sbsecurity.entity.User;
import com.duongw.sbsecurity.exception.ResourceNotFoundException;
import com.duongw.sbsecurity.repository.UserRepository;
import com.duongw.sbsecurity.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class UserService implements IUserService {
    private final UserRepository userRepository;


    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public User updateUser(User user) {
        User saveUser = User.builder()
                .password(user.getPassword())
                .username(user.getUsername())
                .build();

        return userRepository.save(saveUser);
    }

    @Override
    public User saveUser(User user) {
        User saveUser = User.builder()
                .password(user.getPassword())
                .username(user.getUsername())
                .build();

        return userRepository.save(saveUser);
    }

    @Override
    public List<User> getAllUser() {
        List<User> userList = userRepository.findAll();
        return userList;
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);

    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    // implement UserServiceDetail
    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public List<Role> findAllRolesByUserId(Long userId) {
        return userRepository.findAllRolesByUserId(userId);
    }
}
