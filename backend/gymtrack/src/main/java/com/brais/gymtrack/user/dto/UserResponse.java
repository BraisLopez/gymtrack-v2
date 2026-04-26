package com.brais.gymtrack.user.dto;

import java.time.LocalDateTime;

import com.brais.gymtrack.user.User;
import com.brais.gymtrack.user.UserRole;

public class UserResponse {
    
    private Long id;
    private String email;
    private UserRole role;
    private boolean active;
    private LocalDateTime createdAt;

    public UserResponse(User user) {
        this.id = user.getUserId();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.active = user.isActive();
        this.createdAt = user.getCreatedAt();
    }

    public Long getUserId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
