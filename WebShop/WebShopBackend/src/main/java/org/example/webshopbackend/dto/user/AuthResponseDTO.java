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
    private String username;

    public AuthResponseDTO(String accessToken, String username) {

        this.accessToken = accessToken;
        this.username = username;
    }
}
