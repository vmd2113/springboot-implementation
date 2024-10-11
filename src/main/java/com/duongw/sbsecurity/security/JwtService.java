package com.duongw.sbsecurity.security;

import com.duongw.sbsecurity.enums.TokenTypes;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;


public interface JwtService {

    String generateToken( UserDetails userDetails);

    String generateRefreshToken(UserDetails user);

    String extractUsername(String token, TokenTypes type);

    boolean isValid(String token, TokenTypes type, UserDetails userDetails);

}
