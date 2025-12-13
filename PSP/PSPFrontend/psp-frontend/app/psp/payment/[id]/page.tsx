'use client';
import { useEffect, useState } from 'react';
import { useParams, useSearchParams } from 'next/navigation';
import { paymentMethodService, PaymentMethod } from '@/service/paymentMethodService';
import { merchantService } from '@/service/merchantService';
import toast from 'react-hot-toast';

export default function PaymentMethodSelectionPage() {
  const params = useParams();
  const searchParams = useSearchParams();
  const paymentId = params.id as string;
  const merchantId = searchParams.get('merchantId');
  
  const [paymentMethods, setPaymentMethods] = useState<PaymentMethod[]>([]);
  const [selectedPaymentMethodId, setSelectedPaymentMethodId] = useState<string>('');
  const [isLoading, setIsLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    loadPaymentMethods();
  }, [paymentId, merchantId]);

  const loadPaymentMethods = async () => {
    try {
      setIsLoading(true);
      
      // If we have merchantId, fetch payment methods for that merchant
      if (merchantId) {
        const methods = await merchantService.getPaymentMethodsForMerchant(merchantId);
        setPaymentMethods(methods);
      } else {
        // Otherwise, fetch all payment methods (fallback)
        // In the future, we'll fetch payment by ID to get merchant ID
        const methods = await paymentMethodService.getAllPaymentMethods();
        setPaymentMethods(methods);
      }
    } catch (err: any) {
      console.error('[PaymentMethodSelection] Error loading payment methods:', err);
      toast.error(err.message || 'Failed to load payment methods');
    } finally {
      setIsLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!selectedPaymentMethodId) {
      toast.error('Please select a payment method');
      return;
    }

    setIsSubmitting(true);
    
    try {
      const selectedMethod = paymentMethods.find(m => m.id === selectedPaymentMethodId);
      console.log('Selected payment method:', selectedMethod);
      toast.success('Payment method selected');
    } catch (err: any) {
      console.error('[PaymentMethodSelection] Error submitting:', err);
      toast.error(err.message || 'Failed to submit payment method selection');
    } finally {
      setIsSubmitting(false);
    }
  };

  if (isLoading) {
    return (
      <div className="page">
        <div style={{ textAlign: 'center', padding: '2rem' }}>Loading payment methods...</div>
      </div>
    );
  }

  return (
    <div className="page">
      <div style={{ maxWidth: '600px', margin: '0 auto' }}>
        <h1 className="page-title" style={{ marginBottom: '2rem' }}>
          Select Payment Method
        </h1>

        <div className="card">
          <form onSubmit={handleSubmit}>
            <div style={{ marginBottom: '1.5rem' }}>
              <label style={{ display: 'block', marginBottom: '0.75rem', fontWeight: 500, fontSize: '1rem' }}>
                Choose a payment method
              </label>
              
              {paymentMethods.length === 0 ? (
                <div style={{ 
                  padding: '1rem', 
                  background: '#f9fafb', 
                  border: '1px solid #e5e7eb', 
                  borderRadius: '6px',
                  color: '#6b7280'
                }}>
                  No payment methods available
                </div>
              ) : (
                <div style={{ 
                  display: 'flex', 
                  flexDirection: 'column', 
                  gap: '0.75rem' 
                }}>
                  {paymentMethods.map((method) => (
                    <label 
                      key={method.id} 
                      style={{ 
                        display: 'flex', 
                        alignItems: 'center', 
                        gap: '0.75rem', 
                        cursor: 'pointer',
                        padding: '1rem',
                        background: selectedPaymentMethodId === method.id ? '#eff6ff' : 'white',
                        border: selectedPaymentMethodId === method.id 
                          ? '2px solid #3b82f6' 
                          : '1px solid #e5e7eb',
                        borderRadius: '6px',
                        transition: 'all 0.2s'
                      }}
                      onMouseEnter={(e) => {
                        if (selectedPaymentMethodId !== method.id) {
                          e.currentTarget.style.backgroundColor = '#f9fafb';
                        }
                      }}
                      onMouseLeave={(e) => {
                        if (selectedPaymentMethodId !== method.id) {
                          e.currentTarget.style.backgroundColor = 'white';
                        }
                      }}
                    >
                      <input
                        type="radio"
                        name="paymentMethod"
                        value={method.id}
                        checked={selectedPaymentMethodId === method.id}
                        onChange={(e) => setSelectedPaymentMethodId(e.target.value)}
                        style={{ width: '20px', height: '20px', cursor: 'pointer' }}
                      />
                      <div style={{ flex: 1 }}>
                        <div style={{ fontWeight: 500, marginBottom: '0.25rem', fontSize: '1rem' }}>
                          {method.name}
                        </div>
                        <div style={{ fontSize: '0.875rem', color: '#6b7280' }}>
                          Service: {method.serviceName}
                        </div>
                      </div>
                    </label>
                  ))}
                </div>
              )}
            </div>

            <div style={{ display: 'flex', gap: '1rem', justifyContent: 'flex-end' }}>
              <button 
                type="submit" 
                disabled={!selectedPaymentMethodId || isSubmitting} 
                className="btn btn-primary"
                style={{ minWidth: '120px' }}
              >
                {isSubmitting ? 'Processing...' : 'Continue'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

