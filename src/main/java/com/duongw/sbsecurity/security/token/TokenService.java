package com.duongw.sbsecurity.security.token;

import com.duongw.sbsecurity.entity.Token;
import com.duongw.sbsecurity.exception.ResourceNotFoundException;
import com.duongw.sbsecurity.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {
    private final TokenRepository tokenRepository;
    @Override
    public Token getByUsername(String username) {
        return tokenRepository.findByUsername(username).orElseThrow(()-> new ResourceNotFoundException("Token not found"));
    }

    @Override
    public int saveToken(Token token) {
        Optional<Token> optional = tokenRepository.findByUsername(token.getUsername());
        if (optional.isEmpty()) {
            tokenRepository.save(token);
            return Math.toIntExact(token.getId());
        } else {
            Token t = optional.get();
            t.setAccessToken(token.getAccessToken());
            t.setRefreshToken(token.getRefreshToken());
            tokenRepository.save(t);
            return Math.toIntExact(t.getId());
        }
    }

    @Override
    public void deleteToken(String username) {
        Token token = getByUsername(username);
        tokenRepository.delete(token);
    }
}
