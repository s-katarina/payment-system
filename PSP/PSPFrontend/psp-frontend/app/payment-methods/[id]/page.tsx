'use client';
import { useEffect, useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { paymentMethodService, PaymentMethod, UpdatePaymentMethodRequest, UpdatePaymentMethodServiceUrlRequest } from '@/service/paymentMethodService';
import toast from 'react-hot-toast';

export default function PaymentMethodDetailPage() {
  const params = useParams();
  const router = useRouter();
  const id = params.id as string;
  
  const [paymentMethod, setPaymentMethod] = useState<PaymentMethod | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isEditing, setIsEditing] = useState(false);
  const [name, setName] = useState('');
  const [serviceUrl, setServiceUrl] = useState('');
  const [isSaving, setIsSaving] = useState(false);

  useEffect(() => {
    loadPaymentMethod();
  }, [id]);

  const loadPaymentMethod = async () => {
    try {
      setIsLoading(true);
      // Since there's no getById endpoint, we'll fetch all and filter
      const allMethods = await paymentMethodService.getAllPaymentMethods();
      const method = allMethods.find(m => m.id === id);
      if (!method) {
        toast.error('Payment method not found');
        router.push('/payment-methods');
        return;
      }
      setPaymentMethod(method);
      setName(method.name);
      setServiceUrl(method.serviceUrl || '');
    } catch (err: any) {
      toast.error(err.message || 'Failed to load payment method');
      router.push('/payment-methods');
    } finally {
      setIsLoading(false);
    }
  };

  const handleSave = async () => {
    if (!name.trim()) {
      toast.error('Payment method name is required');
      return;
    }

    setIsSaving(true);
    try {
      const request: UpdatePaymentMethodRequest = {
        name: name.trim(),
      };
      await paymentMethodService.updatePaymentMethod(id, request);
      
      if (serviceUrl.trim()) {
        const urlRequest: UpdatePaymentMethodServiceUrlRequest = {
          serviceUrl: serviceUrl.trim(),
        };
        await paymentMethodService.updatePaymentMethodServiceUrl(id, urlRequest);
      }
      
      toast.success('Payment method updated successfully');
      setIsEditing(false);
      loadPaymentMethod();
    } catch (err: any) {
      toast.error(err.message || 'Failed to update payment method');
    } finally {
      setIsSaving(false);
    }
  };

  if (isLoading) {
    return (
      <div className="page">
        <div style={{ textAlign: 'center', padding: '2rem' }}>Loading payment method...</div>
      </div>
    );
  }

  if (!paymentMethod) {
    return null;
  }

  return (
    <div className="page">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <h1 className="page-title">Payment Method Details</h1>
        <button onClick={() => router.back()} className="btn btn-secondary">
          Back
        </button>
      </div>

      <div className="card">
        <div style={{ marginBottom: '1.5rem' }}>
          <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 500 }}>
            ID
          </label>
          <input
            type="text"
            className="form-input"
            value={paymentMethod.id}
            disabled
            style={{ background: '#f3f4f6', cursor: 'not-allowed' }}
          />
        </div>

        <div style={{ marginBottom: '1.5rem' }}>
          <label htmlFor="name" style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 500 }}>
            Payment Method Name
          </label>
          {isEditing ? (
            <input
              id="name"
              type="text"
              className="form-input"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
          ) : (
            <input
              type="text"
              className="form-input"
              value={paymentMethod.name}
              disabled
              style={{ background: '#f3f4f6', cursor: 'not-allowed' }}
            />
          )}
        </div>

        <div style={{ marginBottom: '1.5rem' }}>
          <label htmlFor="serviceUrl" style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 500 }}>
            Service URL
          </label>
          {isEditing ? (
            <input
              id="serviceUrl"
              type="url"
              className="form-input"
              value={serviceUrl}
              onChange={(e) => setServiceUrl(e.target.value)}
              placeholder="https://example.com/payment"
            />
          ) : (
            <input
              type="text"
              className="form-input"
              value={paymentMethod.serviceUrl || '-'}
              disabled
              style={{ background: '#f3f4f6', cursor: 'not-allowed' }}
            />
          )}
        </div>

        <div style={{ display: 'flex', gap: '1rem' }}>
          {isEditing ? (
            <>
              <button onClick={handleSave} disabled={isSaving} className="btn btn-primary">
                {isSaving ? 'Saving...' : 'Save'}
              </button>
              <button onClick={() => {
                setIsEditing(false);
                setName(paymentMethod.name);
                setServiceUrl(paymentMethod.serviceUrl || '');
              }} className="btn btn-secondary">
                Cancel
              </button>
            </>
          ) : (
            <button onClick={() => setIsEditing(true)} className="btn btn-primary">
              Edit
            </button>
          )}
        </div>
      </div>
    </div>
  );
}

