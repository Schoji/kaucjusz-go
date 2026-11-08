package com.kaucjuszGo.core.config;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {
    private final FirebaseAuth firebaseAuth;
    @Value("${firebase.dev-mode:false}")
    private boolean devMode;

    public FirebaseAuthenticationFilter(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        if (devMode && token.startsWith("dev-")) {
            String mockUid = token; // Np. "dev-user-1"
            String mockEmail = token + "@kaucjusz.pl";
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            FirebaseAuthenticationToken devAuth = new FirebaseAuthenticationToken(mockUid, mockEmail, token, authorities);
            SecurityContextHolder.getContext().setAuthentication(devAuth);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(token);
            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();

            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            FirebaseAuthenticationToken authentication = new FirebaseAuthenticationToken(uid, email, token,
                    authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        }

        catch (FirebaseAuthException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Firebase ID token");
            return;
        }
    }
}