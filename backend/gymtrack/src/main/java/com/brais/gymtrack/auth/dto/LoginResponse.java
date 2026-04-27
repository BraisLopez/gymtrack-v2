package com.brais.gymtrack.auth.dto;

import com.brais.gymtrack.user.dto.UserResponse;

public class LoginResponse {
    
    private String token;
    private UserResponse user;
    
    public LoginResponse(String token, UserResponse user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public UserResponse getUser() {
        return user;
    }

}
