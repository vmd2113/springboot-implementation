package com.duongw.sbsecurity.DTO.request.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;

}
