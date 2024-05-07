package com.securemicroservices.dto;

import com.securemicroservices.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    @NotEmpty(message = "Username must not be empty")
    private String username;

    @NotEmpty(message = "Password must not be empty")
    private String password;

    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Role can not be null")
    private Role role;
}
