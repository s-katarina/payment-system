'use client';
import { useEffect, useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { merchantService, Merchant, UpdateMerchantRequest } from '@/service/merchantService';
import { paymentMethodService, PaymentMethod } from '@/service/paymentMethodService';
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
  const [paymentMethods, setPaymentMethods] = useState<PaymentMethod[]>([]);
  const [selectedPaymentMethodIds, setSelectedPaymentMethodIds] = useState<string[]>([]);
  const [isLoadingPaymentMethods, setIsLoadingPaymentMethods] = useState(false);

  useEffect(() => {
    loadMerchant();
  }, [merchantId]);

  const loadMerchant = async () => {
    try {
      setIsLoading(true);
      const data = await merchantService.getMerchantById(merchantId);
      console.log('[MerchantDetail] Loaded merchant data:', data);
      console.log('[MerchantDetail] Payment methods in response:', data.paymentMethods);
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

  const loadPaymentMethods = async (): Promise<PaymentMethod[]> => {
    try {
      setIsLoadingPaymentMethods(true);
      const methods = await paymentMethodService.getAllPaymentMethods();
      console.log('[MerchantDetail] Loaded payment methods:', methods);
      setPaymentMethods(methods);
      return methods;
    } catch (err: any) {
      console.error('[MerchantDetail] Error loading payment methods:', err);
      toast.error(err.message || 'Failed to load payment methods');
      return [];
    } finally {
      setIsLoadingPaymentMethods(false);
    }
  };

  const handleSave = async () => {
    if (!merchantName.trim()) {
      toast.error('Merchant name is required');
      return;
    }

    if (selectedPaymentMethodIds.length === 0) {
      toast.error('At least one payment method is required');
      return;
    }

    setIsSaving(true);
    try {
      const request: UpdateMerchantRequest = {
        merchantName: merchantName.trim(),
        active: isActive,
        paymentMethodIds: selectedPaymentMethodIds,
      };
      console.log('[MerchantDetail] Saving merchant with request:', request);
      await merchantService.updateMerchant(merchantId, request);
      toast.success('Merchant updated successfully');
      setIsEditing(false);
      await loadMerchant();
    } catch (err: any) {
      console.error('[MerchantDetail] Error updating merchant:', err);
      toast.error(err.message || 'Failed to update merchant');
    } finally {
      setIsSaving(false);
    }
  };

  const handlePaymentMethodToggle = (methodId: string) => {
    setSelectedPaymentMethodIds(prev => {
      if (prev.includes(methodId)) {
        // Don't allow unchecking if it's the last one
        if (prev.length === 1) {
          toast.error('At least one payment method must be selected');
          return prev;
        }
        return prev.filter(id => id !== methodId);
      } else {
        return [...prev, methodId];
      }
    });
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

        <div style={{ marginBottom: '1.5rem' }}>
          <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 500 }}>
            Payment Methods {isEditing && <span style={{ color: '#ef4444' }}>*</span>}
          </label>
          {isEditing ? (
            isLoadingPaymentMethods ? (
              <div>Loading payment methods...</div>
            ) : paymentMethods.length === 0 ? (
              <div style={{ color: '#ef4444', marginTop: '0.5rem' }}>
                No payment methods available. Please create a payment method first.
              </div>
            ) : (
              <div style={{ 
                background: '#f9fafb', 
                border: '1px solid #e5e7eb', 
                borderRadius: '6px', 
                padding: '1rem' 
              }}>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
                  {paymentMethods.map((method) => (
                    <label 
                      key={method.id} 
                      style={{ 
                        display: 'flex', 
                        alignItems: 'center', 
                        gap: '0.75rem', 
                        cursor: 'pointer',
                        padding: '0.75rem',
                        background: 'white',
                        border: '1px solid #e5e7eb',
                        borderRadius: '4px',
                        transition: 'background-color 0.2s'
                      }}
                      onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#f9fafb'}
                      onMouseLeave={(e) => e.currentTarget.style.backgroundColor = 'white'}
                    >
                      <input
                        type="checkbox"
                        checked={selectedPaymentMethodIds.includes(method.id)}
                        onChange={() => handlePaymentMethodToggle(method.id)}
                        disabled={selectedPaymentMethodIds.length === 1 && selectedPaymentMethodIds.includes(method.id)}
                      />
                      <div style={{ flex: 1 }}>
                        <div style={{ fontWeight: 500, marginBottom: '0.25rem' }}>
                          {method.name}
                        </div>
                        <div style={{ fontSize: '0.875rem', color: '#6b7280' }}>
                          Service: {method.serviceName}
                        </div>
                      </div>
                    </label>
                  ))}
                </div>
                {selectedPaymentMethodIds.length === 0 && (
                  <div style={{ color: '#ef4444', marginTop: '0.5rem', fontSize: '0.875rem' }}>
                    At least one payment method must be selected
                  </div>
                )}
              </div>
            )
          ) : (
            merchant.paymentMethods && Array.isArray(merchant.paymentMethods) && merchant.paymentMethods.length > 0 ? (
              <div style={{ 
                background: '#f9fafb', 
                border: '1px solid #e5e7eb', 
                borderRadius: '6px', 
                padding: '1rem' 
              }}>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
                  {merchant.paymentMethods.map((pm) => (
                    <div 
                      key={pm.id} 
                      style={{ 
                        padding: '0.75rem', 
                        background: 'white', 
                        border: '1px solid #e5e7eb', 
                        borderRadius: '4px',
                        display: 'flex',
                        justifyContent: 'space-between',
                        alignItems: 'center'
                      }}
                    >
                      <div>
                        <div style={{ fontWeight: 500, marginBottom: '0.25rem' }}>
                          {pm.name}
                        </div>
                        <div style={{ fontSize: '0.875rem', color: '#6b7280' }}>
                          Service: {pm.serviceName}
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            ) : (
              <div style={{ 
                background: '#f9fafb', 
                border: '1px solid #e5e7eb', 
                borderRadius: '6px', 
                padding: '1rem',
                color: '#6b7280',
                fontStyle: 'italic'
              }}>
                No payment methods configured
              </div>
            )
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
                // Reset selected payment methods to current merchant's payment methods
                if (merchant.paymentMethods && merchant.paymentMethods.length > 0) {
                  setSelectedPaymentMethodIds(merchant.paymentMethods.map(pm => pm.id));
                } else {
                  setSelectedPaymentMethodIds([]);
                }
              }} className="btn btn-secondary">
                Cancel
              </button>
            </>
          ) : (
            <>
              <button onClick={async () => {
                // Load payment methods first, then enter edit mode
                setIsLoadingPaymentMethods(true);
                try {
                  const methods = await paymentMethodService.getAllPaymentMethods();
                  console.log('[MerchantDetail] Loaded payment methods:', methods);
                  setPaymentMethods(methods);
                  
                  // Now enter edit mode
                  setIsEditing(true);
                  
                  console.log('[MerchantDetail] Current merchant payment methods:', merchant.paymentMethods);
                  
                  // Initialize selected payment methods with current merchant's payment methods
                  if (merchant.paymentMethods && merchant.paymentMethods.length > 0) {
                    const currentIds = merchant.paymentMethods.map(pm => pm.id);
                    console.log('[MerchantDetail] Setting selected payment methods to:', currentIds);
                    setSelectedPaymentMethodIds(currentIds);
                  } else {
                    // If no payment methods, select the first available one
                    if (methods.length > 0) {
                      console.log('[MerchantDetail] No current payment methods, selecting first available:', methods[0].id);
                      setSelectedPaymentMethodIds([methods[0].id]);
                    } else {
                      // If no payment methods available at all, set empty array (will show error)
                      console.log('[MerchantDetail] No payment methods available');
                      setSelectedPaymentMethodIds([]);
                    }
                  }
                } catch (err: any) {
                  console.error('[MerchantDetail] Error loading payment methods:', err);
                  toast.error(err.message || 'Failed to load payment methods');
                } finally {
                  setIsLoadingPaymentMethods(false);
                }
              }} className="btn btn-primary">
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

