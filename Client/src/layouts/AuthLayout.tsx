import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';

const AuthLayout = ({ children }: { children: React.ReactNode }) => (
  <div className="min-h-screen flex">
    {/* Left — Brand panel */}
    <div className="hidden lg:flex lg:w-[45%] relative bg-gradient-to-br from-primary to-accent overflow-hidden">
      <div className="absolute inset-0 grain opacity-20" />
      <div className="absolute top-1/4 -left-24 w-[400px] h-[400px] rounded-full bg-white/5 blur-[100px]" />
      <div className="absolute bottom-1/4 -right-24 w-[300px] h-[300px] rounded-full bg-white/5 blur-[80px]" />
      <div className="relative flex flex-col justify-between p-14 w-full">
        <Link to="/" className="font-display text-2xl font-bold text-white/90">
          alum<span className="text-white">.</span>
        </Link>
        <div>
          <h2 className="font-display text-4xl lg:text-5xl font-bold text-white leading-tight mb-5">
            Where Alumni<br />Stories Continue
          </h2>
          <p className="text-white/70 text-lg max-w-sm leading-relaxed">
            Connect with your college community, share your journey, and build relationships that last a lifetime.
          </p>
        </div>
        <p className="text-white/40 text-xs">© {new Date().getFullYear()} Alumni Association Platform</p>
      </div>
    </div>

    {/* Right — Form */}
    <div className="flex-1 flex items-center justify-center p-6 bg-background">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className="w-full"
      >
        {children}
      </motion.div>
    </div>
  </div>
);

export default AuthLayout;
