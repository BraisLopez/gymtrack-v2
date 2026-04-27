package com.brais.gymtrack.user;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.brais.gymtrack.user.dto.CreateUserRequest;
import com.brais.gymtrack.user.dto.UserResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //CREATE
    @PostMapping
    public UserResponse createUser(@RequestBody @Valid CreateUserRequest request) {
        User user = userService.createUser(
            request.getEmail(),
            request.getPassword(),
            UserRole.CLIENT
        );

        return new UserResponse(user);
    }
    

    //GET ALL
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(UserResponse::new)
                .toList();
    }

    //GET BY ID
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return new UserResponse(userService.getUserById(id));
    }

    //GET BY EMAIL
    @GetMapping("/by-email")
    public UserResponse getUserByEmail(@RequestParam String email) {
        return new UserResponse(userService.getUserByEmail(email));
    }

    //DEACTIVATE USER
    @PatchMapping("/{id}/deactivate")
    public UserResponse deactivateUser(@PathVariable Long id) {
        return new UserResponse(userService.deactivateUser(id));
    }

}