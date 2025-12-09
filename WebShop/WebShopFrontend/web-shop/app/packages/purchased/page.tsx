'use client';

import { PurchasedPackageDTO, purchaseService } from '@/service/purchaseService';
import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import Link from 'next/link';

export default function PurchasedPackagesPage() {
  const [purchasedPackages, setPurchasedPackages] = useState<PurchasedPackageDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (error) {
      toast.error(error);
    }
  }, [error]);

  useEffect(() => {
    const fetchPurchasedPackages = async () => {
      try {
        const res = await purchaseService.getPurchasedPackages();
        setPurchasedPackages(res);
        setLoading(false);
      } catch (err) {
        setError('Failed to fetch purchased packages.');
        setLoading(false);
      }
    };
    fetchPurchasedPackages();
  }, []);

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', { 
      year: 'numeric', 
      month: 'long', 
      day: 'numeric' 
    });
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'SUCCESS':
        return '#10b981'; // green
      case 'FAILURE':
      case 'ERROR':
        return '#ef4444'; // red
      case 'INITIATED':
        return '#f59e0b'; // amber
      default:
        return '#6b7280'; // gray
    }
  };

  if (loading) {
    return (
      <div className='packages-page'>
        <div className="packages-loading">Loading your packages...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className='packages-page'>
        <div className="packages-error">Error: {error}</div>
      </div>
    );
  }

  return (
    <div className='packages-page'>
      <div className="packages-hero-section">
        <img 
          src="https://images.unsplash.com/photo-1521106047354-5a5b85e819ee?q=80&w=1173&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D" 
          alt="Your packages" 
          className="packages-hero-image"
        />
        <div className="packages-hero-overlay">
          <h1 className="packages-title">Your Packages</h1>
          <p className="packages-subtitle">View all your purchased internet packages</p>
        </div>
      </div>
      
      <div className='page'>
        <h1 className='h1 page-title'>My Purchased Packages</h1>
        {purchasedPackages.length === 0 ? (
          <div className="packages-empty">
            <p>You haven't purchased any packages yet.</p>
            <Link href="/packages">
              <button className="home-cta-button">Browse Packages</button>
            </Link>
          </div>
        ) : (
          <div className="packages-row">
            {purchasedPackages.map(pkg => (
              <Link href={`/packages/${pkg.packageId}`} key={pkg.purchaseId} className="package-card-link">
                <div className="package-card">
                  <div className="package-card-content">
                    <h2>{pkg.packageName}</h2>
                    <span>${pkg.price}</span>
                    <div className="purchased-package-info">
                      <p className="purchased-date">Purchased: {formatDate(pkg.purchaseDate)}</p>
                      <span 
                        className="purchased-status" 
                        style={{ color: getStatusColor(pkg.status) }}
                      >
                        {pkg.status}
                      </span>
                    </div>
                  </div>
                  <div className="package-card-overlay">
                    <span className="package-card-view-text">View</span>
                  </div>
                </div>
              </Link>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

