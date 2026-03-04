import { useState, useEffect, useRef } from 'react';
import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import { searchUsers } from '@/services/search';
import UserAvatar from '@/components/common/UserAvatar';
import RoleBadge from '@/components/common/RoleBadge';
import EmptyState from '@/components/common/EmptyState';
import type { UserSummaryDto } from '@/types';
import { Search as SearchIcon, X, Loader2, Users } from 'lucide-react';

const SearchPage = () => {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState<UserSummaryDto[]>([]);
  const [loading, setLoading] = useState(false);
  const [searched, setSearched] = useState(false);
  const inputRef = useRef<HTMLInputElement>(null);
  const timerRef = useRef<ReturnType<typeof setTimeout>>();

  useEffect(() => { inputRef.current?.focus(); }, []);

  useEffect(() => {
    if (timerRef.current) clearTimeout(timerRef.current);
    if (!query.trim()) { setResults([]); setSearched(false); return; }
    timerRef.current = setTimeout(async () => {
      setLoading(true);
      setSearched(true);
      try { const data = await searchUsers(query); setResults(data); }
      catch { } finally { setLoading(false); }
    }, 300);
    return () => { if (timerRef.current) clearTimeout(timerRef.current); };
  }, [query]);

  return (
    <div className="max-w-3xl mx-auto">
      <div className="relative mb-8">
        <SearchIcon className="absolute left-4 top-1/2 -translate-y-1/2 h-5 w-5 text-muted-foreground" />
        <input
          ref={inputRef}
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="Search alumni and students..."
          className="w-full pl-12 pr-12 py-4 rounded-2xl border border-border bg-card text-foreground text-base placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-ring/50 focus:border-primary transition-all shadow-[0_1px_3px_rgba(0,0,0,0.04)]"
        />
        {query && (
          <button onClick={() => setQuery('')} className="absolute right-4 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground">
            <X className="h-5 w-5" />
          </button>
        )}
      </div>

      {loading ? (
        <div className="flex justify-center py-12"><Loader2 className="h-6 w-6 animate-spin text-primary" /></div>
      ) : !searched ? (
        <EmptyState icon={SearchIcon} title="Find People" description="Start typing to search for alumni and students in your college network." />
      ) : results.length === 0 ? (
        <EmptyState icon={Users} title="No results" description={`No users found matching "${query}"`} />
      ) : (
        <>
          <p className="text-sm text-muted-foreground mb-4">Found {results.length} user{results.length !== 1 ? 's' : ''}</p>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            {results.map((u, i) => (
              <motion.div key={u.id} initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: i * 0.05 }}>
                <Link to={`/users/${u.id}`} className="flex items-center gap-4 p-4 bg-card rounded-2xl shadow-[0_1px_3px_rgba(0,0,0,0.04)] hover:shadow-[0_4px_20px_rgba(0,0,0,0.06)] transition-all duration-300">
                  <UserAvatar name={u.name} profilePictureUrl={u.profilePictureUrl} size="lg" />
                  <div className="flex-1 min-w-0">
                    <h3 className="font-semibold text-foreground truncate">{u.name}</h3>
                    <p className="text-xs text-muted-foreground truncate">{u.email}</p>
                    <RoleBadge role={u.role} className="mt-1" />
                  </div>
                </Link>
              </motion.div>
            ))}
          </div>
        </>
      )}
    </div>
  );
};

export default SearchPage;
