'use client';
import { useEffect, useState } from 'react';
import Link from 'next/link';
import { paymentMethodService, PaymentMethod } from '@/service/paymentMethodService';
import toast from 'react-hot-toast';

export default function PaymentMethodsPage() {
  const [paymentMethods, setPaymentMethods] = useState<PaymentMethod[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadPaymentMethods();
  }, []);

  const loadPaymentMethods = async () => {
    try {
      setIsLoading(true);
      const data = await paymentMethodService.getAllPaymentMethods();
      setPaymentMethods(data);
      setError('');
    } catch (err: any) {
      setError(err.message || 'Failed to load payment methods');
      toast.error(err.message || 'Failed to load payment methods');
    } finally {
      setIsLoading(false);
    }
  };

  if (isLoading) {
    return (
      <div className="page">
        <div style={{ textAlign: 'center', padding: '2rem' }}>Loading payment methods...</div>
      </div>
    );
  }

  if (error && paymentMethods.length === 0) {
    return (
      <div className="page">
        <div style={{ textAlign: 'center', padding: '2rem', color: '#ef4444' }}>{error}</div>
      </div>
    );
  }

  return (
    <div className="page">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <h1 className="page-title">Payment Methods</h1>
        <Link href="/payment-methods/new" className="btn btn-primary">
          + New Payment Method
        </Link>
      </div>

      {paymentMethods.length === 0 ? (
        <div className="card">
          <p>No payment methods found. Create your first payment method to get started.</p>
        </div>
      ) : (
        <div className="card">
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Service URL</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {paymentMethods.map((method) => (
                <tr key={method.id}>
                  <td>{method.id}</td>
                  <td>{method.name}</td>
                  <td>{method.serviceUrl || '-'}</td>
                  <td>
                    <Link href={`/payment-methods/${method.id}`} className="btn btn-sm btn-secondary">
                      View
                    </Link>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

