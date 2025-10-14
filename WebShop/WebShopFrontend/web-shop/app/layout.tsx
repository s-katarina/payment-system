import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import ClientAuthRedirect from "./ClientAuthRedirect";
import NavBar from "@/components/NavBar";

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
          <ClientAuthRedirect>{children}</ClientAuthRedirect>
        </main>
      </body>
    </html>
  );
}
