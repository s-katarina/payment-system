'use client';
import { useEffect, useState } from 'react';
import Link from 'next/link';
import { merchantService, Merchant } from '@/service/merchantService';
import toast from 'react-hot-toast';

export default function MerchantsPage() {
  const [merchants, setMerchants] = useState<Merchant[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadMerchants();
  }, []);

  const loadMerchants = async () => {
    try {
      setIsLoading(true);
      const data = await merchantService.getAllMerchants();
      setMerchants(data);
      setError('');
    } catch (err: any) {
      setError(err.message || 'Failed to load merchants');
      toast.error(err.message || 'Failed to load merchants');
    } finally {
      setIsLoading(false);
    }
  };


  if (isLoading) {
    return (
      <div className="page">
        <div style={{ textAlign: 'center', padding: '2rem' }}>Loading merchants...</div>
      </div>
    );
  }

  if (error && merchants.length === 0) {
    return (
      <div className="page">
        <div style={{ textAlign: 'center', padding: '2rem', color: '#ef4444' }}>{error}</div>
      </div>
    );
  }

  return (
    <div className="page">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <h1 className="page-title">Merchants</h1>
        <Link href="/merchants/new" className="btn btn-primary">
          + New Merchant
        </Link>
      </div>

      {merchants.length === 0 ? (
        <div className="card">
          <p>No merchants found. Create your first merchant to get started.</p>
        </div>
      ) : (
        <div className="card">
          <table className="data-table">
            <thead>
              <tr>
                <th>Merchant ID</th>
                <th>Merchant Name</th>
                <th>Active</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {merchants.map((merchant) => (
                <tr key={merchant.merchantId}>
                  <td>{merchant.merchantId}</td>
                  <td>{merchant.merchantName}</td>
                  <td>{merchant.active ? 'Yes' : 'No'}</td>
                  <td>
                    <Link href={`/merchants/${merchant.merchantId}`} className="btn btn-sm btn-secondary">
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

