package com.securemicroservices.bookstore.service;

import com.securemicroservices.bookstore.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO getUserById(Long userId);
    List<UserDTO> getAllUsers();
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(Long userId, UserDTO userDTO);
    void deleteUser(Long userId);
}
