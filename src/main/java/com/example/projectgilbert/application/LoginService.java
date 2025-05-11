package com.example.projectgilbert.application;

import com.example.projectgilbert.entity.User;
import com.example.projectgilbert.infrastructure.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) { // Tjekker at brugeren findes og bekræfter kodeord med bcrypt
            return user;
        }
        return null;
    }

    public boolean register(User user, String confirmPassword) {
        if (!user.getPassword().equals(confirmPassword)) { // Tejkker vores bekæft om de 2 kodeord stemmer
            return false;
        }
        if (userRepository.findByEmail(user.getEmail()) != null) { // Tjekker om bruger allerede findes
            return false;
        }
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()); // crypt / hash af passowrd
        user.setPassword(hashedPassword);
        return userRepository.registerUser(user);
    }
}
