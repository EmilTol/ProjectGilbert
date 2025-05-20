package com.example.projectgilbert.application;

import com.example.projectgilbert.entity.User;
import com.example.projectgilbert.infrastructure.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, SortingService sortingService) {
        this.userRepository = userRepository;
    }

    public User getUserById (long id) {
        return userRepository.findById(id);
    }

    //updatere en bruger
    public void updateUser(long userId, String email, String password, String firstName, String lastName, String phoneNumber) {
        User user = userRepository.findById(userId);

        //updatere kun hvis der er input
        // sørger for hvis intet indtastet for firstName at det ikke bliver sat som null i db
        if (firstName != null && !firstName.isEmpty()) {
            user.setFirstName(firstName);
        }
        if (lastName != null && !lastName.isEmpty()) {
            user.setLastName(lastName);
        }
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            user.setPhoneNumber(phoneNumber);
        }
        if (email != null && !email.isEmpty()) {
            user.setEmail(email);
        }
        if (password != null && !password.isEmpty()) {
            //gør at nyt password bliver hashed
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            user.setPassword(hashedPassword);
        }
        //updatere
        userRepository.updateUser(user);
    }
}
