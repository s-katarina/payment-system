'use client';
import Link from "next/link";

export default function UnauthenticatedPage() {
  return (
    <div style={{ minHeight: "100vh", display: "flex", flexDirection: "column", justifyContent: "center", alignItems: "center" }}>
      <h2 style={{ marginBottom: "2rem" }}>Welcome to Wonderland Optics</h2>
      <div style={{ display: "flex", gap: "2rem" }}>
        <Link href="/login">
          <button style={{ padding: "1rem 2rem", borderRadius: 8, background: "#333", color: "#fff", border: "none", fontSize: "1rem", cursor: "pointer" }}>Login</button>
        </Link>
        <Link href="/register">
          <button style={{ padding: "1rem 2rem", borderRadius: 8, background: "#0070f3", color: "#fff", border: "none", fontSize: "1rem", cursor: "pointer" }}>Register</button>
        </Link>
      </div>
    </div>
  );
}
