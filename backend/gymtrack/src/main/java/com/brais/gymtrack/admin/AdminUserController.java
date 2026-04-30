package com.brais.gymtrack.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brais.gymtrack.exception.customExceptions.BadRequestException;
import com.brais.gymtrack.user.User;
import com.brais.gymtrack.user.UserRole;
import com.brais.gymtrack.user.UserService;
import com.brais.gymtrack.user.dto.CreateUserRequest;
import com.brais.gymtrack.user.dto.UserResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController{
    
    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse createAdminUser(@RequestBody @Valid CreateUserRequest request) {

        if(request.getRole().equals(UserRole.ADMIN)) {
            throw new BadRequestException("Role cannot be ADMIN");
        }

        User user = userService.createUser(
            request.getEmail(),
            request.getPassword(),
            request.getRole()
        );

        return new UserResponse(user);
    }
}
