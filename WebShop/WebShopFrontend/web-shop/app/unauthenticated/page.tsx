'use client';
import Link from "next/link";

export default function UnauthenticatedPage() {
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
