import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import type { UserSummaryDto } from '@/types';
import { fetchCurrentUser, loginUser, logoutUser } from '@/services/auth';

interface AuthContextType {
  user: UserSummaryDto | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (email: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
  updateUserContext: (user: UserSummaryDto) => void;
}

const AuthContext = createContext<AuthContextType>({} as AuthContextType);

export const useAuth = () => useContext(AuthContext);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<UserSummaryDto | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('authToken');
    if (token) {
      fetchCurrentUser()
        .then(setUser)
        .catch(() => localStorage.removeItem('authToken'))
        .finally(() => setIsLoading(false));
    } else {
      setIsLoading(false);
    }
  }, []);

  const login = useCallback(async (email: string, password: string) => {
    const response = await loginUser({ email, password });
    localStorage.setItem('authToken', response.accessToken);
    setUser(response.user);
  }, []);

  const logout = useCallback(async () => {
    try { await logoutUser(); } catch {}
    localStorage.removeItem('authToken');
    setUser(null);
  }, []);

  const updateUserContext = useCallback((u: UserSummaryDto) => setUser(u), []);

  return (
    <AuthContext.Provider value={{ user, isAuthenticated: !!user, isLoading, login, logout, updateUserContext }}>
      {children}
    </AuthContext.Provider>
  );
};
