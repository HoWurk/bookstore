package com.securemicroservices.service;

import com.securemicroservices.dto.UserDTO;
import com.securemicroservices.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    User getUserById(Long userId);
    List<User> getAllUsers();
    User createUser(UserDTO userDTO);
    User updateUser(Long userId, UserDTO userDTO);
    void deleteUser(Long userId);
    UserDetailsService userDetailsService();
}
