package com.duongw.sbsecurity.DTO.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiResponse<T> {
    private HttpStatus httpStatus;
    private String message;
    private T data;

    public ApiResponse(HttpStatus httpStatus, String message, T data) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
