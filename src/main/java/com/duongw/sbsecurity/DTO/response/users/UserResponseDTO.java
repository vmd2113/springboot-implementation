package com.duongw.sbsecurity.DTO.response.users;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class UserResponseDTO {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;   
    
}
