package org.example.pspbackend.dto.merchant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneratePasswordResponseDTO {
    private String merchantId;
    private String newPassword; // New API key
}


