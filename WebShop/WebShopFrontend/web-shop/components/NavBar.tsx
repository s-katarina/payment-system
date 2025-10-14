"use client";
export default function NavBar() {
  return (
    <nav style={{ minWidth: 180, background: '#f5f5f5', height: '100vh', display: 'flex', flexDirection: 'column', alignItems: 'flex-start', padding: '2rem 1rem' }}>
      <button
        style={{ marginBottom: '1rem', padding: '0.5rem 1rem', borderRadius: 6, border: 'none', background: '#333', color: '#fff', cursor: 'pointer' }}
        onClick={() => window.location.href = '/home/page.tsx'}
      >
        Home
      </button>
    </nav>
  );
}
