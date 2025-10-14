import { apiService } from "./api";
import { API_PREFIX } from "./apiConstants";

export interface PackageResponseDTO {
    id: string; // UUID as string
    packageName: string;
    price: number;
}

const API = `${API_PREFIX}/packages`;

export const packageService = {

    async getAllPackages(): Promise<PackageResponseDTO[]> {
        const response = await apiService.get(`${API}`);
        return response;
    }

}

