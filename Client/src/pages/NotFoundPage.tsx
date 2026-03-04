import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import { Home, Newspaper } from 'lucide-react';

const NotFoundPage = () => (
  <div className="min-h-screen flex items-center justify-center p-4 bg-background">
    <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} className="text-center max-w-md">
      <h1 className="font-display text-8xl font-black gradient-text mb-4">404</h1>
      <h2 className="font-display text-xl font-bold text-foreground mb-2">Page not found</h2>
      <p className="text-muted-foreground text-sm mb-8">The page you're looking for doesn't exist or has been moved.</p>
      <div className="flex gap-3 justify-center">
        <Link to="/" className="inline-flex items-center gap-2 px-5 py-2.5 rounded-xl gradient-primary text-primary-foreground text-sm font-medium hover:opacity-90 active:scale-95 transition-all">
          <Home className="h-4 w-4" /> Go Home
        </Link>
        <Link to="/feed" className="inline-flex items-center gap-2 px-5 py-2.5 rounded-xl border border-border text-sm font-medium text-foreground hover:bg-muted transition-colors">
          <Newspaper className="h-4 w-4" /> Go to Feed
        </Link>
      </div>
    </motion.div>
  </div>
);

export default NotFoundPage;
