import { useState, useRef } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';
import { createEvent } from '@/services/events';
import { ImagePlus, X, Loader2, ArrowLeft } from 'lucide-react';

const CreateEventPage = () => {
  const { user } = useAuth();
  const navigate = useNavigate();
  const fileRef = useRef<HTMLInputElement>(null);

  const [form, setForm] = useState({ title: '', description: '', datetime: '', location: '' });
  const [image, setImage] = useState<File | null>(null);
  const [preview, setPreview] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const update = (key: string, value: string) => setForm((f) => ({ ...f, [key]: value }));

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const [date, time] = form.datetime.split('T');
      await createEvent({ title: form.title, description: form.description, date, time: time || '00:00', location: form.location, collegeId: 0 }, image || undefined);
      navigate('/events');
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to create event');
    } finally { setLoading(false); }
  };

  const inputCls = "w-full px-4 py-3 rounded-xl border border-border bg-background text-foreground text-sm placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-ring/50 focus:border-primary transition-all";

  return (
    <div className="max-w-2xl mx-auto">
      <Link to="/events" className="inline-flex items-center gap-2 text-sm text-muted-foreground hover:text-foreground mb-6 transition-colors">
        <ArrowLeft className="h-4 w-4" /> Back to Events
      </Link>

      <div className="bg-card rounded-2xl shadow-[0_1px_3px_rgba(0,0,0,0.04)] p-6 md:p-8">
        <h1 className="font-display text-xl font-bold text-foreground mb-6">Create Event</h1>

        {error && <div className="bg-destructive/8 border border-destructive/15 text-destructive text-sm rounded-xl px-4 py-3 mb-4">{error}</div>}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-foreground mb-2">Title <span className="text-destructive">*</span></label>
            <input value={form.title} onChange={(e) => update('title', e.target.value)} required placeholder="Event title" className={inputCls} />
          </div>
          <div>
            <label className="block text-sm font-medium text-foreground mb-2">Description <span className="text-destructive">*</span></label>
            <textarea value={form.description} onChange={(e) => update('description', e.target.value)} required placeholder="Describe your event..." rows={4} className={inputCls + ' resize-none'} />
          </div>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-foreground mb-2">Date & Time <span className="text-destructive">*</span></label>
              <input type="datetime-local" value={form.datetime} onChange={(e) => update('datetime', e.target.value)} required className={inputCls} />
            </div>
            <div>
              <label className="block text-sm font-medium text-foreground mb-2">Location <span className="text-destructive">*</span></label>
              <input value={form.location} onChange={(e) => update('location', e.target.value)} required placeholder="Event location" className={inputCls} />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-foreground mb-2">Event Image</label>
            {preview ? (
              <div className="relative">
                <img src={preview} alt="Preview" className="w-full h-48 rounded-xl object-cover" />
                <button type="button" onClick={() => { setImage(null); setPreview(''); }} className="absolute top-2 right-2 h-7 w-7 bg-foreground/80 text-background rounded-full flex items-center justify-center"><X className="h-4 w-4" /></button>
              </div>
            ) : (
              <button type="button" onClick={() => fileRef.current?.click()} className="w-full h-32 rounded-xl border-2 border-dashed border-border hover:border-primary/40 flex flex-col items-center justify-center gap-2 text-muted-foreground hover:text-primary transition-colors">
                <ImagePlus className="h-8 w-8" />
                <span className="text-sm">Click to upload image</span>
              </button>
            )}
            <input ref={fileRef} type="file" accept="image/*" onChange={(e) => { const f = e.target.files?.[0]; if (f) { setImage(f); setPreview(URL.createObjectURL(f)); } }} className="hidden" />
          </div>

          <div className="flex gap-3 pt-4">
            <button type="submit" disabled={loading} className="flex-1 py-3 rounded-xl gradient-primary text-primary-foreground font-medium text-sm hover:opacity-90 active:scale-[0.98] transition-all disabled:opacity-50 flex items-center justify-center gap-2">
              {loading ? <><Loader2 className="h-4 w-4 animate-spin" /> Creating...</> : 'Create Event'}
            </button>
            <Link to="/events" className="px-6 py-3 rounded-xl border border-border text-sm font-medium text-foreground hover:bg-muted transition-colors">Cancel</Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CreateEventPage;
