import { apiService } from "./api";
import { API_PREFIX } from "./apiConstants";

export interface PackageResponseDTO {
    id: string; // UUID as string
    packageName: string;
    price: number;
}

const API = `${API_PREFIX}/package`;

export const packageService = {

    async getAllPackages(): Promise<PackageResponseDTO[]> {
        const response = await apiService.get(`${API}/all`);
        return response;
    }

}

