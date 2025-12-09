'use client';

import { PackageResponseDTO, packageService } from '@/service/packagesService';
import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import Link from 'next/link';

export default function PackagesPage() {
  const [packages, setPackages] = useState<PackageResponseDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (error) {
      toast.error(error);
    }
  }, [error]);

  useEffect(() => {
    const fetchPackages = async () => {
        try {
            const res = await packageService.getAllPackages();
            setPackages(res);
            setLoading(false);
        } catch (err) {
            setError('Failed to fetch packages.');
            setLoading(false);
        }
    }
    fetchPackages();
  }, []);

  if (loading) {
    return (
      <div className='packages-page'>
        <div className="packages-loading">Loading packages...</div>
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
          alt="Telecom packages" 
          className="packages-hero-image"
        />
        <div className="packages-hero-overlay">
          <h1 className="packages-title">Choose Your Package</h1>
          <p className="packages-subtitle">Select from our telecom services: Mobile, Landline, Internet & Digital TV</p>
        </div>
      </div>
      
      <div className='page'>
        <h1 className='h1 page-title'>Packages Page</h1>
        <div className="packages-row">
          {packages.map(pkg => (
            <Link href={`/packages/${pkg.id}`} key={pkg.id} className="package-card-link">
              <div className="package-card">
                <div className="package-card-content">
                  <h2>{pkg.packageName}</h2>
                  <span>${pkg.price}</span>
                </div>
                <div className="package-card-overlay">
                  <span className="package-card-view-text">View</span>
                </div>
              </div>
            </Link>
          ))}
        </div>
      </div>
    </div>
  )
}