package com.duongw.sbsecurity.security;

import com.duongw.sbsecurity.enums.TokenTypes;
import com.duongw.sbsecurity.exception.InvalidDataException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service

public class JwtServiceImpl implements JwtService {

    @Value("${jwt.access}")
    String secretKey;

    @Value("${jwt.refreshKey}")
    String refreshKey;

    @Value("${jwt.resetPassword}")
    String resetPasswordKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpire;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpire;

    @Value("${jwt.reset-password-expiration}")
    private long resetPasswordTokenExpire;

    // TODO: implement generate token
    @Override
    public String generateToken(UserDetails user) {
        return generateToken(Map.of("userId", user.getAuthorities()), user, accessTokenExpire);
    }

    private String generateToken(Map<String, Object> claims, UserDetails userDetails, long accessTokenExpire) {
        return Jwts.builder()
                .setClaims(claims) // information in payload (email, name, etc)
                .setSubject(userDetails.getUsername()) // username
                .setIssuedAt(new Date(System.currentTimeMillis())) // time e
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpire))
                .signWith(getKey(TokenTypes.ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(TokenTypes type) {
        switch (type) {
            case ACCESS_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
            }
            case REFRESH_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));
            }
            case RESET_PASSWORD_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(resetPasswordKey));
            }
            default -> throw new InvalidDataException("Invalid token type");
        }


    }

    @Override
    public String generateRefreshToken(UserDetails user) {
        return generateRefreshToken(new HashMap<>(), user, refreshTokenExpire);
    }


    private String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails, long refreshTokenExpire) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis())) // time e
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpire))
                .signWith(getKey(TokenTypes.REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateResetPasswordToken(UserDetails user) {
        return generateResetPasswordToken(new HashMap<>(), user, resetPasswordTokenExpire);
    }

    private String generateResetPasswordToken(Map<String, Object> claims, UserDetails userDetails, long resetPasswordTokenExpire) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis())) // time e
                .setExpiration(new Date(System.currentTimeMillis() + resetPasswordTokenExpire))
                .signWith(getKey(TokenTypes.RESET_PASSWORD_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }


    @Override
    public String extractUsername(String token, TokenTypes type) {
        return extractClaim(token, type, Claims::getSubject);
    }


    @Override
    public boolean isValid(String token, TokenTypes type, UserDetails userDetails) {
        final String username = extractUsername(token, type);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, type));
    }


    private <T> T extractClaim(String token, TokenTypes type, Function<Claims, T> claimResolver) {
        final Claims claims = extraAllClaim(token, type);
        return claimResolver.apply(claims);
    }

    private Claims extraAllClaim(String token, TokenTypes type) {
        return Jwts.parserBuilder().setSigningKey(getKey(type)).build().parseClaimsJws(token).getBody();
    }


    private Date extractExpiration(String token, TokenTypes type) {
        return extractClaim(token, type, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token, TokenTypes type) {
        Date expiration = extractExpiration(token, type);
        if (expiration == null) {
            // Token không có thông tin expiration
            return true; // Bạn có thể trả về true (token đã hết hạn) hoặc xử lý khác tùy theo logic của bạn
        }
        return expiration.before(new Date());
    }


}
