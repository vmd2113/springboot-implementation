package com.duongw.sbsecurity.DTO.request.user;

import lombok.Data;

@Data
public class LoginRequest {
    String username;
    String password;

    // optional: platform (enum), device (enum), ip (string)
}
