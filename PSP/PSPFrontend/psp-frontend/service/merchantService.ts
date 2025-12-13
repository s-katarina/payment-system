import { apiService } from "./api";
import { API_PREFIX } from "./apiConstants";

export interface Merchant {
  merchantId: string;
  merchantName: string;
  merchantPassword?: string;
  active: boolean;
}

export interface CreateMerchantRequest {
  merchantName: string;
  currency: string;
  successUrl: string;
  failUrl: string;
  errorUrl: string;
  paymentMethodIds?: string[];
}

export interface CreateMerchantResponse {
  merchantId: string;
  merchantName: string;
  merchantPassword: string;
}

export interface UpdateMerchantRequest {
  merchantName?: string;
  active?: boolean;
}

export interface GeneratePasswordResponse {
  merchantId: string;
  newPassword: string; // New API key
}

const MERCHANT_PREFIX = `${API_PREFIX}/merchant`;

export const merchantService = {
  async getAllMerchants(): Promise<Merchant[]> {
    return apiService.get<Merchant[]>(MERCHANT_PREFIX);
  },

  async getMerchantById(merchantId: string): Promise<Merchant> {
    return apiService.get<Merchant>(`${MERCHANT_PREFIX}/${merchantId}`);
  },

  async createMerchant(request: CreateMerchantRequest): Promise<CreateMerchantResponse> {
    return apiService.post<CreateMerchantResponse>(`${MERCHANT_PREFIX}/register`, request);
  },

  async updateMerchant(merchantId: string, request: UpdateMerchantRequest): Promise<CreateMerchantResponse> {
    return apiService.put<CreateMerchantResponse>(`${MERCHANT_PREFIX}/${merchantId}`, request);
  },

  async deleteMerchant(merchantId: string): Promise<void> {
    return apiService.delete(`${MERCHANT_PREFIX}/${merchantId}`);
  },

  async generatePassword(merchantId: string): Promise<GeneratePasswordResponse> {
    return apiService.post<GeneratePasswordResponse>(`${MERCHANT_PREFIX}/${merchantId}/generate-password`);
  },

  async getPaymentMethodsForMerchant(merchantId: string) {
    return apiService.get(`${MERCHANT_PREFIX}/${merchantId}/payment-methods`);
  },
};

