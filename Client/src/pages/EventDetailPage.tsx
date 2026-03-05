import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import { useAuth } from '@/contexts/AuthContext';
import { fetchEventById, joinEvent, leaveEvent, deleteEvent } from '@/services/events';
import { getEventImageUrl } from '@/utils/images';
import { formatEventDate } from '@/utils/time';
import UserAvatar from '@/components/common/UserAvatar';
import ConfirmDialog from '@/components/common/ConfirmDialog';
import Modal from '@/components/common/Modal';
import type { EventDto } from '@/types';
import { CalendarDays, MapPin, UserPlus, UserMinus, ArrowLeft, Trash2, Users, Loader2 } from 'lucide-react';

const EventDetailPage = () => {
  const { eventId } = useParams();
  const { user } = useAuth();
  const navigate = useNavigate();

  const [event, setEvent] = useState<EventDto | null>(null);
  const [loading, setLoading] = useState(true);
  const [joining, setJoining] = useState(false);
  const [deleteOpen, setDeleteOpen] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const [attendeesOpen, setAttendeesOpen] = useState(false);

  useEffect(() => {
    if (eventId) fetchEventById(Number(eventId)).then(setEvent).catch(() => navigate('/events')).finally(() => setLoading(false));
  }, [eventId, navigate]);

  const handleJoinToggle = async () => {
    if (!event) return;
    const was = event.isAttending;
    setEvent({ ...event, isAttending: !was, attendees: was ? event.attendees.filter((a) => a.id !== user?.id) : [...event.attendees, user!] });
    setJoining(true);
    try { if (was) await leaveEvent(event.id); else await joinEvent(event.id); }
    catch { setEvent({ ...event, isAttending: was }); }
    finally { setJoining(false); }
  };

  const handleDelete = async () => {
    if (!event) return;
    setDeleting(true);
    try { await deleteEvent(event.id); navigate('/events'); }
    catch { } finally { setDeleting(false); }
  };

  if (loading) return <div className="flex justify-center py-20"><Loader2 className="h-8 w-8 animate-spin text-primary" /></div>;
  if (!event) return null;

  const isCreator = user?.id === event.createdBy.id;
  const isAdmin = user?.role === 'admin';

  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="max-w-3xl mx-auto">
      <Link to="/events" className="inline-flex items-center gap-2 text-sm text-muted-foreground hover:text-foreground mb-6 transition-colors">
        <ArrowLeft className="h-4 w-4" /> Back to Events
      </Link>

      <div className="relative h-64 md:h-80 rounded-2xl overflow-hidden mb-6 bg-gradient-to-br from-primary/10 to-accent/10">
        {event.imageUrl ? (
          <img src={getEventImageUrl(event.imageUrl)} alt={event.title} className="h-full w-full object-cover" />
        ) : (
          <div className="h-full w-full flex items-center justify-center"><CalendarDays className="h-20 w-20 text-primary/20" /></div>
        )}
        <div className="absolute inset-0 bg-gradient-to-t from-black/60 to-transparent" />
        <div className="absolute bottom-6 left-6 right-6 text-white">
          <h1 className="font-display text-2xl md:text-3xl font-bold">{event.title}</h1>
        </div>
      </div>

      <div className="bg-card rounded-2xl shadow-[0_1px_3px_rgba(0,0,0,0.04)] p-6 md:p-8 space-y-6">
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div className="flex items-center gap-3 text-sm">
            <div className="h-10 w-10 rounded-xl bg-primary/8 flex items-center justify-center shrink-0"><CalendarDays className="h-5 w-5 text-primary" /></div>
            <div>
              <p className="text-muted-foreground text-xs">Date & Time</p>
              <p className="font-medium text-foreground">{formatEventDate(event.date, event.time)}</p>
            </div>
          </div>
          <div className="flex items-center gap-3 text-sm">
            <div className="h-10 w-10 rounded-xl bg-accent/8 flex items-center justify-center shrink-0"><MapPin className="h-5 w-5 text-accent" /></div>
            <div>
              <p className="text-muted-foreground text-xs">Location</p>
              <p className="font-medium text-foreground">{event.location}</p>
            </div>
          </div>
        </div>

        <div className="flex items-center gap-3">
          <UserAvatar name={event.createdBy.name} profilePictureUrl={event.createdBy.profilePictureUrl} size="sm" />
          <div>
            <p className="text-xs text-muted-foreground">Organized by</p>
            <Link to={`/users/${event.createdBy.id}`} className="text-sm font-medium text-foreground hover:text-primary">{event.createdBy.name}</Link>
          </div>
        </div>

        <p className="text-sm text-foreground whitespace-pre-wrap">{event.description}</p>

        <div className="flex flex-wrap gap-3">
          <button onClick={handleJoinToggle} disabled={joining} className={`flex items-center gap-2 px-5 py-2.5 rounded-xl text-sm font-medium transition-all active:scale-95 ${event.isAttending ? 'border border-destructive text-destructive hover:bg-destructive/10' : 'gradient-primary text-primary-foreground hover:opacity-90'}`}>
            {event.isAttending ? <><UserMinus className="h-4 w-4" /> Leave Event</> : <><UserPlus className="h-4 w-4" /> Join Event</>}
          </button>
          {(isCreator || isAdmin) && (
            <button onClick={() => setDeleteOpen(true)} className="flex items-center gap-2 px-5 py-2.5 rounded-xl border border-destructive text-destructive text-sm font-medium hover:bg-destructive/10 transition-colors">
              <Trash2 className="h-4 w-4" /> Delete
            </button>
          )}
        </div>

        <div>
          <button onClick={() => setAttendeesOpen(true)} className="flex items-center gap-2 text-sm font-medium text-foreground hover:text-primary transition-colors">
            <Users className="h-4 w-4" /> {event.attendees.length} attending
          </button>
          <div className="flex -space-x-2 mt-2">
            {event.attendees.slice(0, 8).map((a) => <UserAvatar key={a.id} name={a.name} profilePictureUrl={a.profilePictureUrl} size="sm" />)}
            {event.attendees.length > 8 && <div className="h-8 w-8 rounded-full bg-muted flex items-center justify-center text-xs font-medium text-muted-foreground ring-2 ring-card">+{event.attendees.length - 8}</div>}
          </div>
        </div>
      </div>

      <Modal open={attendeesOpen} onClose={() => setAttendeesOpen(false)} title={`Attendees (${event.attendees.length})`}>
        <div className="space-y-3 max-h-64 overflow-y-auto">
          {event.attendees.map((a) => (
            <Link key={a.id} to={`/users/${a.id}`} onClick={() => setAttendeesOpen(false)} className="flex items-center gap-3 p-2 rounded-xl hover:bg-muted transition-colors">
              <UserAvatar name={a.name} profilePictureUrl={a.profilePictureUrl} size="sm" />
              <span className="text-sm font-medium text-foreground">{a.name}</span>
            </Link>
          ))}
        </div>
      </Modal>

      <ConfirmDialog open={deleteOpen} onClose={() => setDeleteOpen(false)} onConfirm={handleDelete} title="Delete Event" message="Are you sure? This cannot be undone." confirmLabel="Delete" loading={deleting} />
    </motion.div>
  );
};

export default EventDetailPage;
