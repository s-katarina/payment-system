package org.example.webshopbackend.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    @NotNull
    @Size(max = 100, message = "Username cannot be longer than 100 characters")
    private String username;
    @NotNull(message = "Password is required!")
    private String password;

}