package com.duongw.sbsecurity.security;

import com.duongw.sbsecurity.DTO.request.user.LoginRequest;
import com.duongw.sbsecurity.DTO.request.user.RegisterRequest;
import com.duongw.sbsecurity.DTO.request.user.ResetPasswordDTO;
import com.duongw.sbsecurity.DTO.response.TokenResponse;
import com.duongw.sbsecurity.entity.Role;
import com.duongw.sbsecurity.entity.Token;
import com.duongw.sbsecurity.entity.User;
import com.duongw.sbsecurity.exception.AlreadyExistedException;
import com.duongw.sbsecurity.exception.InvalidDataException;
import com.duongw.sbsecurity.repository.RoleRepository;
import com.duongw.sbsecurity.repository.UserRepository;
import com.duongw.sbsecurity.security.token.TokenService;
import com.duongw.sbsecurity.service.IUserService;
import com.duongw.sbsecurity.service.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

import static org.springframework.http.HttpHeaders.REFERER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import static com.duongw.sbsecurity.enums.TokenTypes.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final IUserService userService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    // authenticate user login
    public TokenResponse authenticateLogin(LoginRequest loginRequest) {
        log.info("---------- authenticate ----------");

        var user = userService.getUserByUsername(loginRequest.getUsername());
        if (user == null) {
            log.warn("User not found: {}", loginRequest.getUsername());
            throw new UsernameNotFoundException("User not found");
        }

        log.info("User found: {}", user.getUsername());

        List<String> roles = userService.findAllRolesByUserId(user.getId()).stream()
                .map(Role::getName)
                .toList();
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword(),
                authorities
        ));

        log.info("User {} authenticated successfully", user.getUsername());

        // create new access token
        String accessToken = jwtService.generateToken(user);
        log.info("Access token generated for user {}", user.getUsername());

        // create new refresh token
        String refreshToken = jwtService.generateRefreshToken(user);
        log.info("Refresh token generated for user {}", user.getUsername());

        // save token to db
        tokenService.saveToken(Token.builder()
                .username(user.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(user)
                .build());

        log.info("Tokens saved to database for user {}", user.getUsername());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    public TokenResponse refreshToken(HttpServletRequest request) {
        log.info("---------- refreshToken ----------");

        final String refreshToken = request.getHeader(REFERER);
        if (StringUtils.isBlank(refreshToken)) {
            log.error("Refresh token is blank");
            throw new InvalidDataException("Token must be not blank");
        }

        final String userName = jwtService.extractUsername(refreshToken, REFRESH_TOKEN);
        var user = userService.getUserByUsername(userName);
        if (!jwtService.isValid(refreshToken, REFRESH_TOKEN, user)) {
            log.error("Invalid refresh token for user {}", userName);
            throw new InvalidDataException("Not allow access with this token");
        }

        log.info("Valid refresh token for user {}", userName);

        // create new access token
        String accessToken = jwtService.generateToken(user);
        log.info("New access token generated for user {}", userName);

        // save token to db
        tokenService.saveToken(Token.builder()
                .username(user.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());

        log.info("New token saved to database for user {}", user.getUsername());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    /**
     * Register user
     *
     * @param request
     * @return
     */
    public TokenResponse register(RegisterRequest request) {
        log.info("---------- register ----------");

        boolean exitedUser = userService.existsByUsername(request.getUsername());
        boolean exitedEmail = userService.existedByEmail(request.getEmail());

        if (exitedUser) {
            log.warn("Attempt to register existing user: {}", request.getUsername());
            throw new AlreadyExistedException("User with username: " + request.getUsername() + "already existing");
        }

        if (exitedEmail) {
            log.warn("Attempt to register existing user: {}", request.getUsername());
            throw new AlreadyExistedException("User with email: " + request.getEmail() + "already existing");
        }
        User user = new User();

        if (StringUtils.isBlank(request.getUsername())) {
            log.error("Username must be not blank");
            throw new InvalidDataException("Username must be not blank");
        }
        if (StringUtils.isBlank(request.getPassword())) {
            log.error("Password must be not blank");
            throw new InvalidDataException("Password must be not blank");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            log.error("Password mismatch for user {}", request.getUsername());
            throw new InvalidDataException("Password not match");
        }
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("ROLE_CUSTOMER"));
        user.setRoles(roles);

        user = userRepository.save(user);
        log.info("User {} registered successfully", user.getUsername());

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(accessToken, refreshToken, user);

        log.info("Tokens generated for user {}", user.getUsername());
        return new TokenResponse(accessToken, refreshToken, user.getId(), "Register success");
    }

    private void saveUserToken(String accessToken, String refreshToken, User user) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLogout(false);
        token.setUser(user);
        token.setUsername(user.getUsername());
        tokenService.saveToken(token);
        log.info("Token saved for user {}", user.getUsername());
    }

    // logout
    public String logout(HttpServletRequest request) {
        log.info("---------- logout ----------");
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            log.info("Access token: {}", token);

            if (StringUtils.isBlank(token)) {
                log.error("Token must be not blank");
                throw new InvalidDataException("Token must be not blank");
            }

            final String userName = jwtService.extractUsername(token, ACCESS_TOKEN);
            tokenService.deleteToken(userName);
            log.info("Token deleted for user {}", userName);
        } else {
            log.error("Token must be not blank or invalid");
            throw new InvalidDataException("Token must be not blank or invalid");
        }

        return "Deleted!";
    }

    public String forgetPassword(String email) {
        log.info("---------- forgetPassword ----------");
        boolean checkEmail = userService.existedByEmail(email);
        if (!checkEmail) {
            log.error("Email not exist");
            throw new InvalidDataException("Email not exist");
        }
        // check email exists or notZz
        User user = userService.getUserByEmail(email);


        // generate reset token
        String resetToken = jwtService.generateResetPasswordToken(user);

        // save to db
        tokenService.saveToken(Token.builder().username(user.getUsername()).resetPasswordToken(resetToken).build());

        // TODO send email to user
        String confirmLink = String.format("curl --location 'http://localhost:80/auth/reset-password' \\\n" +
                "--header 'accept: */*' \\\n" +
                "--header 'Content-Type: application/json' \\\n" +
                "--data '%s'", resetToken);
        log.info("--> confirmLink: {}", confirmLink);

        return resetToken;

    }

    public String resetPassword(String secretKey) {
        log.info("---------- resetPassword ----------");

        // validate token
        var user = validateToken(secretKey);

        // check token by username
        tokenService.getByUsername(user.getUsername());

        return "Reset";
    }

    public String changePassword(ResetPasswordDTO request) {
        log.info("---------- changePassword ----------");

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidDataException("Passwords do not match");
        }
        // get user by reset token
        var user = validateToken(request.getSecretKey());

        // update password
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.changePassword(user.getEmail(), request.getPassword());

        return "Changed";
    }

    private User validateToken(String token) {
        // validate token
        var userName = jwtService.extractUsername(token, RESET_PASSWORD_TOKEN);

        // validate user is active or not
        var user = userService.getUserByUsername(userName);
        if (!user.isEnabled()) {
            throw new InvalidDataException("User not active");
        }

        return user;
    }
}
