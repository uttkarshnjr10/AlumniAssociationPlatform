import { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';
import { fetchUserProfileById, updateUserProfile } from '@/services/profile';
import { ArrowLeft, Loader2, Lock } from 'lucide-react';

const EditProfilePage = () => {
  const { user, updateUserContext } = useAuth();
  const navigate = useNavigate();

  const [form, setForm] = useState({ name: '', bio: '', department: '', graduationYear: '' });
  const [email, setEmail] = useState('');
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (user?.id) {
      fetchUserProfileById(user.id).then((p) => {
        setForm({ name: p.name, bio: p.bio || '', department: p.department || '', graduationYear: p.graduationYear?.toString() || '' });
        setEmail(p.email);
      }).finally(() => setLoading(false));
    }
  }, [user?.id]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    setError('');
    try {
      const updated = await updateUserProfile({
        name: form.name,
        bio: form.bio || undefined,
        department: form.department || undefined,
        graduationYear: form.graduationYear ? Number(form.graduationYear) : undefined,
      });
      updateUserContext({ ...user!, name: updated.name });
      navigate('/profile');
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to update profile');
    } finally { setSaving(false); }
  };

  const inputCls = "w-full px-4 py-3 rounded-xl border border-border bg-background text-foreground text-sm placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-ring/50 focus:border-primary transition-all";

  if (loading) return <div className="flex justify-center py-20"><Loader2 className="h-8 w-8 animate-spin text-primary" /></div>;

  return (
    <div className="max-w-xl mx-auto">
      <Link to="/profile" className="inline-flex items-center gap-2 text-sm text-muted-foreground hover:text-foreground mb-6 transition-colors">
        <ArrowLeft className="h-4 w-4" /> Back to Profile
      </Link>

      <div className="bg-card rounded-2xl shadow-[0_1px_3px_rgba(0,0,0,0.04)] p-6 md:p-8">
        <h1 className="font-display text-xl font-bold text-foreground mb-6">Edit Profile</h1>

        {error && <div className="bg-destructive/8 border border-destructive/15 text-destructive text-sm rounded-xl px-4 py-3 mb-4">{error}</div>}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-foreground mb-2">Name</label>
            <input value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required className={inputCls} />
          </div>
          <div>
            <label className="block text-sm font-medium text-foreground mb-2">Email</label>
            <div className="relative">
              <input value={email} disabled className={inputCls + ' bg-muted cursor-not-allowed pr-10'} />
              <Lock className="absolute right-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
            </div>
          </div>
          <div>
            <label className="block text-sm font-medium text-foreground mb-2">Bio</label>
            <textarea value={form.bio} onChange={(e) => setForm({ ...form, bio: e.target.value.slice(0, 500) })} rows={3} placeholder="Tell us about yourself..." className={inputCls + ' resize-none'} />
            <p className="text-xs text-muted-foreground mt-1 text-right">{form.bio.length}/500</p>
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-foreground mb-2">Department</label>
              <input value={form.department} onChange={(e) => setForm({ ...form, department: e.target.value })} placeholder="e.g. Computer Science" className={inputCls} />
            </div>
            <div>
              <label className="block text-sm font-medium text-foreground mb-2">Graduation Year</label>
              <input type="number" value={form.graduationYear} onChange={(e) => setForm({ ...form, graduationYear: e.target.value })} placeholder="e.g. 2024" min="1900" max="2100" className={inputCls} />
            </div>
          </div>

          <div className="flex gap-3 pt-4">
            <button type="submit" disabled={saving} className="flex-1 py-3 rounded-xl gradient-primary text-primary-foreground font-medium text-sm hover:opacity-90 active:scale-[0.98] transition-all disabled:opacity-50 flex items-center justify-center gap-2">
              {saving ? <><Loader2 className="h-4 w-4 animate-spin" /> Saving...</> : 'Save Changes'}
            </button>
            <Link to="/profile" className="px-6 py-3 rounded-xl border border-border text-sm font-medium text-foreground hover:bg-muted transition-colors text-center">Cancel</Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EditProfilePage;
