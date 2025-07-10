package com.aws.acquaintance.image_service.security;

import org.springframework.security.core.Authentication;

public class JwtAuthentication implements Authentication {
    private final String token;
    private boolean authenticated;

    public JwtAuthentication(String token) {
        this.token = token;
        this.authenticated = true;
    }

    @Override
    public Object getCredentials() { return token; }
    @Override
    public Object getPrincipal() { return token; }
    @Override
    public String getName() { return null; }
    @Override
    public boolean isAuthenticated() { return authenticated; }
    @Override
    public void setAuthenticated(boolean isAuthenticated) { this.authenticated = isAuthenticated; }
    @Override
    public Object getDetails() { return null; }
    @Override
    public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
        return java.util.Collections.emptyList();
    }
}
