import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import { useAuth } from '@/contexts/AuthContext';
import { fetchUserProfileById, followUser, unfollowUser, fetchFollowers, fetchFollowing } from '@/services/profile';
import UserAvatar from '@/components/common/UserAvatar';
import RoleBadge from '@/components/common/RoleBadge';
import Modal from '@/components/common/Modal';
import type { UserProfileDto, UserSummaryDto } from '@/types';
import { UserPlus, UserCheck, Building2, GraduationCap, Mail, Loader2 } from 'lucide-react';

const UserProfilePage = () => {
  const { userId } = useParams();
  const { user } = useAuth();
  const navigate = useNavigate();

  const [profile, setProfile] = useState<UserProfileDto | null>(null);
  const [loading, setLoading] = useState(true);
  const [followHover, setFollowHover] = useState(false);
  const [followModal, setFollowModal] = useState<'followers' | 'following' | null>(null);
  const [followList, setFollowList] = useState<UserSummaryDto[]>([]);
  const [loadingFollow, setLoadingFollow] = useState(false);

  useEffect(() => {
    if (userId && Number(userId) === user?.id) { navigate('/profile', { replace: true }); return; }
    if (userId) fetchUserProfileById(Number(userId)).then(setProfile).catch(() => navigate('/home')).finally(() => setLoading(false));
  }, [userId, user?.id, navigate]);

  const handleFollowToggle = async () => {
    if (!profile) return;
    const was = profile.isFollowedByCurrentUser;
    setProfile({ ...profile, isFollowedByCurrentUser: !was, followersCount: profile.followersCount + (was ? -1 : 1) });
    try { if (was) await unfollowUser(profile.id); else await followUser(profile.id); }
    catch { setProfile({ ...profile, isFollowedByCurrentUser: was, followersCount: profile.followersCount }); }
  };

  const openFollowModal = async (type: 'followers' | 'following') => {
    if (!profile) return;
    setFollowModal(type);
    setLoadingFollow(true);
    try {
      const data = type === 'followers' ? await fetchFollowers(profile.id) : await fetchFollowing(profile.id);
      setFollowList(data);
    } catch { } finally { setLoadingFollow(false); }
  };

  if (loading) return <div className="flex justify-center py-20"><Loader2 className="h-8 w-8 animate-spin text-primary" /></div>;
  if (!profile) return null;

  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="max-w-3xl mx-auto space-y-6">
      <div className="bg-card rounded-2xl shadow-[0_1px_3px_rgba(0,0,0,0.04)] p-6 md:p-8">
        <div className="flex flex-col sm:flex-row items-center sm:items-start gap-6">
          <UserAvatar name={profile.name} profilePictureUrl={profile.profilePictureUrl} size="xl" />
          <div className="flex-1 text-center sm:text-left">
            <div className="flex items-center gap-2 justify-center sm:justify-start">
              <h1 className="font-display text-2xl font-bold text-foreground">{profile.name}</h1>
              <RoleBadge role={profile.role} />
            </div>
            {profile.collegeName && <p className="text-sm text-muted-foreground mt-1">{profile.collegeName}</p>}
            {profile.bio && <p className="text-sm text-foreground mt-3">{profile.bio}</p>}
            <div className="flex items-center gap-4 mt-4 justify-center sm:justify-start text-sm">
              {profile.department && <span className="flex items-center gap-1 text-muted-foreground"><Building2 className="h-3.5 w-3.5" />{profile.department}</span>}
              {profile.graduationYear && <span className="flex items-center gap-1 text-muted-foreground"><GraduationCap className="h-3.5 w-3.5" />Class of {profile.graduationYear}</span>}
            </div>
            <div className="flex items-center gap-6 mt-4 justify-center sm:justify-start">
              <button onClick={() => openFollowModal('followers')} className="text-sm hover:text-primary transition-colors"><span className="font-bold text-foreground">{profile.followersCount}</span> <span className="text-muted-foreground">Followers</span></button>
              <button onClick={() => openFollowModal('following')} className="text-sm hover:text-primary transition-colors"><span className="font-bold text-foreground">{profile.followingCount}</span> <span className="text-muted-foreground">Following</span></button>
            </div>
          </div>
          <button
            onClick={handleFollowToggle}
            onMouseEnter={() => setFollowHover(true)}
            onMouseLeave={() => setFollowHover(false)}
            className={`inline-flex items-center gap-2 px-5 py-2.5 rounded-xl text-sm font-medium transition-all active:scale-95 shrink-0 ${
              profile.isFollowedByCurrentUser
                ? followHover ? 'border border-destructive text-destructive hover:bg-destructive/10' : 'border border-border text-foreground hover:bg-muted'
                : 'gradient-primary text-primary-foreground hover:opacity-90'
            }`}
          >
            {profile.isFollowedByCurrentUser ? (
              followHover ? <><UserPlus className="h-4 w-4" /> Unfollow</> : <><UserCheck className="h-4 w-4" /> Following</>
            ) : (
              <><UserPlus className="h-4 w-4" /> Follow</>
            )}
          </button>
        </div>
      </div>

      <div className="bg-card rounded-2xl shadow-[0_1px_3px_rgba(0,0,0,0.04)] p-6">
        <h2 className="font-display text-lg font-semibold text-foreground mb-4">About</h2>
        <div className="space-y-3 text-sm">
          <div className="flex items-center gap-3"><Mail className="h-4 w-4 text-muted-foreground" /><span className="text-foreground">{profile.email}</span></div>
          {profile.department && <div className="flex items-center gap-3"><Building2 className="h-4 w-4 text-muted-foreground" /><span className="text-foreground">{profile.department}</span></div>}
          {profile.graduationYear && <div className="flex items-center gap-3"><GraduationCap className="h-4 w-4 text-muted-foreground" /><span className="text-foreground">Class of {profile.graduationYear}</span></div>}
        </div>
      </div>

      <Modal open={!!followModal} onClose={() => setFollowModal(null)} title={followModal === 'followers' ? 'Followers' : 'Following'}>
        {loadingFollow ? (
          <div className="flex justify-center py-8"><Loader2 className="h-5 w-5 animate-spin text-muted-foreground" /></div>
        ) : followList.length === 0 ? (
          <p className="text-center text-muted-foreground text-sm py-8">No {followModal} yet.</p>
        ) : (
          <div className="space-y-2 max-h-64 overflow-y-auto">
            {followList.map((u) => (
              <Link key={u.id} to={`/users/${u.id}`} onClick={() => setFollowModal(null)} className="flex items-center gap-3 p-2 rounded-xl hover:bg-muted transition-colors">
                <UserAvatar name={u.name} profilePictureUrl={u.profilePictureUrl} size="sm" />
                <div className="flex-1"><p className="text-sm font-medium text-foreground">{u.name}</p></div>
                <RoleBadge role={u.role} />
              </Link>
            ))}
          </div>
        )}
      </Modal>
    </motion.div>
  );
};

export default UserProfilePage;
