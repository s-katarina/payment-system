import { apiService } from "./api";
import { API_PREFIX } from "./apiConstants";

export interface BuyPackageResponseDTO {
    redirectUrl: string;
}

export interface PurchasedPackageDTO {
    purchaseId: string;
    packageId: string;
    packageName: string;
    price: number;
    purchaseDate: string;
    status: 'INITIATED' | 'SUCCESS' | 'FAILURE' | 'ERROR';
}

const PURCHASE_API = `${API_PREFIX}/purchase`;
const PACKAGE_API = `${API_PREFIX}/package`;

export const purchaseService = {
    async buyPackage(packageId: string): Promise<BuyPackageResponseDTO> {
        const response = await apiService.post<BuyPackageResponseDTO>(
            `${PURCHASE_API}/package?packageId=${packageId}`
        );
        return response;
    },
    async getPurchasedPackages(): Promise<PurchasedPackageDTO[]> {
        const response = await apiService.get<PurchasedPackageDTO[]>(
            `${PACKAGE_API}/purchased`
        );
        return response;
    }
};

