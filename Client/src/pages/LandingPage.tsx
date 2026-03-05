import { Link } from 'react-router-dom';
import { motion, useInView } from 'framer-motion';
import { useRef } from 'react';
import { MessageSquare, CalendarDays, Users, ArrowRight, Sparkles } from 'lucide-react';

const stats = [
  { value: '10,000+', label: 'Alumni Connected' },
  { value: '500+', label: 'Events Hosted' },
  { value: '50+', label: 'College Networks' },
];

const features = [
  { icon: MessageSquare, title: 'Social Feed', desc: 'Share updates, celebrate milestones, and stay connected with your college community through a rich social experience.' },
  { icon: CalendarDays, title: 'Events & Reunions', desc: 'Discover and organize alumni meetups, webinars, and campus events. Never miss a chance to reconnect.' },
  { icon: Users, title: 'Professional Network', desc: 'Find mentors, collaborators, and friends from your alma mater. Build relationships that last a lifetime.' },
];

const steps = [
  { num: '01', title: 'Register Your College', desc: 'Set up your institution\'s alumni network in minutes with a simple registration process.' },
  { num: '02', title: 'Build Your Profile', desc: 'Showcase your journey — your graduation year, department, achievements, and current endeavors.' },
  { num: '03', title: 'Connect & Engage', desc: 'Follow classmates, join events, share posts, and grow your professional network organically.' },
];

const testimonials = [
  { quote: 'This platform helped me reconnect with batchmates I had lost touch with for over a decade. The events feature is fantastic!', name: 'Priya Sharma', role: 'Class of 2012, Computer Science' },
  { quote: 'As an admin, managing our alumni network has never been easier. The dashboard gives me everything I need at a glance.', name: 'Rahul Menon', role: 'Alumni Relations Officer' },
  { quote: 'I found my co-founder through the networking features here. This platform is more than just nostalgia — it creates real opportunities.', name: 'Ananya Iyer', role: 'Class of 2018, MBA' },
];

const AnimatedSection = ({ children, className = '' }: { children: React.ReactNode; className?: string }) => {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true, margin: '-80px' });
  return (
    <motion.div
      ref={ref}
      initial={{ opacity: 0, y: 40 }}
      animate={isInView ? { opacity: 1, y: 0 } : {}}
      transition={{ duration: 0.6, ease: [0.22, 1, 0.36, 1] }}
      className={className}
    >
      {children}
    </motion.div>
  );
};

