package org.example.webshopbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer ";
    private UserDTO userDTO;

    public AuthResponseDTO(String accessToken, String id, String username, String name) {
        UserDTO userDTO = new UserDTO(id, name, username);
        this.accessToken = accessToken;
        this.userDTO = userDTO;
    }
}
