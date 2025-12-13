"use client";
import { useAuth } from '@/app/AuthContext';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useState, useEffect, useRef } from 'react';

export default function NavBar() {
  const { isAuthenticated, user, logout } = useAuth();
  const router = useRouter();
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null);

  const handleLogout = () => {
    logout();
    router.push('/login');
    setIsDropdownOpen(false);
  };

  const toggleDropdown = () => {
    setIsDropdownOpen(!isDropdownOpen);
  };

  // Close dropdown when clicking outside
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setIsDropdownOpen(false);
      }
    };

    if (isDropdownOpen) {
      document.addEventListener('mousedown', handleClickOutside);
    }

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [isDropdownOpen]);

  if (!isAuthenticated) {
    return null;
  }

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <div className="navbar-brand">
          <Link href="/">PSP Admin</Link>
        </div>
        <div className="navbar-links">
          <Link href="/merchants" className="navbar-link">Merchants</Link>
          <Link href="/payment-methods" className="navbar-link">Payment Methods</Link>
        </div>
        <div className="navbar-user" ref={dropdownRef}>
          <button 
            className="navbar-username-button" 
            onClick={toggleDropdown}
            aria-expanded={isDropdownOpen}
            aria-haspopup="true"
          >
            {user?.name || 'Admin'}
          </button>
          {isDropdownOpen && (
            <div className="navbar-dropdown">
              <button 
                onClick={handleLogout} 
                className="navbar-dropdown-item navbar-dropdown-logout"
              >
                Logout
              </button>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
}

