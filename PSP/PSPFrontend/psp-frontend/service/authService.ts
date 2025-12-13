import { apiService } from "./api"
import { API_PREFIX } from "./apiConstants";

interface UserDTO {
    id: string;
    name: string;
    username: string;
}

interface AuthResponseDTO {
  accessToken: string;
  tokenType: string;
  userDTO: UserDTO;
}

interface LoginDTO {
  username: string;
  password: string;
}

// Note: PSP backend may not have a traditional login endpoint
// This is a placeholder that can be adapted based on actual backend implementation
const AUTH_PREFIX = `${API_PREFIX}/auth`;

export const authService = {
  async login(username: string, password: string): Promise<AuthResponseDTO> {
    // This endpoint may need to be adjusted based on actual PSP backend implementation
    // For now, assuming there's an admin login endpoint
    const response = await apiService.post<AuthResponseDTO>(`${AUTH_PREFIX}/login`, {
      username,
      password,
    });
    // Store token in localStorage
    if (response.accessToken) {
      localStorage.setItem("access_token", response.accessToken);
      localStorage.setItem("token_type", response.tokenType);
      localStorage.setItem("user", JSON.stringify(response.userDTO));    
    }
    return response;
  },

  logout() {
    localStorage.removeItem("access_token");
    localStorage.removeItem("token_type");
    localStorage.removeItem("user");
  },
};

