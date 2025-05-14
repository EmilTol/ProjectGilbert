package com.example.projectgilbert.infrastructure;

import com.example.projectgilbert.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User findByEmail(String email) {
        try {
            String sql = "SELECT user_id, email, password, first_name, last_name, phone_number FROM users WHERE email = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{email}, (rs, rowNum) -> {
                User user = new User(); // Henter brugeren udfra email i db og mapper resultatet til et user objekt
                user.setUserId(rs.getLong("user_id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setPhoneNumber(rs.getString("phone_number"));
                return user;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (email, password, first_name, last_name, phone_number) VALUES (?, ?, ?, ?, ?)";
        int result = jdbcTemplate.update(sql,
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber());
        return result == 1;
    }

    public void updateUser(User user) {
        String sql = "UPDATE users SET email = ?, password = ?, first_name = ?, last_name = ?, phone_number = ? WHERE user_id = ?";
        int updatedRows = jdbcTemplate.update(sql,
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getUserId());

        if (updatedRows == 0) {
            System.out.println("No rows updated" + user.getUserId());
        }
    }

    public User findById(long id) {
        try {
            String sql = "SELECT user_id, email, password, first_name, last_name, phone_number FROM users WHERE user_id = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                User user = new User();
                user.setUserId(rs.getLong("user_id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setPhoneNumber(rs.getString("phone_number"));
                return user;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void deleteById(long id) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<User> findAllUsers() {
        String sql = "SELECT user_id, email, password, first_name, last_name, phone_number FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setUserId(rs.getLong("user_id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setPhoneNumber(rs.getString("phone_number"));
            return user;
        });
    }
}