import { apiService } from "./api";

interface UserDTO {
    id: string;
    name: string;
    username: string;
}
interface AuthResponseDTO {
  accessToken: string;
  tokenType: string;
  user: UserDTO;
}

interface LoginDTO {
  username: string;
  password: string;
}

export const authService = {
  async login(username: string, password: string): Promise<AuthResponseDTO> {
    // Send POST to /login with username and password
    const response = await apiService.post<AuthResponseDTO>("/login", {
      username,
      password,
    });
    // Store token in localStorage
    if (response.accessToken) {
      localStorage.setItem("access_token", response.accessToken);
      localStorage.setItem("token_type", response.tokenType);
      localStorage.setItem("user", JSON.stringify(response.user));    
    }
    return response;
  },

  async register(username: string, name: string, password: string): Promise<void> {
    await apiService.post("/register", {
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