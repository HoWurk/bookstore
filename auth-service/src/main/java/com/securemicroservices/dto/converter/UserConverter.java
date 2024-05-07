package com.securemicroservices.dto.converter;

import com.securemicroservices.dto.UserDTO;
import com.securemicroservices.entity.User;
import org.springframework.stereotype.Component;


@Component
public class UserConverter {
    public User mapDTOToUser(UserDTO userDTO) {
        return User.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .role(userDTO.getRole())
                .build();
    }
}
