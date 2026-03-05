import { useAuth } from '@/contexts/AuthContext';
import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import { MessageSquare, CalendarDays, Users, PenSquare, Search as SearchIcon } from 'lucide-react';

const stats = [
  { label: 'Feed Posts', icon: MessageSquare },
  { label: 'Events', icon: CalendarDays },
  { label: 'Network', icon: Users },
];

const HomePage = () => {
  const { user } = useAuth();

  return (
    <div className="space-y-8">
      {/* Hero */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="relative overflow-hidden rounded-2xl bg-gradient-to-br from-primary to-primary/80 p-8 md:p-12 text-primary-foreground"
      >
        <div className="absolute inset-0 grain opacity-20" />
        <div className="relative z-10">
          <h1 className="font-display text-3xl md:text-4xl font-bold tracking-tight mb-2">
            Welcome back, {user?.name?.split(' ')[0]} 👋
          </h1>
          <p className="text-primary-foreground/70 text-base md:text-lg mb-6 max-w-xl">
            Stay connected with your college community.
          </p>
          <div className="flex flex-wrap gap-3">
            <Link to="/feed" className="inline-flex items-center gap-2 px-5 py-2.5 bg-white/15 hover:bg-white/25 rounded-xl text-sm font-medium backdrop-blur-sm transition-all active:scale-95">
              <PenSquare className="h-4 w-4" /> Create Post
            </Link>
            <Link to="/events" className="inline-flex items-center gap-2 px-5 py-2.5 bg-white/15 hover:bg-white/25 rounded-xl text-sm font-medium backdrop-blur-sm transition-all active:scale-95">
              <CalendarDays className="h-4 w-4" /> Browse Events
            </Link>
            <Link to="/search" className="inline-flex items-center gap-2 px-5 py-2.5 bg-white/15 hover:bg-white/25 rounded-xl text-sm font-medium backdrop-blur-sm transition-all active:scale-95">
              <SearchIcon className="h-4 w-4" /> Find People
            </Link>
          </div>
        </div>
      </motion.div>

      {/* Stats */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        {stats.map(({ label, icon: Icon }, i) => (
          <motion.div
            key={label}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.1 * (i + 1) }}
            className="bg-card rounded-2xl p-6 shadow-[0_1px_3px_rgba(0,0,0,0.04)] hover:shadow-[0_4px_20px_rgba(0,0,0,0.06)] transition-all duration-300"
          >
            <Icon className="h-6 w-6 text-muted-foreground mb-3" />
            <p className="text-2xl font-bold text-foreground">—</p>
            <p className="text-sm text-muted-foreground">{label}</p>
          </motion.div>
        ))}
      </div>

      {/* Two columns */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2">
          <div className="flex items-center justify-between mb-4">
            <h2 className="font-display text-lg font-semibold text-foreground">Recent Activity</h2>
            <Link to="/feed" className="text-sm text-primary font-medium hover:text-primary/80">View All →</Link>
          </div>
          <div className="bg-card rounded-2xl shadow-[0_1px_3px_rgba(0,0,0,0.04)] p-8 text-center">
            <MessageSquare className="h-10 w-10 text-muted-foreground/30 mx-auto mb-3" />
            <p className="text-muted-foreground text-sm">Head to the Feed to see what's happening.</p>
            <Link to="/feed" className="inline-block mt-4 px-5 py-2 rounded-xl gradient-primary text-primary-foreground text-sm font-medium hover:opacity-90 transition-all">
              Go to Feed
            </Link>
          </div>
        </div>

        <div>
          <div className="flex items-center justify-between mb-4">
            <h2 className="font-display text-lg font-semibold text-foreground">Upcoming Events</h2>
            <Link to="/events" className="text-sm text-primary font-medium hover:text-primary/80">View All →</Link>
          </div>
          <div className="bg-card rounded-2xl shadow-[0_1px_3px_rgba(0,0,0,0.04)] p-6 text-center">
            <CalendarDays className="h-10 w-10 text-muted-foreground/30 mx-auto mb-3" />
            <p className="text-muted-foreground text-sm">No upcoming events.</p>
            <Link to="/events" className="inline-block mt-4 text-primary text-sm font-medium hover:text-primary/80">Browse Events →</Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default HomePage;
