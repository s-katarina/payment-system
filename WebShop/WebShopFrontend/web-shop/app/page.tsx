'use client';
import { useAuth } from './AuthContext';
import Link from 'next/link';

export default function RootPage() {
  const { isAuthenticated, user } = useAuth();

  // Show landing page for unauthenticated users
  if (!isAuthenticated) {
    return (
      <div className="landing-page">
        <div className="landing-hero">
          <div className="landing-content">
            <h1 className="landing-title">Welcome to Wonderland Optics</h1>
            <p className="landing-subtitle">Complete telecom solutions: Mobile, Landline, Internet & Digital TV</p>
            <div className="landing-buttons">
              <Link href="/login">
                <button className="landing-button landing-button-primary">Login</button>
              </Link>
              <Link href="/register">
                <button className="landing-button landing-button-secondary">Register</button>
              </Link>
            </div>
          </div>
        </div>
        <div className="landing-features">
          <div className="feature-card">
            <h3>📱 Mobile Services</h3>
            <p>Mobile phone services with nationwide coverage</p>
          </div>
          <div className="feature-card">
            <h3>☎️ Landline Services</h3>
            <p>Fixed phone services for reliable home communication</p>
          </div>
          <div className="feature-card">
            <h3>🌐 Internet & TV</h3>
            <p>High-speed internet and digital TV packages</p>
          </div>
        </div>
      </div>
    );
  }

  // Show home page for authenticated users
  return (
    <div className="home-page">
      <div className="home-hero">
        <div className="home-hero-content">
          <h1 className="home-title">Welcome back, {user?.name || 'User'}!</h1>
          <p className="home-subtitle">Explore our telecom services: Mobile, Landline, Internet & Digital TV</p>
          <Link href="/packages">
            <button className="home-cta-button">View Packages</button>
          </Link>
        </div>
        <div className="home-hero-image">
          <img 
            src="https://images.unsplash.com/photo-1610034534785-4706b86dd6a5?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D" 
            alt="Telecom services" 
            className="hero-img"
          />
        </div>
      </div>
      <div className="home-features">
        <div className="home-feature">
          <h3>📱 Mobile Services</h3>
          <p>Mobile phone services with flexible plans and nationwide coverage</p>
        </div>
        <div className="home-feature">
          <h3>☎️ Landline & Internet</h3>
          <p>Fixed phone services and high-speed internet for your home</p>
        </div>
        <div className="home-feature">
          <h3>📺 Digital TV</h3>
          <p>Digital TV with hundreds of channels and on-demand content</p>
        </div>
      </div>
    </div>
  );
}
