package com.duongw.sbsecurity.DTO.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResetPasswordDTO {
    @NotBlank(message = "secretKey must be not blank")
    private String secretKey;

    @NotBlank(message = "password must be not blank")
    private String password;

    @NotBlank(message = "confirmPassword must be not blank")
    private String confirmPassword;

    public boolean checkConfirmPassword() {
        return password.equals(confirmPassword);
    }
}
