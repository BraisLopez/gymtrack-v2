package com.brais.gymtrack.user;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    public User createUser(String email, String passwordHash, UserRole role){
        if(userRepository.findByEmail(email).isPresent()){
            throw new IllegalArgumentException("This email is in use");
        }
        
        User user = new User(email, passwordHash, role);

        return userRepository.save(user);
    }


    public List<User> getAllUsers(){      
        return userRepository.findAll();
    }


    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }


    public User getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User deactivateUser(Long id){
        User user = getUserById(id);
        user.setActive(false);
        return userRepository.save(user);
    }

}
