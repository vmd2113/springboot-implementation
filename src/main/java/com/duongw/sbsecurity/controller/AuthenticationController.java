package com.duongw.sbsecurity.controller;

import com.duongw.sbsecurity.DTO.request.LoginRequest;
import com.duongw.sbsecurity.DTO.request.RegisterRequest;
import com.duongw.sbsecurity.DTO.response.ApiResponse;
import com.duongw.sbsecurity.DTO.response.TokenResponse;
import com.duongw.sbsecurity.service.IUserService;
import com.duongw.sbsecurity.security.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Validated
@Slf4j
@Tag(name = "Authentication controller")
@RequiredArgsConstructor
public class AuthenticationController {

    private final IUserService userService;
    private final AuthenticationService authenticationService;


    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<?>> refreshToken(HttpServletRequest request) {
        try {
            TokenResponse token = authenticationService.refreshToken(request);
            ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.OK, "Refresh token successful", token);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<?> apiResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST, "Refresh token failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request) {
        log.info("---------- logout ----------");
        try {
            String status = authenticationService.logout(request);
            ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.OK, "Logout successful", status);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Logout failed: " + e.getMessage());
            return ResponseEntity.status(apiResponse.getHttpStatus()).body(apiResponse);
        }
    }

    @PostMapping("/access-token")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest loginRequest) {
        try {
            TokenResponse tokenResponse = authenticationService.authenticateLogin(loginRequest);
            ApiResponse<TokenResponse> apiResponse = new ApiResponse<>(HttpStatus.OK, "Login successful", tokenResponse);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<TokenResponse> apiResponse = new ApiResponse<>(HttpStatus.UNAUTHORIZED, "Login failed: " + e.getMessage());
            return ResponseEntity.status(apiResponse.getHttpStatus()).body(apiResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<TokenResponse>> register(@RequestBody RegisterRequest registerRequest) {
        log.info("---------- register ----------");
        try {
            TokenResponse tokenResponse = authenticationService.register(registerRequest);
            ApiResponse<TokenResponse> apiResponse = new ApiResponse<>(HttpStatus.CREATED, "Registration successful", tokenResponse);
            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
        } catch (Exception e) {
            ApiResponse<TokenResponse> apiResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST, "Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody String username, @RequestBody String password) {
        try {
            // Call your reset password logic here
            String result = "Password reset successful"; // Placeholder
            ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.OK, result, null);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST, "Password reset failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }

    //TODO: updating feature

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@RequestBody String username, @RequestBody String password) {
        try {
            // Call your change password logic here
            String result = "Password change successful"; // Placeholder
            ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.OK, result, null);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST, "Password change failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }

    @PostMapping("/confirm-email")
    public ResponseEntity<ApiResponse<String>> confirmEmail(@RequestBody String username, @RequestBody String password) {
        try {
            // Call your confirm email logic here
            String result = "Email confirmation successful"; // Placeholder
            ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.OK, result);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST, "Email confirmation failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody String username, @RequestBody String password) {
        try {
            // Call your forgot password logic here
            String result = "Forgot password request successful"; // Placeholder
            ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.OK, result, null);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST, "Forgot password request failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestBody String username, @RequestBody String password) {
        try {
            // Call your verify email logic here
            String result = "Email verification successful"; // Placeholder
            ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.OK, result, null);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST, "Email verification failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }

    @PostMapping("/resend-email")
    public ResponseEntity<ApiResponse<String>> resendEmail(@RequestBody String username, @RequestBody String password) {
        try {
            // Call your resend email logic here
            String result = "Email resend successful"; // Placeholder
            ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.OK, result, null);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST, "Email resend failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }
}
