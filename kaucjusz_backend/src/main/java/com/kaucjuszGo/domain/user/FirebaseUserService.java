package com.kaucjuszGo.domain.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.kaucjuszGo.core.config.FirebaseAuthenticationToken;

@Service
public class FirebaseUserService {
    public String getCurrentUserId() {
        FirebaseAuthenticationToken auth = getAuthentication();
        return (String) auth.getPrincipal();
    }

    public String getCurrentUserEmail() {
        FirebaseAuthenticationToken auth = getAuthentication();
        return auth.getEmail();
    }

    private FirebaseAuthenticationToken getAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof FirebaseAuthenticationToken) {
            return (FirebaseAuthenticationToken) auth;
        }
        throw new IllegalStateException("Brak autoryzacji Firebase w kontekście!");
    }
}
