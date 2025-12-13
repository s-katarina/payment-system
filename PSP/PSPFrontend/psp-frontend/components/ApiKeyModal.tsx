'use client';
import { useState } from 'react';
import toast from 'react-hot-toast';

interface ApiKeyModalProps {
  title: string;
  merchantId: string;
  apiKey: string;
  warningMessage: string;
  descriptionMessage: string;
  continueButtonText: string;
  onContinue: () => void;
}

export default function ApiKeyModal({
  title,
  merchantId,
  apiKey,
  warningMessage,
  descriptionMessage,
  continueButtonText,
  onContinue,
}: ApiKeyModalProps) {
  const [copied, setCopied] = useState(false);

  const handleCopy = () => {
    navigator.clipboard.writeText(apiKey);
    setCopied(true);
    toast.success('Copied to clipboard!');
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="page">
      <div className="card" style={{ maxWidth: '600px', margin: '2rem auto' }}>
        <h2 className="page-title" style={{ color: '#10b981', marginBottom: '1rem' }}>
          {title}
        </h2>
        <div style={{ marginBottom: '1.5rem' }}>
          <p style={{ marginBottom: '0.5rem', fontWeight: 500 }}>Merchant ID:</p>
          <p style={{ fontFamily: 'monospace', background: '#f3f4f6', padding: '0.5rem', borderRadius: '4px' }}>
            {merchantId}
          </p>
        </div>
        <div style={{ marginBottom: '1.5rem' }}>
          <p style={{ marginBottom: '0.5rem', fontWeight: 600}}>
            ⚠️ {warningMessage}
          </p>
          <p style={{ marginBottom: '0.5rem', fontSize: '0.9rem', color: '#6b7280' }}>
            {descriptionMessage}
          </p>
          <div style={{ 
            display: 'flex', 
            gap: '0.5rem', 
            alignItems: 'center',
            background: '#fef3c7',
            padding: '1rem',
            borderRadius: '8px',
            border: '1px solid #fbbf24'
          }}>
            <input
              type="text"
              readOnly
              value={apiKey}
              style={{
                flex: 1,
                padding: '0.75rem',
                fontFamily: 'monospace',
                fontSize: '0.9rem',
                border: '1px solid #d1d5db',
                borderRadius: '4px',
                background: 'white'
              }}
            />
            <button
              type="button"
              onClick={handleCopy}
              className="btn btn-secondary"
              style={{ 
                whiteSpace: 'nowrap',
                background: copied ? '#10b981' : undefined,
                color: copied ? 'white' : undefined,
                transition: 'all 0.2s ease'
              }}
            >
              {copied ? '✓ Copied!' : 'Copy'}
            </button>
          </div>
        </div>
        <div style={{ display: 'flex', gap: '1rem', justifyContent: 'flex-end' }}>
          <button
            type="button"
            onClick={onContinue}
            className="btn btn-primary"
          >
            {continueButtonText}
          </button>
        </div>
      </div>
    </div>
  );
}

