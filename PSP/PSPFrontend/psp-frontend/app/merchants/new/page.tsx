'use client';
import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { merchantService, CreateMerchantRequest } from '@/service/merchantService';
import { paymentMethodService, PaymentMethod } from '@/service/paymentMethodService';
import toast from 'react-hot-toast';
import ApiKeyModal from '@/components/ApiKeyModal';

export default function NewMerchantPage() {
  const router = useRouter();
  const [merchantName, setMerchantName] = useState('');
  const [currency, setCurrency] = useState('USD');
  const [successUrl, setSuccessUrl] = useState('http://localhost:3000/payment/success');
  const [failUrl, setFailUrl] = useState('http://localhost:3000/payment/fail');
  const [errorUrl, setErrorUrl] = useState('http://localhost:3000/payment/error');
  const [paymentMethods, setPaymentMethods] = useState<PaymentMethod[]>([]);
  const [selectedPaymentMethodIds, setSelectedPaymentMethodIds] = useState<string[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isLoadingPaymentMethods, setIsLoadingPaymentMethods] = useState(true);
  const [createdMerchantApiKey, setCreatedMerchantApiKey] = useState<string | null>(null);
  const [createdMerchantId, setCreatedMerchantId] = useState<string | null>(null);

  useEffect(() => {
    loadPaymentMethods();
  }, []);

  const loadPaymentMethods = async () => {
    try {
      setIsLoadingPaymentMethods(true);
      const methods = await paymentMethodService.getAllPaymentMethods();
      setPaymentMethods(methods);
      if (methods.length > 0) {
        setSelectedPaymentMethodIds([methods[0].id]);
      }
    } catch (err: any) {
      toast.error(err.message || 'Failed to load payment methods');
    } finally {
      setIsLoadingPaymentMethods(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!merchantName.trim()) {
      toast.error('Merchant name is required');
      return;
    }

    if (!currency.trim()) {
      toast.error('Currency is required');
      return;
    }

    if (!successUrl.trim() || !failUrl.trim() || !errorUrl.trim()) {
      toast.error('All URLs are required');
      return;
    }

    if (selectedPaymentMethodIds.length === 0) {
      toast.error('At least one payment method is required');
      return;
    }

    setIsLoading(true);
    try {
      const request: CreateMerchantRequest = {
        merchantName: merchantName.trim(),
        currency: currency.trim(),
        successUrl: successUrl.trim(),
        failUrl: failUrl.trim(),
        errorUrl: errorUrl.trim(),
        paymentMethodIds: selectedPaymentMethodIds,
      };
      const response = await merchantService.createMerchant(request);
      // Store the API key to display it prominently (only shown once on creation)
      setCreatedMerchantApiKey(response.merchantPassword);
      setCreatedMerchantId(response.merchantId);
      toast.success('Merchant created successfully!');
    } catch (err: any) {
      toast.error(err.message || 'Failed to create merchant');
    } finally {
      setIsLoading(false);
    }
  };

  const handlePaymentMethodToggle = (methodId: string) => {
    setSelectedPaymentMethodIds(prev => {
      if (prev.includes(methodId)) {
        return prev.filter(id => id !== methodId);
      } else {
        return [...prev, methodId];
      }
    });
  };

  const handleContinueAfterApiKey = () => {
    const merchantIdToNavigate = createdMerchantId;
    setCreatedMerchantApiKey(null);
    setCreatedMerchantId(null);
    if (merchantIdToNavigate) {
      router.push(`/merchants/${merchantIdToNavigate}`);
    }
  };

  // Show API key modal if merchant was just created
  if (createdMerchantApiKey && createdMerchantId) {
    return (
      <ApiKeyModal
        title="Merchant Created Successfully"
        merchantId={createdMerchantId}
        apiKey={createdMerchantApiKey}
        warningMessage="Important: Save API Key now"
        descriptionMessage="This is the only time you will see this API key. Make sure to copy and save it securely."
        continueButtonText="Continue to Merchant Details"
        onContinue={handleContinueAfterApiKey}
      />
    );
  }

  return (
    <div className="page">
      <h1 className="page-title">Create New Merchant</h1>
      <div className="card">
        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: '1.5rem' }}>
            <label htmlFor="merchantName" style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 500 }}>
              Merchant Name *
            </label>
            <input
              id="merchantName"
              type="text"
              className="form-input"
              value={merchantName}
              onChange={(e) => setMerchantName(e.target.value)}
              placeholder="Enter merchant name"
              required
            />
          </div>

          <div style={{ marginBottom: '1.5rem' }}>
            <label htmlFor="currency" style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 500 }}>
              Currency *
            </label>
            <select
              id="currency"
              className="form-input"
              value={currency}
              onChange={(e) => setCurrency(e.target.value)}
              required
            >
              <option value="USD">USD</option>
              <option value="EUR">EUR</option>
              <option value="GBP">GBP</option>
              <option value="RSD">RSD</option>
            </select>
          </div>

          <div style={{ marginBottom: '1.5rem' }}>
            <label htmlFor="successUrl" style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 500 }}>
              Success URL *
            </label>
            <input
              id="successUrl"
              type="url"
              className="form-input"
              value={successUrl}
              onChange={(e) => setSuccessUrl(e.target.value)}
              placeholder="http://localhost:3000/payment/success"
              required
            />
          </div>

          <div style={{ marginBottom: '1.5rem' }}>
            <label htmlFor="failUrl" style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 500 }}>
              Fail URL *
            </label>
            <input
              id="failUrl"
              type="url"
              className="form-input"
              value={failUrl}
              onChange={(e) => setFailUrl(e.target.value)}
              placeholder="http://localhost:3000/payment/fail"
              required
            />
          </div>

          <div style={{ marginBottom: '1.5rem' }}>
            <label htmlFor="errorUrl" style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 500 }}>
              Error URL *
            </label>
            <input
              id="errorUrl"
              type="url"
              className="form-input"
              value={errorUrl}
              onChange={(e) => setErrorUrl(e.target.value)}
              placeholder="http://localhost:3000/payment/error"
              required
            />
          </div>

          <div style={{ marginBottom: '1.5rem' }}>
            <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 500 }}>
              Payment Methods * (Select at least one)
            </label>
            {isLoadingPaymentMethods ? (
              <div>Loading payment methods...</div>
            ) : paymentMethods.length === 0 ? (
              <div style={{ color: '#ef4444', marginTop: '0.5rem' }}>
                No payment methods available. Please create a payment method first.
              </div>
            ) : (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
                {paymentMethods.map((method) => (
                  <label key={method.id} style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', cursor: 'pointer' }}>
                    <input
                      type="checkbox"
                      checked={selectedPaymentMethodIds.includes(method.id)}
                      onChange={() => handlePaymentMethodToggle(method.id)}
                    />
                    <span>{method.name}</span>
                  </label>
                ))}
              </div>
            )}
          </div>

          <div style={{ display: 'flex', gap: '1rem' }}>
            <button type="submit" disabled={isLoading || paymentMethods.length === 0} className="btn btn-primary">
              {isLoading ? 'Creating...' : 'Create Merchant'}
            </button>
            <button
              type="button"
              onClick={() => router.back()}
              className="btn btn-secondary"
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

