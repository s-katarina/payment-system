'use client';

import { useParams, useRouter } from 'next/navigation';
import { useEffect, useState } from 'react';
import { packageService, PackageResponseDTO } from '@/service/packagesService';
import { purchaseService } from '@/service/purchaseService';
import toast from 'react-hot-toast';

export default function PackageDetailPage() {
  const params = useParams();
  const router = useRouter();
  const packageId = params.id as string;
  
  const [packageData, setPackageData] = useState<PackageResponseDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [purchasing, setPurchasing] = useState(false);

  useEffect(() => {
    const fetchPackage = async () => {
      try {
        const packages = await packageService.getAllPackages();
        const pkg = packages.find(p => p.id === packageId);
        if (pkg) {
          setPackageData(pkg);
        } else {
          toast.error('Package not found');
          router.push('/packages');
        }
      } catch (err) {
        toast.error('Failed to load package');
        router.push('/packages');
      } finally {
        setLoading(false);
      }
    };

    if (packageId) {
      fetchPackage();
    }
  }, [packageId, router]);

  const handleBuy = async () => {
    if (!packageData) return;
    
    setPurchasing(true);
    try {
      const response = await purchaseService.buyPackage(packageData.id);
      if (response.redirectUrl) {
        // Redirect to payment page if redirect URL is provided
        window.location.href = response.redirectUrl;
      } else {
        toast.success('Purchase initiated successfully!');
      }
    } catch (error) {
      toast.error('Failed to purchase package. Please try again.');
    } finally {
      setPurchasing(false);
    }
  };

  if (loading) {
    return (
      <div className="package-detail-page">
        <div className="package-detail-loading">Loading package details...</div>
      </div>
    );
  }

  if (!packageData) {
    return null;
  }

  return (
    <div className="package-detail-page">
      <div className="package-detail-container">
        <button 
          onClick={() => router.back()} 
          className="package-detail-back-button"
        >
          ← Back to Packages
        </button>
        
        <div className="package-detail-card">
          <h1 className="package-detail-name">{packageData.packageName}</h1>
          <div className="package-detail-price-section">
            <span className="package-detail-currency">$</span>
            <span className="package-detail-amount">{packageData.price.toFixed(2)}</span>
            <span className="package-detail-period">/month</span>
          </div>
          
          <button 
            onClick={handleBuy}
            disabled={purchasing}
            className="package-detail-buy-button"
          >
            {purchasing ? 'Processing...' : 'Buy Package'}
          </button>
        </div>
      </div>
    </div>
  );
}

