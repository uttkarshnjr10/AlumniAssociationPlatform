import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import { useAuth } from '@/contexts/AuthContext';
import { Users, CalendarDays, MessageSquare, Shield } from 'lucide-react';

const cards = [
  { label: 'Manage Users', desc: 'Add, remove, and manage user accounts', icon: Users, to: '/admin/users' },
  { label: 'Manage Events', desc: 'Moderate and remove events', icon: CalendarDays, to: '/admin/events' },
  { label: 'View Feed', desc: 'Monitor community posts', icon: MessageSquare, to: '/feed' },
];

const AdminDashboardPage = () => {
  const { user } = useAuth();

  return (
    <div className="space-y-8">
      <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} className="bg-card rounded-2xl shadow-[0_1px_3px_rgba(0,0,0,0.04)] p-6 md:p-8">
        <div className="flex items-center gap-3 mb-2">
          <Shield className="h-5 w-5 text-warning" />
          <h1 className="font-display text-2xl font-bold text-foreground">Admin Dashboard</h1>
        </div>
        <p className="text-muted-foreground text-sm">Welcome, {user?.name}. Manage your college community from here.</p>
      </motion.div>

      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        {cards.map(({ label, desc, icon: Icon, to }, i) => (
          <motion.div key={label} initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.1 * (i + 1) }}>
            <Link to={to} className="block bg-card rounded-2xl shadow-[0_1px_3px_rgba(0,0,0,0.04)] p-6 hover:shadow-[0_8px_30px_rgba(0,0,0,0.06)] transition-all duration-300">
              <Icon className="h-6 w-6 text-muted-foreground mb-3" />
              <h3 className="font-semibold text-foreground mb-1">{label}</h3>
              <p className="text-sm text-muted-foreground">{desc}</p>
            </Link>
          </motion.div>
        ))}
      </div>
    </div>
  );
};

export default AdminDashboardPage;
