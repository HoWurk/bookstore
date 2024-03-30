package com.securemicroservices.dto.converter;

import com.securemicroservices.dto.UserDTO;
import com.securemicroservices.entity.User;
import org.springframework.stereotype.Component;


@Component
public class UserConverter {
    public UserDTO mapUserToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .build();
    }

    public User mapDTOToUser(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .build();
    }
}
