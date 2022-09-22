package com.piotrdomagalski.planning.security;

import java.util.List;

public class LoginResponse {
    private final String accessToken;
    private final String refreshToken;
    private final List<String> roles;

    public LoginResponse(String accessToken, String refreshToken, List<String> roles) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.roles = roles;
    }
}

