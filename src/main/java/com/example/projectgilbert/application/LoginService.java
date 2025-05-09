package com.example.projectgilbert.application;

import com.example.projectgilbert.entity.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginService {
    private final Map<String, User> userStore;

    public LoginService() { // Det her er bare for at teste da vi ikke har en DB
        userStore = new HashMap<>();
        User user1 = new User();
        user1.setEmail("Emil@gmail.com");
        user1.setPassword("1234");
        user1.setFirstName("Emil");
        user1.setLastName("Toltov");
        userStore.put(user1.getEmail(), user1);
    }

    public User login(String email, String password) { // Skal opdateres når vi har DB, bare for at teste igen.
        User user = userStore.get(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
