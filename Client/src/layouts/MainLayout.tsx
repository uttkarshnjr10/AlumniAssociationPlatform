import { useState } from 'react';
import { Link, useLocation, Outlet } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import { useAuth } from '@/contexts/AuthContext';
import UserAvatar from '@/components/common/UserAvatar';
import {
  Home, Newspaper, CalendarDays, Search, Menu, X, LogOut,
  User, Shield, Bell, Heart
} from 'lucide-react';

const navLinks = [
  { to: '/home', label: 'Home', icon: Home },
  { to: '/feed', label: 'Feed', icon: Newspaper },
  { to: '/events', label: 'Events', icon: CalendarDays },
  { to: '/search', label: 'Search', icon: Search },
];

const MainLayout = () => {
  const { user, logout } = useAuth();
  const location = useLocation();
  const [mobileOpen, setMobileOpen] = useState(false);
  const [userMenuOpen, setUserMenuOpen] = useState(false);

  return (
    <div className="min-h-screen bg-background flex flex-col">
      {/* Navbar */}
      <nav className="sticky top-0 z-50 bg-background/80 backdrop-blur-xl border-b border-border/50">
        <div className="max-w-6xl mx-auto px-6">
          <div className="flex items-center justify-between h-14">
            {/* Logo */}
            <Link to="/home" className="font-display text-xl font-bold text-foreground tracking-tight shrink-0">
              alum<span className="text-primary">.</span>
            </Link>

            {/* Desktop nav */}
            <div className="hidden md:flex items-center gap-1">
              {navLinks.map(({ to, label }) => {
                const active = location.pathname === to || (to !== '/home' && location.pathname.startsWith(to));
                return (
                  <Link
                    key={to}
                    to={to}
                    className={`relative px-4 py-2 text-sm font-medium transition-colors duration-200
                      ${active ? 'text-foreground' : 'text-muted-foreground hover:text-foreground'}`}
                  >
                    {label}
                    {active && (
                      <motion.div
                        layoutId="nav-indicator"
                        className="absolute bottom-0 left-2 right-2 h-0.5 bg-primary rounded-full"
                        transition={{ type: 'spring', stiffness: 380, damping: 30 }}
                      />
                    )}
                  </Link>
                );
              })}
            </div>

            {/* Right side */}
            <div className="flex items-center gap-2">
              <Link to="/donate" className="p-2 rounded-lg text-muted-foreground hover:text-foreground transition-colors" title="Donate">
                <Heart className="h-4.5 w-4.5" />
              </Link>
              <button className="relative p-2 rounded-lg text-muted-foreground hover:text-foreground transition-colors">
                <Bell className="h-4.5 w-4.5" />
                <span className="absolute top-1.5 right-1.5 h-1.5 w-1.5 rounded-full bg-destructive" />
              </button>

              {/* User menu */}
              <div className="relative">
                <button
                  onClick={() => setUserMenuOpen(!userMenuOpen)}
                  className="flex items-center gap-2 p-1 rounded-lg hover:bg-muted transition-colors"
                >
                  <UserAvatar name={user?.name || ''} profilePictureUrl={user?.profilePictureUrl} size="sm" />
                </button>

                <AnimatePresence>
                  {userMenuOpen && (
                    <>
                      <div className="fixed inset-0 z-40" onClick={() => setUserMenuOpen(false)} />
                      <motion.div
                        initial={{ opacity: 0, y: 8, scale: 0.96 }}
                        animate={{ opacity: 1, y: 0, scale: 1 }}
                        exit={{ opacity: 0, y: 8, scale: 0.96 }}
                        className="absolute right-0 top-full mt-2 w-56 bg-card rounded-xl shadow-[0_8px_30px_rgba(0,0,0,0.08)] border border-border p-1.5 z-50"
                      >
                        <div className="px-3 py-2.5 mb-1">
                          <p className="text-sm font-semibold text-foreground">{user?.name}</p>
                          <p className="text-xs text-muted-foreground">{user?.email}</p>
                        </div>
                        <div className="h-px bg-border mb-1" />
                        <Link to="/profile" onClick={() => setUserMenuOpen(false)} className="flex items-center gap-2.5 px-3 py-2 text-sm rounded-lg hover:bg-muted transition-colors text-foreground">
                          <User className="h-4 w-4 text-muted-foreground" /> Profile
                        </Link>
                        <Link to="/donate" onClick={() => setUserMenuOpen(false)} className="flex items-center gap-2.5 px-3 py-2 text-sm rounded-lg hover:bg-muted transition-colors text-foreground">
                          <Heart className="h-4 w-4 text-muted-foreground" /> Donate
                        </Link>
                        {user?.role === 'admin' && (
                          <Link to="/admin/dashboard" onClick={() => setUserMenuOpen(false)} className="flex items-center gap-2.5 px-3 py-2 text-sm rounded-lg hover:bg-muted transition-colors text-foreground">
                            <Shield className="h-4 w-4 text-muted-foreground" /> Admin
                          </Link>
                        )}
                        <div className="h-px bg-border my-1" />
                        <button
                          onClick={() => { setUserMenuOpen(false); logout(); }}
                          className="w-full flex items-center gap-2.5 px-3 py-2 text-sm rounded-lg hover:bg-destructive/10 text-destructive transition-colors"
                        >
                          <LogOut className="h-4 w-4" /> Sign Out
                        </button>
                      </motion.div>
                    </>
                  )}
                </AnimatePresence>
              </div>

              {/* Mobile hamburger */}
              <button
                onClick={() => setMobileOpen(!mobileOpen)}
                className="md:hidden p-2 rounded-lg text-muted-foreground hover:text-foreground transition-colors"
              >
                {mobileOpen ? <X className="h-5 w-5" /> : <Menu className="h-5 w-5" />}
              </button>
            </div>
          </div>
        </div>

        {/* Mobile nav */}
        <AnimatePresence>
          {mobileOpen && (
            <motion.div
              initial={{ height: 0, opacity: 0 }}
              animate={{ height: 'auto', opacity: 1 }}
              exit={{ height: 0, opacity: 0 }}
              className="md:hidden border-t border-border overflow-hidden"
            >
              <div className="px-4 py-3 space-y-1">
                {navLinks.map(({ to, label, icon: Icon }) => {
                  const active = location.pathname === to;
                  return (
                    <Link
                      key={to}
                      to={to}
                      onClick={() => setMobileOpen(false)}
                      className={`flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium transition-all
                        ${active ? 'bg-primary/8 text-primary' : 'text-muted-foreground hover:bg-muted'}`}
                    >
                      <Icon className="h-4.5 w-4.5" />
                      {label}
                    </Link>
                  );
                })}
              </div>
            </motion.div>
          )}
        </AnimatePresence>
      </nav>

      {/* Main content */}
      <main className="flex-1">
        <div className="max-w-6xl mx-auto px-6 py-8">
          <motion.div
            key={location.pathname}
            initial={{ opacity: 0, y: 12 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.3 }}
          >
            <Outlet />
          </motion.div>
        </div>
      </main>

      {/* Footer */}
      <footer className="border-t border-border mt-auto">
        <div className="max-w-6xl mx-auto px-6 py-6">
          <div className="flex flex-col sm:flex-row items-center justify-between gap-3">
            <span className="font-display text-lg font-bold text-foreground">alum<span className="text-primary">.</span></span>
            <p className="text-xs text-muted-foreground">© {new Date().getFullYear()} Alumni Association Platform</p>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default MainLayout;
