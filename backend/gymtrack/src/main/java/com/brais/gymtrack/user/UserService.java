package com.brais.gymtrack.user;

import java.util.List;

import org.springframework.stereotype.Service;

import com.brais.gymtrack.exception.customExceptions.EmailAlreadyExistsException;
import com.brais.gymtrack.exception.customExceptions.UserNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }



    public User createUser(String email, String password, UserRole role){
        if(userRepository.findByEmail(email).isPresent()){
            throw new EmailAlreadyExistsException(email);
        }
        
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(email, encodedPassword, role);

        return userRepository.save(user);
    }


    public List<User> getAllUsers(){      
        return userRepository.findAll();
    }


    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }


    public User getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    public User deactivateUser(Long id){
        User user = getUserById(id);
        user.setActive(false);
        return userRepository.save(user);
    }

}