const LandingPage = () => (
  <div className="min-h-screen bg-background">
    {/* Navbar — minimal, single Sign In link */}
    <nav className="fixed top-0 left-0 right-0 z-50 bg-background/80 backdrop-blur-xl border-b border-border/50">
      <div className="max-w-6xl mx-auto px-6 flex items-center justify-between h-16">
        <Link to="/" className="font-display text-2xl font-bold text-foreground tracking-tight">
          alum<span className="text-primary">.</span>
        </Link>
        <Link to="/login" className="text-sm font-medium text-muted-foreground hover:text-foreground transition-colors">
          Sign In
        </Link>
      </div>
    </nav>

    {/* Hero — single primary CTA + subtle sign-in text link */}
    <section className="relative pt-40 pb-32 px-6 overflow-hidden">
      <div className="absolute inset-0 grain opacity-60" />
      <div className="absolute top-1/4 -left-32 w-[500px] h-[500px] rounded-full bg-primary/5 blur-[120px]" />
      <div className="absolute bottom-0 -right-32 w-[400px] h-[400px] rounded-full bg-accent/5 blur-[100px]" />
      <div className="relative max-w-4xl mx-auto text-center">
        <motion.div
          initial={{ opacity: 0, y: 30 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.7, ease: [0.22, 1, 0.36, 1] }}
        >
          <div className="inline-flex items-center gap-2 px-4 py-1.5 rounded-full bg-primary/8 border border-primary/15 text-primary text-sm font-medium mb-8">
            <Sparkles className="h-3.5 w-3.5" />
            The modern alumni platform
          </div>
          <h1 className="font-display text-5xl sm:text-6xl md:text-7xl font-bold text-foreground tracking-tight leading-[1.1] mb-6">
            Where Alumni<br />
            <span className="gradient-text">Stories Continue</span>
          </h1>
          <p className="text-lg md:text-xl text-muted-foreground max-w-2xl mx-auto mb-10 leading-relaxed">
            A beautifully crafted platform to keep your college community alive — share memories, attend events, and build connections that matter.
          </p>
          <Link to="/register/college" className="inline-flex items-center gap-2 px-8 py-4 rounded-xl gradient-primary text-primary-foreground font-medium hover:opacity-90 active:scale-[0.98] transition-all shadow-lg shadow-primary/20">
            Get Started <ArrowRight className="h-4 w-4" />
          </Link>
          <p className="mt-5 text-sm text-muted-foreground">
            Already have an account?{' '}
            <Link to="/login" className="text-primary hover:text-primary/80 transition-colors font-medium">Sign in</Link>
          </p>
        </motion.div>
      </div>
    </section>

    {/* Stats */}
    <AnimatedSection>
      <div className="max-w-4xl mx-auto px-6 py-16">
        <div className="grid grid-cols-3 gap-8">
          {stats.map(({ value, label }) => (
            <div key={label} className="text-center">
              <p className="font-display text-3xl md:text-4xl font-bold text-foreground mb-1">{value}</p>
              <p className="text-sm text-muted-foreground">{label}</p>
            </div>
          ))}
        </div>
      </div>
    </AnimatedSection>

    {/* Separator + Editorial Pull Quote */}
    <div className="max-w-5xl mx-auto px-6">
      <div className="h-px bg-border" />
    </div>
    <AnimatedSection className="py-20 px-6">
      <blockquote className="max-w-3xl mx-auto text-center">
        <p className="font-display text-2xl md:text-3xl italic text-foreground/80 leading-relaxed">
          "The connections we make in college shape our lives. This platform ensures those bonds never fade."
        </p>
      </blockquote>
    </AnimatedSection>
    <div className="max-w-5xl mx-auto px-6">
      <div className="h-px bg-border" />
    </div>

    {/* Features */}
    <section className="py-24 px-6">
      <div className="max-w-5xl mx-auto">
        <AnimatedSection className="text-center mb-16">
          <p className="text-sm font-medium text-primary mb-3 tracking-wide uppercase">Features</p>
          <h2 className="font-display text-3xl md:text-4xl font-bold text-foreground">Everything your network needs</h2>
        </AnimatedSection>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          {features.map(({ icon: Icon, title, desc }) => (
            <AnimatedSection key={title}>
              <motion.div
                whileHover={{ y: -4 }}
                transition={{ duration: 0.2 }}
                className="bg-card rounded-2xl p-8 shadow-[0_1px_3px_rgba(0,0,0,0.04)] hover:shadow-[0_8px_30px_rgba(0,0,0,0.06)] transition-shadow duration-300"
              >
                <div className="h-12 w-12 rounded-xl bg-primary/8 flex items-center justify-center mb-5">
                  <Icon className="h-6 w-6 text-primary" />
                </div>
                <h3 className="font-display text-xl font-semibold text-foreground mb-3">{title}</h3>
                <p className="text-muted-foreground text-sm leading-relaxed">{desc}</p>
              </motion.div>
            </AnimatedSection>
          ))}
        </div>
      </div>
    </section>

    {/* Separator */}
    <div className="max-w-5xl mx-auto px-6"><div className="h-px bg-border" /></div>

    {/* How It Works */}
    <section className="py-24 px-6">
      <div className="max-w-5xl mx-auto">
        <AnimatedSection className="text-center mb-16">
          <p className="text-sm font-medium text-primary mb-3 tracking-wide uppercase">How it works</p>
          <h2 className="font-display text-3xl md:text-4xl font-bold text-foreground">Get started in three steps</h2>
        </AnimatedSection>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-10">
          {steps.map(({ num, title, desc }) => (
            <AnimatedSection key={num}>
              <div className="relative">
                <span className="font-display text-6xl font-bold text-primary/10 block mb-4">{num}</span>
                <h3 className="font-display text-lg font-semibold text-foreground mb-2">{title}</h3>
                <p className="text-sm text-muted-foreground leading-relaxed">{desc}</p>
              </div>
            </AnimatedSection>
          ))}
        </div>
      </div>
    </section>

    {/* Separator */}
    <div className="max-w-5xl mx-auto px-6"><div className="h-px bg-border" /></div>

    {/* Testimonials — with decorative quotation marks */}
    <section className="py-24 px-6">
      <div className="max-w-5xl mx-auto">
        <AnimatedSection className="text-center mb-16">
          <p className="text-sm font-medium text-primary mb-3 tracking-wide uppercase">Testimonials</p>
          <h2 className="font-display text-3xl md:text-4xl font-bold text-foreground">Loved by communities</h2>
        </AnimatedSection>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          {testimonials.map(({ quote, name, role }) => (
            <AnimatedSection key={name}>
              <div className="bg-card rounded-2xl p-7 shadow-[0_1px_3px_rgba(0,0,0,0.04)] h-full flex flex-col relative">
                <span className="font-display text-5xl text-primary/10 absolute top-4 left-6 leading-none select-none">"</span>
                <p className="text-foreground text-sm leading-relaxed flex-1 mb-6 pt-6">{quote}</p>
                <div>
                  <p className="font-semibold text-sm text-foreground">{name}</p>
                  <p className="text-xs text-muted-foreground mt-0.5">{role}</p>
                </div>
              </div>
            </AnimatedSection>
          ))}
        </div>
      </div>
    </section>

    {/* CTA — specific action, not generic */}
    <AnimatedSection>
      <section className="py-24 px-6">
        <div className="max-w-3xl mx-auto text-center bg-gradient-to-br from-primary to-accent rounded-3xl p-12 md:p-16 relative overflow-hidden">
          <div className="absolute inset-0 grain opacity-30" />
          <div className="relative">
            <h2 className="font-display text-3xl md:text-4xl font-bold text-primary-foreground mb-4">Ready to reconnect?</h2>
            <p className="text-primary-foreground/80 mb-8 max-w-lg mx-auto">Join thousands of alumni who are already building meaningful connections through our platform.</p>
            <Link to="/register/college" className="inline-flex items-center gap-2 px-7 py-3.5 rounded-xl bg-white text-primary font-medium hover:bg-white/90 active:scale-[0.98] transition-all shadow-lg">
              Register Your College <ArrowRight className="h-4 w-4" />
            </Link>
            <p className="mt-4 text-sm text-primary-foreground/60">
              or{' '}
              <Link to="/login" className="text-primary-foreground/90 hover:text-primary-foreground transition-colors underline underline-offset-2">sign in</Link>
            </p>
          </div>
        </div>
      </section>
    </AnimatedSection>

    {/* Footer — informational links only, no redundant CTAs */}
    <footer className="border-t border-border py-10 px-6">
      <div className="max-w-5xl mx-auto flex flex-col md:flex-row items-center justify-between gap-4">
        <div className="font-display text-xl font-bold text-foreground">alum<span className="text-primary">.</span></div>
        <div className="flex items-center gap-8 text-sm text-muted-foreground">
          <a href="#" className="hover:text-foreground transition-colors">About</a>
          <a href="#" className="hover:text-foreground transition-colors">Features</a>
          <a href="#" className="hover:text-foreground transition-colors">Contact</a>
        </div>
        <p className="text-xs text-muted-foreground">© {new Date().getFullYear()} Alumni Association Platform</p>
      </div>
    </footer>
  </div>
);

export default LandingPage;
