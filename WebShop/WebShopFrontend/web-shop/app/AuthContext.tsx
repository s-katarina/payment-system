'use client';
import React, { createContext, useContext, useState, useEffect, ReactNode, useCallback } from 'react';

export type User = {
  username: string;
  name: string;
  id: string;
} | null;

interface AuthContextType {
  user: User;
  setUser: (user: User) => void;
  isAuthenticated: boolean;
  logout: () => void;
  getRedirectPath: () => string;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUserState] = useState<User>(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    const token = localStorage.getItem('access_token');
    if (storedUser && storedUser !== 'undefined' && token) {
      setUserState(JSON.parse(storedUser));
      setIsAuthenticated(true);
    } else {
      setUserState(null);
      setIsAuthenticated(false);
    }
  }, []);

  const setUser = useCallback((user: User) => {
    setUserState(user);
    setIsAuthenticated(!!user);
    if (user) {
      localStorage.setItem('user', JSON.stringify(user));
    }
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem('access_token');
    localStorage.removeItem('user');
    setUserState(null);
    setIsAuthenticated(false);
  }, []);

  const getRedirectPath = useCallback((): string => {
    return isAuthenticated ? '/packages' : '/unauthenticated';
  }, [isAuthenticated]);

  return (
    <AuthContext.Provider value={{ user, setUser, isAuthenticated, logout, getRedirectPath }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
