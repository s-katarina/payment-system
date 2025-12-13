"use client";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

export default function ClientAuthRedirect({ children }: { children: React.ReactNode }) {
  const router = useRouter();
  const pathname = typeof window !== "undefined" ? window.location.pathname : "";
  let isLoggedIn = false;
  if (typeof window !== "undefined") {
    isLoggedIn = !!localStorage.getItem("access_token");
  }

  useEffect(() => {
    if (!isLoggedIn && pathname !== "/login" && pathname !== "/") {
      router.replace("/login");
    }
  }, [isLoggedIn, pathname, router]);

  return <>{children}</>;
}

