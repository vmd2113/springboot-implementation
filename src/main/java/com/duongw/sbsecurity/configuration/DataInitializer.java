package com.duongw.sbsecurity.configuration;

import com.duongw.sbsecurity.entity.Role;
import com.duongw.sbsecurity.entity.User;
import com.duongw.sbsecurity.repository.RoleRepository;
import com.duongw.sbsecurity.repository.UserRepository;
import com.duongw.sbsecurity.security.AuthenticationService;
import com.duongw.sbsecurity.security.JwtServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration

public class DataInitializer {
    @Value("${app.data.init:false}")
    private boolean dataInit;


    @Bean
    CommandLineRunner initData(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationService authenticationService,
            JwtServiceImpl jwtService
    ) {
        return args -> {

            if (dataInit) {
                Set<Role> roles = new HashSet<>();
                // Tạo các vai trò
                Role adminRole = roleRepository.findByName("ROLE_ADMIN");
                Role staffRole = roleRepository.findByName("ROLE_STAFF");
                Role customerRole = roleRepository.findByName("ROLE_CUSTOMER");

                roles.add(adminRole);
                roles.add(staffRole);
                roles.add(customerRole);

                // Tạo người dùng với vai trò ROLE_ADMIN
                User adminUser = User.builder()
                        .username("admin")
                        .email("trinhcandy2113@gmail.com")
                        .enabled(true)
                        .password(passwordEncoder.encode("admin123"))
                        .roles(roles)
                        .build();
                userRepository.save(adminUser);

                String accessToken_ = jwtService.generateToken(adminUser);
                String refreshToken_ = jwtService.generateRefreshToken(adminUser);
                authenticationService.saveUserToken(accessToken_, refreshToken_, adminUser);

                // Tạo người dùng với vai trò STAFF
                User staffUser1 = User.builder()
                        .username("staff1")
                        .email("chungminhg9999@gmail.com")
                        .enabled(true)
                        .password(passwordEncoder.encode("staff123"))
                        .roles(new HashSet<>(Arrays.asList(staffRole)))
                        .build();
                userRepository.save(staffUser1);

                String accessTokenStaff1 = jwtService.generateToken(staffUser1);
                String refreshTokenStaff1 = jwtService.generateRefreshToken(staffUser1);
                authenticationService.saveUserToken(accessTokenStaff1, refreshTokenStaff1, staffUser1);

                // Tạo thêm người dùng STAFF khác...
            } else {
                System.out.println("Data initialization is disabled.");
            }
        };
    }
}
