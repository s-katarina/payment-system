import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import ClientAuthRedirect from "./ClientAuthRedirect";
import NavBar from "@/components/NavBar";
import { AuthProvider } from "./AuthContext";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "Wonderland Optics",
  description: "Webshop for Wonderland Optics",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  let isLoggedIn = false;
  if (typeof window !== "undefined") {
    isLoggedIn = !!localStorage.getItem("access_token");
  }
  return (
    <html lang="en">
      <body
        className={`${geistSans.variable} ${geistMono.variable} antialiased flex`}
      >
        {isLoggedIn ? <NavBar /> : null}
        <main style={{ flex: 1 }}>
          <AuthProvider>
            <ClientAuthRedirect>{children}</ClientAuthRedirect>
          </AuthProvider>
        </main>
      </body>
    </html>
  );
}
