package com.example.projectgilbert.application;

import com.example.projectgilbert.entity.User;
import com.example.projectgilbert.infrastructure.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    static class FakeUserRepository extends UserRepository {
        User storedUser;

        @Override
        public User findById(long userId) {
            return storedUser;
        }

        @Override
        public void updateUser(User user) {
            this.storedUser = user;
        }
    }

        @Test
        void updateUser_updatesUser() {
            FakeUserRepository fakeRepo = new FakeUserRepository();

            User existingUser = new User();
            existingUser.setUserId(1L);
            existingUser.setFirstName("OldFirstName");
            fakeRepo.storedUser = existingUser;

            UserService userService = new UserService(fakeRepo, null);

            userService.updateUser(1L, "newemail@gmail.com", "newpass", "NewFirst", "NewLast", "12345678");

            User updatedUser = fakeRepo.storedUser;

            assertEquals("NewFirst", updatedUser.getFirstName());
            assertEquals("NewLast", updatedUser.getLastName());
            assertEquals("newemail@gmail.com", updatedUser.getEmail());
            assertEquals("12345678", updatedUser.getPhoneNumber());
            assertNotNull(updatedUser.getPassword());
            assertNotEquals("newpass", updatedUser.getPassword());  // fordi password er hashed
        }

}