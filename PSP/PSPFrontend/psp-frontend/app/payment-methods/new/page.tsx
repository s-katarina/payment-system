'use client';
import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { paymentMethodService, CreatePaymentMethodRequest } from '@/service/paymentMethodService';
import toast from 'react-hot-toast';

export default function NewPaymentMethodPage() {
  const router = useRouter();
  const [name, setName] = useState('');
  const [serviceName, setServiceName] = useState('');
  const [serviceUrl, setServiceUrl] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!name.trim()) {
      toast.error('Payment method name is required');
      return;
    }

    if (!serviceName.trim()) {
      toast.error('Service name is required');
      return;
    }

    if (!serviceUrl.trim()) {
      toast.error('Service URL is required');
      return;
    }

    setIsLoading(true);
    try {
      const request: CreatePaymentMethodRequest = {
        name: name.trim(),
        serviceName: serviceName.trim(),
        serviceUrl: serviceUrl.trim(),
      };
      await paymentMethodService.createPaymentMethod(request);
      toast.success('Payment method created successfully');
      router.push('/payment-methods');
    } catch (err: any) {
      toast.error(err.message || 'Failed to create payment method');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="page">
      <h1 className="page-title">Create New Payment Method</h1>
      <div className="card">
        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: '1.5rem' }}>
            <label htmlFor="name" style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 500 }}>
              Payment Method Name *
            </label>
            <input
              id="name"
              type="text"
              className="form-input"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Enter payment method name"
              required
            />
          </div>
          <div style={{ marginBottom: '1.5rem' }}>
            <label htmlFor="serviceName" style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 500 }}>
              Service Name *
            </label>
            <input
              id="serviceName"
              type="text"
              className="form-input"
              value={serviceName}
              onChange={(e) => setServiceName(e.target.value)}
              placeholder="Enter service name"
              required
            />
          </div>
          <div style={{ marginBottom: '1.5rem' }}>
            <label htmlFor="serviceUrl" style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 500 }}>
              Service URL *
            </label>
            <input
              id="serviceUrl"
              type="url"
              className="form-input"
              value={serviceUrl}
              onChange={(e) => setServiceUrl(e.target.value)}
              placeholder="https://example.com/payment"
              required
            />
          </div>
          <div style={{ display: 'flex', gap: '1rem' }}>
            <button type="submit" disabled={isLoading} className="btn btn-primary">
              {isLoading ? 'Creating...' : 'Create Payment Method'}
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

