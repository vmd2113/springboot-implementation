package com.duongw.sbsecurity.security.token;

import com.duongw.sbsecurity.entity.Token;

public  interface ITokenService {
    public Token getByUsername(String username);
    public int saveToken(Token token);
    public void deleteToken(String username);


}
