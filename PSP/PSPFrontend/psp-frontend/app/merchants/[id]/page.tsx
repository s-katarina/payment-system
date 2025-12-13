'use client';
import { useEffect, useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { merchantService, Merchant, UpdateMerchantRequest } from '@/service/merchantService';
import toast from 'react-hot-toast';
import ApiKeyModal from '@/components/ApiKeyModal';

export default function MerchantDetailPage() {
  const params = useParams();
  const router = useRouter();
  const merchantId = params.id as string;
  
  const [merchant, setMerchant] = useState<Merchant | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isEditing, setIsEditing] = useState(false);
  const [merchantName, setMerchantName] = useState('');
  const [isActive, setIsActive] = useState(true);
  const [isSaving, setIsSaving] = useState(false);
  const [isGeneratingPassword, setIsGeneratingPassword] = useState(false);
  const [newPassword, setNewPassword] = useState<string | null>(null);
  const [showPasswordModal, setShowPasswordModal] = useState(false);

  useEffect(() => {
    loadMerchant();
  }, [merchantId]);

  const loadMerchant = async () => {
    try {
      setIsLoading(true);
      const data = await merchantService.getMerchantById(merchantId);
      setMerchant(data);
      setMerchantName(data.merchantName);
      setIsActive(data.active ?? true);
    } catch (err: any) {
      toast.error(err.message || 'Failed to load merchant');
      router.push('/merchants');
    } finally {
      setIsLoading(false);
    }
  };

  const handleSave = async () => {
    if (!merchantName.trim()) {
      toast.error('Merchant name is required');
      return;
    }

    setIsSaving(true);
    try {
      const request: UpdateMerchantRequest = {
        merchantName: merchantName.trim(),
        active: isActive,
      };
      await merchantService.updateMerchant(merchantId, request);
      toast.success('Merchant updated successfully');
      setIsEditing(false);
      loadMerchant();
    } catch (err: any) {
      toast.error(err.message || 'Failed to update merchant');
    } finally {
      setIsSaving(false);
    }
  };

  const handleGeneratePassword = async () => {
    if (!confirm('This will generate a new password for this merchant. The old password will no longer work. Continue?')) {
      return;
    }

    setIsGeneratingPassword(true);
    try {
      const response = await merchantService.generatePassword(merchantId);
      setNewPassword(response.newPassword);
      setShowPasswordModal(true);
      toast.success('New password generated!');
    } catch (err: any) {
      toast.error(err.message || 'Failed to generate password');
    } finally {
      setIsGeneratingPassword(false);
    }
  };

  const handleClosePasswordModal = () => {
    setShowPasswordModal(false);
    setNewPassword(null);
  };

  if (isLoading) {
    return (
      <div className="page">
        <div style={{ textAlign: 'center', padding: '2rem' }}>Loading merchant...</div>
      </div>
    );
  }

  if (!merchant) {
    return null;
  }

  // Show password modal if password was just generated
  if (showPasswordModal && newPassword) {
    return (
      <ApiKeyModal
        title="New API Key Generated"
        merchantId={merchantId}
        apiKey={newPassword}
        warningMessage="Important: Save this new API key now"
        descriptionMessage="The old API key has been invalidated. This is the only time you will see this new API key. Make sure to copy and save it securely."
        continueButtonText="Continue to Merchant Details"
        onContinue={handleClosePasswordModal}
      />
    );
  }

  return (
    <div className="page">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <h1 className="page-title">Merchant Details</h1>
        <button onClick={() => router.back()} className="btn btn-secondary">
          Back
        </button>
      </div>

      <div className="card">
        <div style={{ marginBottom: '1.5rem' }}>
          <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 500 }}>
            Merchant ID
          </label>
          <input
            type="text"
            className="form-input"
            value={merchant.merchantId}
            disabled
            style={{ background: '#f3f4f6', cursor: 'not-allowed' }}
          />
        </div>

        <div style={{ marginBottom: '1.5rem' }}>
          <label htmlFor="merchantName" style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 500 }}>
            Merchant Name
          </label>
          {isEditing ? (
            <input
              id="merchantName"
              type="text"
              className="form-input"
              value={merchantName}
              onChange={(e) => setMerchantName(e.target.value)}
            />
          ) : (
            <input
              type="text"
              className="form-input"
              value={merchant.merchantName}
              disabled
              style={{ background: '#f3f4f6', cursor: 'not-allowed' }}
            />
          )}
        </div>

        <div style={{ marginBottom: '1.5rem' }}>
          <label htmlFor="active" style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 500 }}>
            Status
          </label>
          {isEditing ? (
            <select
              id="active"
              className="form-input"
              value={isActive ? 'true' : 'false'}
              onChange={(e) => setIsActive(e.target.value === 'true')}
            >
              <option value="true">Active</option>
              <option value="false">Inactive</option>
            </select>
          ) : (
            <input
              type="text"
              className="form-input"
              value={merchant.active ? 'Active' : 'Inactive'}
              disabled
              style={{ background: '#f3f4f6', cursor: 'not-allowed' }}
            />
          )}
        </div>

        <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap' }}>
          {isEditing ? (
            <>
              <button onClick={handleSave} disabled={isSaving} className="btn btn-primary">
                {isSaving ? 'Saving...' : 'Save'}
              </button>
              <button onClick={() => {
                setIsEditing(false);
                setMerchantName(merchant.merchantName);
                setIsActive(merchant.active ?? true);
              }} className="btn btn-secondary">
                Cancel
              </button>
            </>
          ) : (
            <>
              <button onClick={() => setIsEditing(true)} className="btn btn-primary">
                Edit
              </button>
              <button 
                onClick={handleGeneratePassword} 
                disabled={isGeneratingPassword}
                className="btn btn-secondary"
              >
                {isGeneratingPassword ? 'Generating...' : 'Generate New Password'}
              </button>
            </>
          )}
        </div>
      </div>
    </div>
  );
}

