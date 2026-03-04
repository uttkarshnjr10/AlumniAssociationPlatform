import { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { fetchManagedUsers, addManagedUser, removeManagedUser, changeUserStatus } from '@/services/admin';
import UserAvatar from '@/components/common/UserAvatar';
import RoleBadge from '@/components/common/RoleBadge';
import Modal from '@/components/common/Modal';
import ConfirmDialog from '@/components/common/ConfirmDialog';
import type { UserSummaryDto } from '@/types';
import { Users, Plus, Search, Loader2 } from 'lucide-react';

const AdminUsersPage = () => {
  const [users, setUsers] = useState<UserSummaryDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [roleFilter, setRoleFilter] = useState('all');
  const [statusFilter, setStatusFilter] = useState('all');

  const [addOpen, setAddOpen] = useState(false);
  const [addForm, setAddForm] = useState({ name: '', email: '', password: '', role: 'alumnus' });
  const [adding, setAdding] = useState(false);

  const [removeId, setRemoveId] = useState<number | null>(null);
  const [removing, setRemoving] = useState(false);

  useEffect(() => { fetchManagedUsers().then(setUsers).catch(() => {}).finally(() => setLoading(false)); }, []);

  const filtered = users.filter((u) => {
    if (search && !u.name.toLowerCase().includes(search.toLowerCase()) && !u.email.toLowerCase().includes(search.toLowerCase())) return false;
    if (roleFilter !== 'all' && u.role !== roleFilter) return false;
    if (statusFilter !== 'all' && u.status !== statusFilter) return false;
    return true;
  });

  const handleAdd = async () => {
    setAdding(true);
    try {
      const newUser = await addManagedUser(addForm);
      setUsers((prev) => [...prev, newUser]);
      setAddOpen(false);
      setAddForm({ name: '', email: '', password: '', role: 'alumnus' });
    } catch { } finally { setAdding(false); }
  };

  const handleToggleStatus = async (u: UserSummaryDto) => {
    const newStatus = u.status === 'active' ? 'inactive' : 'active';
    try {
      const updated = await changeUserStatus(u.id, newStatus);
      setUsers((prev) => prev.map((x) => x.id === u.id ? updated : x));
    } catch { }
  };

  const handleRemove = async () => {
    if (!removeId) return;
    setRemoving(true);
    try {
      await removeManagedUser(removeId);
      setUsers((prev) => prev.filter((u) => u.id !== removeId));
      setRemoveId(null);
    } catch { } finally { setRemoving(false); }
  };

  const inputCls = "w-full px-4 py-3 rounded-xl border border-border bg-background text-foreground text-sm placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-ring/50 focus:border-primary";
  const selectCls = "px-3 py-2.5 rounded-xl border border-border bg-background text-foreground text-sm focus:outline-none focus:ring-2 focus:ring-ring/50";

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="font-display text-2xl font-bold text-foreground">User Management</h1>
        <button onClick={() => setAddOpen(true)} className="inline-flex items-center gap-2 px-5 py-2.5 rounded-xl gradient-primary text-primary-foreground text-sm font-medium hover:opacity-90 active:scale-95 transition-all">
          <Plus className="h-4 w-4" /> Add User
        </button>
      </div>

      <div className="flex flex-wrap gap-3">
        <div className="relative flex-1 min-w-[200px]">
          <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
          <input value={search} onChange={(e) => setSearch(e.target.value)} placeholder="Search users..." className={inputCls + ' pl-10'} />
        </div>
        <select value={roleFilter} onChange={(e) => setRoleFilter(e.target.value)} className={selectCls}>
          <option value="all">All Roles</option>
          <option value="admin">Admin</option>
          <option value="alumnus">Alumni</option>
          <option value="student">Student</option>
        </select>
        <select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)} className={selectCls}>
          <option value="all">All Status</option>
          <option value="active">Active</option>
          <option value="inactive">Inactive</option>
        </select>
      </div>

      {loading ? (
        <div className="flex justify-center py-12"><Loader2 className="h-6 w-6 animate-spin text-primary" /></div>
      ) : filtered.length === 0 ? (
        <div className="text-center py-12 text-muted-foreground text-sm">No users found.</div>
      ) : (
        <div className="space-y-3">
          {filtered.map((u, i) => (
            <motion.div key={u.id} initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: i * 0.03 }} className="bg-card rounded-2xl shadow-[0_1px_3px_rgba(0,0,0,0.04)] p-4 flex items-center gap-4 hover:shadow-[0_4px_20px_rgba(0,0,0,0.06)] transition-all">
              <UserAvatar name={u.name} profilePictureUrl={u.profilePictureUrl} size="md" />
              <div className="flex-1 min-w-0">
                <p className="font-medium text-foreground truncate">{u.name}</p>
                <p className="text-xs text-muted-foreground truncate">{u.email}</p>
              </div>
              <RoleBadge role={u.role} />
              <span className={`text-xs font-medium px-2 py-0.5 rounded-full ${u.status === 'active' ? 'bg-success/10 text-success' : 'bg-destructive/10 text-destructive'}`}>{u.status}</span>
              <div className="flex gap-2">
                <button onClick={() => handleToggleStatus(u)} className="px-3 py-1.5 rounded-lg border border-border text-xs font-medium text-foreground hover:bg-muted transition-colors">
                  {u.status === 'active' ? 'Deactivate' : 'Activate'}
                </button>
                <button onClick={() => setRemoveId(u.id)} className="px-3 py-1.5 rounded-lg border border-destructive text-xs font-medium text-destructive hover:bg-destructive/10 transition-colors">Remove</button>
              </div>
            </motion.div>
          ))}
        </div>
      )}

      <Modal open={addOpen} onClose={() => setAddOpen(false)} title="Add User">
        <div className="space-y-4">
          <input value={addForm.name} onChange={(e) => setAddForm({ ...addForm, name: e.target.value })} placeholder="Full name" className={inputCls} />
          <input type="email" value={addForm.email} onChange={(e) => setAddForm({ ...addForm, email: e.target.value })} placeholder="Email" className={inputCls} />
          <input type="password" value={addForm.password} onChange={(e) => setAddForm({ ...addForm, password: e.target.value })} placeholder="Password" className={inputCls} />
          <select value={addForm.role} onChange={(e) => setAddForm({ ...addForm, role: e.target.value })} className={selectCls + ' w-full'}>
            <option value="alumnus">Alumni</option>
            <option value="student">Student</option>
          </select>
          <button onClick={handleAdd} disabled={adding || !addForm.name || !addForm.email || !addForm.password} className="w-full py-3 rounded-xl gradient-primary text-primary-foreground font-medium text-sm hover:opacity-90 disabled:opacity-50 flex items-center justify-center gap-2">
            {adding ? <><Loader2 className="h-4 w-4 animate-spin" /> Adding...</> : 'Add User'}
          </button>
        </div>
      </Modal>

      <ConfirmDialog open={!!removeId} onClose={() => setRemoveId(null)} onConfirm={handleRemove} title="Remove User" message="Are you sure you want to remove this user?" confirmLabel="Remove" loading={removing} />
    </div>
  );
};

export default AdminUsersPage;
