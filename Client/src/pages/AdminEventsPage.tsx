import { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { fetchEvents } from '@/services/events';
import { removeEventByAdmin } from '@/services/admin';
import { getEventImageUrl } from '@/utils/images';
import ConfirmDialog from '@/components/common/ConfirmDialog';
import EmptyState from '@/components/common/EmptyState';
import type { EventDto } from '@/types';
import { CalendarDays, MapPin, Trash2, Loader2 } from 'lucide-react';

const AdminEventsPage = () => {
  const [events, setEvents] = useState<EventDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [removeId, setRemoveId] = useState<number | null>(null);
  const [removing, setRemoving] = useState(false);

  useEffect(() => { fetchEvents().then(setEvents).catch(() => {}).finally(() => setLoading(false)); }, []);

  const handleRemove = async () => {
    if (!removeId) return;
    setRemoving(true);
    try {
      await removeEventByAdmin(removeId);
      setEvents((prev) => prev.filter((e) => e.id !== removeId));
      setRemoveId(null);
    } catch { } finally { setRemoving(false); }
  };

  if (loading) return <div className="flex justify-center py-20"><Loader2 className="h-8 w-8 animate-spin text-primary" /></div>;

  return (
    <div>
      <h1 className="font-display text-2xl font-bold text-foreground mb-6">Events Management</h1>

      {events.length === 0 ? (
        <EmptyState icon={CalendarDays} title="No events" description="No events found in your college." />
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {events.map((event, i) => (
            <motion.div key={event.id} initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: i * 0.05 }} className="bg-card rounded-2xl shadow-[0_1px_3px_rgba(0,0,0,0.04)] overflow-hidden">
              <div className="h-36 bg-gradient-to-br from-primary/10 to-accent/10">
                {event.imageUrl && <img src={getEventImageUrl(event.imageUrl)} alt={event.title} className="h-full w-full object-cover" />}
              </div>
              <div className="p-4">
                <h3 className="font-semibold text-foreground line-clamp-1 mb-1">{event.title}</h3>
                <p className="text-xs text-muted-foreground flex items-center gap-1 mb-1"><CalendarDays className="h-3 w-3" />{event.date}</p>
                <p className="text-xs text-muted-foreground flex items-center gap-1 mb-3"><MapPin className="h-3 w-3" />{event.location}</p>
                <p className="text-xs text-muted-foreground mb-3">By {event.createdBy.name}</p>
                <button onClick={() => setRemoveId(event.id)} className="w-full flex items-center justify-center gap-2 py-2 rounded-xl border border-destructive text-destructive text-sm font-medium hover:bg-destructive/10 transition-colors">
                  <Trash2 className="h-4 w-4" /> Remove Event
                </button>
              </div>
            </motion.div>
          ))}
        </div>
      )}

      <ConfirmDialog open={!!removeId} onClose={() => setRemoveId(null)} onConfirm={handleRemove} title="Remove Event" message="Are you sure you want to remove this event?" confirmLabel="Remove" loading={removing} />
    </div>
  );
};

export default AdminEventsPage;
