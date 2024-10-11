package com.duongw.sbsecurity.DTO.request;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
@Builder
public class RegisterRequest {
    String username;
    String password;
    String confirmPassword;
}
