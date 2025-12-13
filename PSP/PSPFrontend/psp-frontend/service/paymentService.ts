import { apiService } from "./api";
import { API_PREFIX } from "./apiConstants";

const PAYMENT_PREFIX = `${API_PREFIX}/payment`;

export const paymentService = {
  async initiatePayment(paymentId: string, merchantId: string): Promise<void> {
    return apiService.post<void>(
      `${PAYMENT_PREFIX}/initiate?paymentId=${paymentId}`,
      undefined,
      {
        headers: {
          'X-Merchant-Id': merchantId,
        },
      }
    );
  },
};

