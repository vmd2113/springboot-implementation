package com.duongw.sbsecurity.repository;

import com.duongw.sbsecurity.entity.Role;
import com.duongw.sbsecurity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);


    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    @Query("SELECT u.roles FROM User u WHERE u.id = :userId")
    List<Role> findAllRolesByUserId(Long userId);
}
