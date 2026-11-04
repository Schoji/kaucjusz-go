package com.kaucjuszGo.core.config;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class FirebaseAuthenticationToken extends AbstractAuthenticationToken {
    private final String uid;
    private final String email;
    private final String token;

    public FirebaseAuthenticationToken(String uid, String email, String token,
            Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.uid = uid;
        this.email = email;
        this.token = token;
        setAuthenticated(true);
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return uid;
    }

}
