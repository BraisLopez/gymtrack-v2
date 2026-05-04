package com.brais.gymtrack.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.brais.gymtrack.auth.dto.LoginResponse;
import com.brais.gymtrack.exception.customExceptions.InvalidCredentialsException;
import com.brais.gymtrack.exception.customExceptions.InvalidCurrentPasswordException;
import com.brais.gymtrack.user.User;
import com.brais.gymtrack.user.UserRepository;
import com.brais.gymtrack.user.dto.UserResponse;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(String email, String password){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException());
        
        if(!passwordEncoder.matches(password, user.getPasswordHash())){
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(user);
        return new LoginResponse(token, new UserResponse(user));
    }

    public void changePassword(User user, String currentPassword, String newPassword){
        if(!passwordEncoder.matches(currentPassword, user.getPasswordHash())){
            throw new InvalidCurrentPasswordException();
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);

        user.setPasswordHash(encodedNewPassword);
        user.setMustChangePassword(false);
        userRepository.save(user);
    }
}
