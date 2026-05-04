package com.brais.gymtrack.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brais.gymtrack.auth.dto.ChangePasswordRequest;
import com.brais.gymtrack.auth.dto.LoginRequest;
import com.brais.gymtrack.auth.dto.LoginResponse;
import com.brais.gymtrack.user.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login (@RequestBody @Valid LoginRequest request){
        return authService.login(
            request.getEmail(), 
            request.getPassword()
        );
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(
        @AuthenticationPrincipal User user,
        @RequestBody @Valid ChangePasswordRequest request
    ){
        authService.changePassword(
            user,
            request.getCurrentPassword(),
            request.getNewPassword()
        );
        return ResponseEntity.noContent().build();
    }

}
