import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import AuthLayout from '@/layouts/AuthLayout';
import Modal from '@/components/common/Modal';
import { registerCollege } from '@/services/college';
import { Loader2, CheckCircle } from 'lucide-react';

const sections = [
  { num: 1, label: 'College Details' },
  { num: 2, label: 'Contact Person' },
  { num: 3, label: 'Admin Account' },
];

const CollegeRegistrationPage = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [successModal, setSuccessModal] = useState(false);

  const [form, setForm] = useState({
    collegeName: '', address: '',
    contactPersonName: '', contactEmail: '', contactPhone: '',
    adminName: '', adminEmail: '', adminPassword: '',
  });

  const update = (key: string, value: string) => setForm((f) => ({ ...f, [key]: value }));

  const passwordStrength = () => {
    const p = form.adminPassword;
    if (!p) return { label: '', color: '', width: '0%' };
    let score = 0;
    if (p.length >= 8) score++;
    if (/[A-Z]/.test(p)) score++;
    if (/[0-9]/.test(p)) score++;
    if (/[^A-Za-z0-9]/.test(p)) score++;
    const levels = [
      { label: 'Weak', color: 'bg-destructive', width: '25%' },
      { label: 'Fair', color: 'bg-warning', width: '50%' },
      { label: 'Good', color: 'bg-primary', width: '75%' },
      { label: 'Strong', color: 'bg-success', width: '100%' },
    ];
    return levels[Math.min(score, 4) - 1] || levels[0];
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      await registerCollege(form);
      setSuccessModal(true);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Registration failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const strength = passwordStrength();
  const inputCls = "w-full px-4 py-3.5 rounded-xl border border-border bg-background text-foreground text-sm placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-ring/50 focus:border-primary transition-all";

  return (
    <AuthLayout>
      <div className="max-w-lg mx-auto">
        <div className="mb-10">
          <div className="lg:hidden font-display text-2xl font-bold text-foreground mb-6">
            alum<span className="text-primary">.</span>
          </div>
          <h1 className="font-display text-3xl font-bold text-foreground">Register Your College</h1>
          <p className="text-muted-foreground text-sm mt-2">Set up your alumni network in minutes</p>
        </div>

        {/* Visual progress indicator */}
        <div className="flex items-center gap-2 mb-10">
          {sections.map(({ num, label }, i) => (
            <div key={num} className="flex items-center gap-2 flex-1">
              <div className="flex items-center gap-2 min-w-0">
                <span className="h-7 w-7 rounded-full bg-primary/10 text-primary text-xs font-semibold flex items-center justify-center shrink-0">{num}</span>
                <span className="text-xs text-muted-foreground truncate hidden sm:block">{label}</span>
              </div>
              {i < sections.length - 1 && <div className="flex-1 h-px bg-border" />}
            </div>
          ))}
        </div>

        {error && (
          <div className="bg-destructive/8 border border-destructive/15 text-destructive text-sm rounded-xl px-4 py-3 mb-6">{error}</div>
        )}

        <form onSubmit={handleSubmit} className="space-y-10">
          {/* Section 1: College Details */}
          <section>
            <div className="flex items-center gap-3 mb-5">
              <span className="font-display text-3xl font-bold text-primary/15">1</span>
              <h2 className="text-sm font-semibold text-foreground">College Details</h2>
            </div>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-foreground mb-1.5">College Name <span className="text-destructive">*</span></label>
                <input value={form.collegeName} onChange={(e) => update('collegeName', e.target.value)} required placeholder="e.g. MIT" className={inputCls} />
              </div>
              <div>
                <label className="block text-sm font-medium text-foreground mb-1.5">Address <span className="text-destructive">*</span></label>
                <textarea value={form.address} onChange={(e) => update('address', e.target.value)} required placeholder="Full address" rows={2} className={inputCls + ' resize-none'} />
              </div>
            </div>
          </section>

          <div className="h-px bg-border" />

          {/* Section 2: Contact Person */}
          <section>
            <div className="flex items-center gap-3 mb-5">
              <span className="font-display text-3xl font-bold text-primary/15">2</span>
              <h2 className="text-sm font-semibold text-foreground">Contact Person</h2>
            </div>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-foreground mb-1.5">Full Name <span className="text-destructive">*</span></label>
                <input value={form.contactPersonName} onChange={(e) => update('contactPersonName', e.target.value)} required placeholder="Full name" className={inputCls} />
              </div>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-foreground mb-1.5">Email <span className="text-destructive">*</span></label>
                  <input type="email" value={form.contactEmail} onChange={(e) => update('contactEmail', e.target.value)} required placeholder="Email" className={inputCls} />
                </div>
                <div>
                  <label className="block text-sm font-medium text-foreground mb-1.5">Phone <span className="text-destructive">*</span></label>
                  <input type="tel" value={form.contactPhone} onChange={(e) => update('contactPhone', e.target.value)} required placeholder="Phone" className={inputCls} />
                </div>
              </div>
            </div>
          </section>

          <div className="h-px bg-border" />

          {/* Section 3: Admin Account */}
          <section>
            <div className="flex items-center gap-3 mb-5">
              <span className="font-display text-3xl font-bold text-primary/15">3</span>
              <h2 className="text-sm font-semibold text-foreground">Admin Account</h2>
            </div>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-foreground mb-1.5">Admin Name <span className="text-destructive">*</span></label>
                <input value={form.adminName} onChange={(e) => update('adminName', e.target.value)} required placeholder="Admin full name" className={inputCls} />
              </div>
              <div>
                <label className="block text-sm font-medium text-foreground mb-1.5">Admin Email <span className="text-destructive">*</span></label>
                <input type="email" value={form.adminEmail} onChange={(e) => update('adminEmail', e.target.value)} required placeholder="Admin email" className={inputCls} />
              </div>
              <div>
                <label className="block text-sm font-medium text-foreground mb-1.5">Password <span className="text-destructive">*</span></label>
                <input type="password" value={form.adminPassword} onChange={(e) => update('adminPassword', e.target.value)} required placeholder="Min. 8 characters" minLength={8} className={inputCls} />
                {form.adminPassword && (
                  <div className="mt-2.5 flex items-center gap-2">
                    <div className="flex-1 h-1 bg-muted rounded-full overflow-hidden">
                      <div className={`h-full ${strength.color} rounded-full transition-all`} style={{ width: strength.width }} />
                    </div>
                    <span className="text-xs text-muted-foreground">{strength.label}</span>
                  </div>
                )}
              </div>
            </div>
          </section>

          <button
            type="submit"
            disabled={loading}
            className="w-full py-3.5 rounded-xl gradient-primary text-primary-foreground font-medium text-sm hover:opacity-90 active:scale-[0.98] transition-all disabled:opacity-50 flex items-center justify-center gap-2"
          >
            {loading ? <><Loader2 className="h-4 w-4 animate-spin" /> Registering...</> : 'Register College'}
          </button>
        </form>

        <div className="mt-8 text-center">
          <Link to="/login" className="text-sm text-muted-foreground hover:text-foreground transition-colors">← Back to Sign In</Link>
        </div>
      </div>

      <Modal open={successModal} onClose={() => { setSuccessModal(false); navigate('/login'); }} size="sm">
        <div className="text-center py-6">
          <div className="h-16 w-16 rounded-full bg-success/10 flex items-center justify-center mx-auto mb-5">
            <CheckCircle className="h-8 w-8 text-success" />
          </div>
          <h3 className="font-display text-xl font-semibold text-foreground mb-2">Registration Submitted!</h3>
          <p className="text-sm text-muted-foreground mb-8">Your registration is pending approval. You'll receive an email when approved.</p>
          <button
            onClick={() => { setSuccessModal(false); navigate('/login'); }}
            className="px-8 py-3 rounded-xl gradient-primary text-primary-foreground font-medium text-sm"
          >
            Go to Sign In
          </button>
        </div>
      </Modal>
    </AuthLayout>
  );
};

export default CollegeRegistrationPage;
