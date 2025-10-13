import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import { usePathname, useRouter } from "next/navigation";
import { useEffect } from "react";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "Wonderland Beverages",
  description: "Webshop for Wonderland Beverages",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const router = useRouter();
  const pathname = typeof window !== "undefined" ? window.location.pathname : "";
  let isLoggedIn = false;
  if (typeof window !== "undefined") {
    isLoggedIn = !!localStorage.getItem("access_token");
  }

  useEffect(() => {
    if (!isLoggedIn && pathname !== "/login" && pathname !== "/register") {
      router.replace("/login");
    }
  }, [isLoggedIn, pathname, router]);

  return (
    <html lang="en">
      <body
        className={`${geistSans.variable} ${geistMono.variable} antialiased flex`}
      >
        {isLoggedIn ? (
          <nav style={{ minWidth: 180, background: '#f5f5f5', height: '100vh', display: 'flex', flexDirection: 'column', alignItems: 'flex-start', padding: '2rem 1rem' }}>
            <button
              style={{ marginBottom: '1rem', padding: '0.5rem 1rem', borderRadius: 6, border: 'none', background: '#333', color: '#fff', cursor: 'pointer' }}
              onClick={() => window.location.href = '/home/page.tsx'}
            >
              Home
            </button>
          </nav>
        ) : null}
        <main style={{ flex: 1 }}>{children}</main>
      </body>
    </html>
  );
}
