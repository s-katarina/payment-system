'use client';

import { PackageResponseDTO, packageService } from '@/service/packagesService';
import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';

export default function PackagesPage() {
  const [packages, setPackages] = useState<PackageResponseDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    toast.error(error);
  }, [error]);

  useEffect(() => {
    const fetchPackages = async () => {
        try {
            const res = await packageService.getAllPackages();
            setPackages(res);
            setLoading(false);
        } catch (err) {
            setError('Failed to fetch packages.');
        }
    }
    fetchPackages();
  }, []);

  if (loading) return <div className='page'>Loading packages...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <div className='page'>
      <h1 className='h1 page-title'>Packages Page</h1>
      <div className="packages-row">
        {packages.map(pkg => (
          <div className="package-card" key={pkg.id}>
            <h2>{pkg.packageName}</h2>
            <span>${pkg.price}</span>
          </div>
        ))}
      </div>
    </div>
  )
}