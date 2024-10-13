package com.duongw.sbsecurity.service;

import com.duongw.sbsecurity.entity.Role;
import com.duongw.sbsecurity.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    User getUserById(Long id);
    User getUserByUsername(String username);


    User updateUser(User user);

    User saveUser(User user);

    User changePassword(String email,

                        String password);

    List<User> getAllUser();
    void deleteUser(Long id);

    User getUserByEmail(String email);

    boolean existsByUsername(String username);
    boolean existedByEmail(String  email);

    // implement UserDetailService of core spring security



    List<Role> findAllRolesByUserId(Long userId);


}
