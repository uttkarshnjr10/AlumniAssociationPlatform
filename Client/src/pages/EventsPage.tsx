import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import { fetchEvents } from '@/services/events';
import { getEventImageUrl } from '@/utils/images';
import EmptyState from '@/components/common/EmptyState';
import SkeletonCard from '@/components/common/SkeletonCard';
import type { EventDto } from '@/types';
import { CalendarDays, MapPin, Clock, Users, Plus } from 'lucide-react';
import { formatTime } from '@/utils/time';

const EventsPage = () => {
  const [events, setEvents] = useState<EventDto[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => { fetchEvents().then(setEvents).catch(() => {}).finally(() => setLoading(false)); }, []);

  if (loading) return <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">{[1, 2, 3].map((i) => <SkeletonCard key={i} />)}</div>;

  return (
    <div>
      <div className="flex items-center justify-between mb-8">
        <div>
          <h1 className="font-display text-2xl font-bold text-foreground">Events</h1>
          <p className="text-muted-foreground text-sm mt-1">Discover and join events in your community</p>
        </div>
        <Link to="/events/create" className="inline-flex items-center gap-2 px-5 py-2.5 rounded-xl gradient-primary text-primary-foreground text-sm font-medium hover:opacity-90 active:scale-95 transition-all">
          <Plus className="h-4 w-4" /> Create Event
        </Link>
      </div>

      {events.length === 0 ? (
        <EmptyState icon={CalendarDays} title="No events yet" description="Create the first event for your community!" action={<Link to="/events/create" className="px-5 py-2 rounded-xl gradient-primary text-primary-foreground text-sm font-medium">Create Event</Link>} />
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {events.map((event, i) => (
            <motion.div key={event.id} initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: i * 0.05 }}>
              <Link to={`/events/${event.id}`} className="block group">
                <div className="bg-card rounded-2xl shadow-[0_1px_3px_rgba(0,0,0,0.04)] overflow-hidden hover:shadow-[0_8px_30px_rgba(0,0,0,0.06)] hover:scale-[1.01] transition-all duration-300">
                  <div className="relative h-48 bg-gradient-to-br from-primary/10 to-accent/10">
                    {event.imageUrl ? (
                      <img src={getEventImageUrl(event.imageUrl)} alt={event.title} className="h-full w-full object-cover" />
                    ) : (
                      <div className="h-full w-full flex items-center justify-center">
                        <CalendarDays className="h-12 w-12 text-primary/20" />
                      </div>
                    )}
                    <div className="absolute top-3 left-3 bg-card/90 backdrop-blur-sm rounded-lg px-3 py-1.5 text-center">
                      <p className="text-xs font-bold text-primary uppercase">{new Date(event.date).toLocaleDateString('en-US', { month: 'short' })}</p>
                      <p className="text-lg font-bold text-foreground leading-none">{new Date(event.date).getDate()}</p>
                    </div>
                  </div>
                  <div className="p-5">
                    <h3 className="font-semibold text-foreground line-clamp-2 mb-3 group-hover:text-primary transition-colors">{event.title}</h3>
                    <div className="space-y-2 text-sm text-muted-foreground">
                      <div className="flex items-center gap-2"><MapPin className="h-3.5 w-3.5 shrink-0" /><span className="truncate">{event.location}</span></div>
                      <div className="flex items-center gap-2"><Clock className="h-3.5 w-3.5 shrink-0" /><span>{formatTime(event.time)}</span></div>
                      <div className="flex items-center gap-2"><Users className="h-3.5 w-3.5 shrink-0" /><span>{event.attendees.length} attending</span></div>
                    </div>
                  </div>
                </div>
              </Link>
            </motion.div>
          ))}
        </div>
      )}
    </div>
  );
};

export default EventsPage;
