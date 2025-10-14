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

const AUTH_PREFIX = `${API_PREFIX}/auth`;

export const authService = {
  async login(username: string, password: string): Promise<AuthResponseDTO> {
    // Send POST to /login with username and password
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

  async register(username: string, name: string, password: string): Promise<void> {
    await apiService.post(`${AUTH_PREFIX}/register`, {
      username,
      name,
      password,
    });
  },

  logout() {
    localStorage.removeItem("access_token");
    localStorage.removeItem("token_type");
    localStorage.removeItem("username");
  },
};