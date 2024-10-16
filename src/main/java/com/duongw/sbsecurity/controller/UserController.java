package com.duongw.sbsecurity.controller;

import com.duongw.sbsecurity.DTO.response.ApiResponse;
import com.duongw.sbsecurity.entity.User;
import com.duongw.sbsecurity.exception.ResourceNotFoundException;
import com.duongw.sbsecurity.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/api/users")
public class UserController {

    private final IUserService userService;

    // ADMIN có thể xem tất cả người dùng
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping(path = "/all")
    public ResponseEntity<ApiResponse<?>> getAllUsers() {
        log.info("---------- getAllUsers ----------");
        try {
            List<User> list = userService.getAllUser();
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, "Get all users success", list), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Get all users failed", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ADMIN và chính người dùng có thể xem thông tin chi tiết theo ID
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or #id == principal.id")
    @GetMapping(path = "/by-id/{id}")
    public ResponseEntity<ApiResponse<?>> getUserById(@PathVariable(name = "id") Long id) {
        log.info("---------- getUserById ----------");
        try {
            User user = userService.getUserById(id);
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, "Get user by id success", user), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.NOT_FOUND, "Get user by id failed", null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Get user by id failed", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ADMIN và chính người dùng có thể xem thông tin theo username
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_STAFF')")
    @GetMapping(path = "/by-username/{username}")
    public ResponseEntity<ApiResponse<?>> getUserByUsername(@PathVariable(value = "username") String username) {
        log.info("---------- getUserByUsername ----------");
        try {
            User user = userService.getUserByUsername(username);
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, "Get user by username success", user), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.NOT_FOUND, "Get user by username failed", null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Get user by username failed", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAnyAuthority('ROLE_STAFF')")
    @GetMapping(path = "/by-email/{email}")
    public ResponseEntity<ApiResponse<?>> getUserByEmail(@PathVariable(value = "email") String email) {
        log.info("---------- getUserByEmail ----------");
        try {
            User user = userService.getUserByEmail(email);
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, "Get user by email success", user), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.NOT_FOUND, "Get user by email failed", null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Get user by email failed", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Chỉ ADMIN có quyền xóa người dùng
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable(name = "id") Long id) {
        log.info("---------- deleteUser ----------");
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK, "Delete user success"), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.NOT_FOUND, "Delete user failed", null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Delete user failed", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
