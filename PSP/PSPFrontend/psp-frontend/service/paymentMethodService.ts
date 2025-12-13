import { apiService } from "./api";
import { API_PREFIX } from "./apiConstants";

export interface PaymentMethod {
  id: string;
  name: string;
  serviceUrl?: string;
}

export interface CreatePaymentMethodRequest {
  name: string;
  serviceUrl?: string;
}

export interface UpdatePaymentMethodRequest {
  name: string;
}

export interface UpdatePaymentMethodServiceUrlRequest {
  serviceUrl: string;
}

const PAYMENT_METHOD_PREFIX = `${API_PREFIX}/payment-method`;

export const paymentMethodService = {
  async getAllPaymentMethods(): Promise<PaymentMethod[]> {
    return apiService.get<PaymentMethod[]>(PAYMENT_METHOD_PREFIX);
  },

  async createPaymentMethod(request: CreatePaymentMethodRequest): Promise<PaymentMethod> {
    return apiService.post<PaymentMethod>(`${PAYMENT_METHOD_PREFIX}/create`, request);
  },

  async updatePaymentMethod(id: string, request: UpdatePaymentMethodRequest): Promise<PaymentMethod> {
    return apiService.put<PaymentMethod>(`${PAYMENT_METHOD_PREFIX}/${id}`, request);
  },

  async updatePaymentMethodServiceUrl(id: string, request: UpdatePaymentMethodServiceUrlRequest): Promise<PaymentMethod> {
    return apiService.put<PaymentMethod>(`${PAYMENT_METHOD_PREFIX}/${id}/service-url`, request);
  },
};

