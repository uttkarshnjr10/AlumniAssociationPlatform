import { useState, useEffect, useCallback, useRef } from 'react';
import { Link } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import { useAuth } from '@/contexts/AuthContext';
import UserAvatar from '@/components/common/UserAvatar';
import RoleBadge from '@/components/common/RoleBadge';
import EmptyState from '@/components/common/EmptyState';
import ConfirmDialog from '@/components/common/ConfirmDialog';
import SkeletonCard from '@/components/common/SkeletonCard';
import { fetchPosts, createPost, updatePost, deletePost, likePost, unlikePost, fetchComments, addComment, deleteComment } from '@/services/posts';
import { getPostImageUrl } from '@/utils/images';
import { timeAgo } from '@/utils/time';
import type { PostDto, CommentDto } from '@/types';
import {
  ImagePlus, Send, Heart, MessageCircle, Share2, MoreHorizontal, Edit3, Trash2,
  X, Loader2, Newspaper
} from 'lucide-react';

const FeedPage = () => {
  const { user } = useAuth();
  const [posts, setPosts] = useState<PostDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loadingMore, setLoadingMore] = useState(false);

  const [newContent, setNewContent] = useState('');
  const [newImage, setNewImage] = useState<File | null>(null);
  const [newImagePreview, setNewImagePreview] = useState('');
  const [creating, setCreating] = useState(false);

  const [editingId, setEditingId] = useState<number | null>(null);
  const [editContent, setEditContent] = useState('');

  const [deleteId, setDeleteId] = useState<number | null>(null);
  const [deleting, setDeleting] = useState(false);

  const [openComments, setOpenComments] = useState<number | null>(null);
  const [comments, setComments] = useState<Record<number, CommentDto[]>>({});
  const [commentText, setCommentText] = useState('');
  const [loadingComments, setLoadingComments] = useState(false);

  const fileRef = useRef<HTMLInputElement>(null);

  const loadPosts = useCallback(async (p = 0) => {
    try {
      const data = await fetchPosts(p);
      if (p === 0) setPosts(data);
      else setPosts((prev) => [...prev, ...data]);
      setHasMore(data.length === 10);
    } catch { } finally {
      setLoading(false);
      setLoadingMore(false);
    }
  }, []);

  useEffect(() => { loadPosts(0); }, [loadPosts]);

  const handleCreatePost = async () => {
    if (!newContent.trim()) return;
    setCreating(true);
    try {
      const post = await createPost(newContent, newImage || undefined);
      setPosts((prev) => [post, ...prev]);
      setNewContent('');
      setNewImage(null);
      setNewImagePreview('');
    } catch { } finally { setCreating(false); }
  };

  const handleImageSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      setNewImage(file);
      setNewImagePreview(URL.createObjectURL(file));
    }
  };

  const handleLike = async (post: PostDto) => {
    const wasLiked = post.isLikedByCurrentUser;
    setPosts((prev) => prev.map((p) => p.id === post.id ? { ...p, isLikedByCurrentUser: !wasLiked, likeCount: p.likeCount + (wasLiked ? -1 : 1) } : p));
    try {
      if (wasLiked) await unlikePost(post.id); else await likePost(post.id);
    } catch {
      setPosts((prev) => prev.map((p) => p.id === post.id ? { ...p, isLikedByCurrentUser: wasLiked, likeCount: p.likeCount + (wasLiked ? 1 : -1) } : p));
    }
  };

  const handleEdit = async (postId: number) => {
    try {
      const updated = await updatePost(postId, editContent);
      setPosts((prev) => prev.map((p) => p.id === postId ? { ...p, content: updated.content } : p));
      setEditingId(null);
    } catch { }
  };

  const handleDelete = async () => {
    if (!deleteId) return;
    setDeleting(true);
    try {
      await deletePost(deleteId);
      setPosts((prev) => prev.filter((p) => p.id !== deleteId));
      setDeleteId(null);
    } catch { } finally { setDeleting(false); }
  };

  const toggleComments = async (postId: number) => {
    if (openComments === postId) { setOpenComments(null); return; }
    setOpenComments(postId);
    if (!comments[postId]) {
      setLoadingComments(true);
      try {
        const data = await fetchComments(postId);
        setComments((prev) => ({ ...prev, [postId]: data }));
      } catch { } finally { setLoadingComments(false); }
    }
  };

  const handleAddComment = async (postId: number) => {
    if (!commentText.trim()) return;
    try {
      const comment = await addComment(postId, commentText);
      setComments((prev) => ({ ...prev, [postId]: [...(prev[postId] || []), comment] }));
      setPosts((prev) => prev.map((p) => p.id === postId ? { ...p, commentCount: p.commentCount + 1 } : p));
      setCommentText('');
    } catch { }
  };

  const handleDeleteComment = async (commentId: number, postId: number) => {
    try {
      await deleteComment(commentId);
      setComments((prev) => ({ ...prev, [postId]: (prev[postId] || []).filter((c) => c.id !== commentId) }));
      setPosts((prev) => prev.map((p) => p.id === postId ? { ...p, commentCount: Math.max(0, p.commentCount - 1) } : p));
    } catch { }
  };

  if (loading) return <div className="max-w-2xl mx-auto space-y-4">{[1, 2, 3].map((i) => <SkeletonCard key={i} />)}</div>;

  return (
    <div className="max-w-2xl mx-auto space-y-6">
      {/* Create Post */}
      <div className="bg-card rounded-2xl shadow-[0_1px_3px_rgba(0,0,0,0.04)] p-5">
        <div className="flex gap-3">
          <UserAvatar name={user?.name || ''} profilePictureUrl={user?.profilePictureUrl} size="md" />
          <div className="flex-1">
            <textarea
              value={newContent}
              onChange={(e) => setNewContent(e.target.value)}
              placeholder={`What's on your mind, ${user?.name?.split(' ')[0]}?`}
              rows={2}
              className="w-full resize-none bg-transparent text-foreground text-sm placeholder:text-muted-foreground focus:outline-none"
            />
            {newImagePreview && (
              <div className="relative mt-2 inline-block">
                <img src={newImagePreview} alt="Preview" className="h-32 rounded-xl object-cover" />
                <button onClick={() => { setNewImage(null); setNewImagePreview(''); }} className="absolute -top-2 -right-2 h-6 w-6 bg-foreground text-background rounded-full flex items-center justify-center">
                  <X className="h-3 w-3" />
                </button>
              </div>
            )}
            <div className="flex items-center justify-between mt-3 pt-3 border-t border-border">
              <button onClick={() => fileRef.current?.click()} className="flex items-center gap-2 text-muted-foreground hover:text-primary text-sm transition-colors">
                <ImagePlus className="h-4 w-4" /> Photo
              </button>
              <input ref={fileRef} type="file" accept="image/*" onChange={handleImageSelect} className="hidden" />
              <button
                onClick={handleCreatePost}
                disabled={!newContent.trim() || creating}
                className="px-5 py-2 rounded-xl gradient-primary text-primary-foreground text-sm font-medium disabled:opacity-50 hover:opacity-90 active:scale-95 transition-all flex items-center gap-2"
              >
                {creating ? <Loader2 className="h-4 w-4 animate-spin" /> : <Send className="h-4 w-4" />}
                Post
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* Posts */}
      {posts.length === 0 ? (
        <EmptyState icon={Newspaper} title="No posts yet" description="Be the first to share something with your community!" />
      ) : (
        <div className="space-y-4">
          {posts.map((post, idx) => (
            <motion.div
              key={post.id}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: Math.min(idx * 0.05, 0.3) }}
              className="bg-card rounded-2xl shadow-[0_1px_3px_rgba(0,0,0,0.04)] p-5"
            >
              {/* Header */}
              <div className="flex items-start justify-between mb-3">
                <div className="flex items-center gap-3">
                  <Link to={`/users/${post.author.id}`}>
                    <UserAvatar name={post.author.name} profilePictureUrl={post.author.profilePictureUrl} size="md" />
                  </Link>
                  <div>
                    <div className="flex items-center gap-2">
                      <Link to={`/users/${post.author.id}`} className="text-sm font-medium text-foreground hover:text-primary transition-colors">{post.author.name}</Link>
                      <RoleBadge role={post.author.role} />
                    </div>
                    <p className="text-xs text-muted-foreground">{timeAgo(post.createdAt)}</p>
                  </div>
                </div>
                {user?.id === post.author.id && (
                  <div className="relative group">
                    <button className="p-1 rounded-lg hover:bg-muted text-muted-foreground transition-colors">
                      <MoreHorizontal className="h-4 w-4" />
                    </button>
                    <div className="absolute right-0 top-full mt-1 bg-card border border-border rounded-xl shadow-lg p-1 opacity-0 group-hover:opacity-100 pointer-events-none group-hover:pointer-events-auto transition-all z-10 w-36">
                      <button onClick={() => { setEditingId(post.id); setEditContent(post.content); }} className="w-full flex items-center gap-2 px-3 py-2 text-sm rounded-lg hover:bg-muted text-foreground"><Edit3 className="h-3.5 w-3.5" /> Edit</button>
                      <button onClick={() => setDeleteId(post.id)} className="w-full flex items-center gap-2 px-3 py-2 text-sm rounded-lg hover:bg-destructive/10 text-destructive"><Trash2 className="h-3.5 w-3.5" /> Delete</button>
                    </div>
                  </div>
                )}
              </div>

              {/* Content */}
              {editingId === post.id ? (
                <div className="mb-3">
                  <textarea value={editContent} onChange={(e) => setEditContent(e.target.value)} rows={3} className="w-full rounded-xl border border-border bg-background p-3 text-sm text-foreground focus:outline-none focus:ring-2 focus:ring-ring/50 resize-none" />
                  <div className="flex gap-2 mt-2">
                    <button onClick={() => handleEdit(post.id)} className="px-4 py-1.5 rounded-lg gradient-primary text-primary-foreground text-sm font-medium">Save</button>
                    <button onClick={() => setEditingId(null)} className="px-4 py-1.5 rounded-lg border border-border text-sm text-foreground">Cancel</button>
                  </div>
                </div>
              ) : (
                <p className="text-sm text-foreground mb-3 whitespace-pre-wrap">{post.content}</p>
              )}

              {/* Image */}
              {post.imageUrl && (
                <img src={getPostImageUrl(post.imageUrl)} alt="Post" className="w-full rounded-xl object-cover max-h-96 mb-3" />
              )}

              {/* Engagement */}
              <div className="flex items-center gap-6 pt-3 border-t border-border">
                <button onClick={() => handleLike(post)} className="flex items-center gap-1.5 text-sm transition-all active:scale-90">
                  <Heart className={`h-4 w-4 transition-all ${post.isLikedByCurrentUser ? 'fill-destructive text-destructive scale-110' : 'text-muted-foreground hover:text-destructive'}`} />
                  <span className={post.isLikedByCurrentUser ? 'text-destructive font-medium' : 'text-muted-foreground'}>{post.likeCount}</span>
                </button>
                <button onClick={() => toggleComments(post.id)} className="flex items-center gap-1.5 text-sm text-muted-foreground hover:text-primary transition-colors">
                  <MessageCircle className="h-4 w-4" /> {post.commentCount}
                </button>
                <button className="flex items-center gap-1.5 text-sm text-muted-foreground hover:text-primary transition-colors">
                  <Share2 className="h-4 w-4" />
                </button>
              </div>

              {/* Comments */}
              <AnimatePresence>
                {openComments === post.id && (
                  <motion.div initial={{ height: 0, opacity: 0 }} animate={{ height: 'auto', opacity: 1 }} exit={{ height: 0, opacity: 0 }} className="overflow-hidden">
                    <div className="pt-4 mt-3 border-t border-border space-y-3">
                      {loadingComments ? (
                        <div className="flex justify-center py-4"><Loader2 className="h-5 w-5 animate-spin text-muted-foreground" /></div>
                      ) : (
                        (comments[post.id] || []).map((c) => (
                          <div key={c.id} className="flex gap-2">
                            <UserAvatar name={c.author.name} profilePictureUrl={c.author.profilePictureUrl} size="xs" />
                            <div className="flex-1 bg-muted rounded-xl px-3 py-2">
                              <div className="flex items-center justify-between">
                                <span className="text-xs font-medium text-foreground">{c.author.name}</span>
                                <div className="flex items-center gap-2">
                                  <span className="text-xs text-muted-foreground">{timeAgo(c.createdAt)}</span>
                                  {user?.id === c.author.id && (
                                    <button onClick={() => handleDeleteComment(c.id, post.id)} className="text-muted-foreground hover:text-destructive"><X className="h-3 w-3" /></button>
                                  )}
                                </div>
                              </div>
                              <p className="text-sm text-foreground mt-0.5">{c.text}</p>
                            </div>
                          </div>
                        ))
                      )}
                      <div className="flex gap-2">
                        <UserAvatar name={user?.name || ''} profilePictureUrl={user?.profilePictureUrl} size="xs" />
                        <div className="flex-1 flex gap-2">
                          <input
                            value={commentText}
                            onChange={(e) => setCommentText(e.target.value)}
                            onKeyDown={(e) => e.key === 'Enter' && handleAddComment(post.id)}
                            placeholder="Add a comment..."
                            className="flex-1 bg-muted rounded-xl px-3 py-2 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-ring/50"
                          />
                          <button onClick={() => handleAddComment(post.id)} className="text-primary hover:text-primary/80"><Send className="h-4 w-4" /></button>
                        </div>
                      </div>
                    </div>
                  </motion.div>
                )}
              </AnimatePresence>
            </motion.div>
          ))}

          {hasMore && (
            <button
              onClick={() => { setLoadingMore(true); const next = page + 1; setPage(next); loadPosts(next); }}
              disabled={loadingMore}
              className="w-full py-3 rounded-xl border border-border text-sm font-medium text-foreground hover:bg-muted transition-colors"
            >
              {loadingMore ? <Loader2 className="h-4 w-4 animate-spin mx-auto" /> : 'Load More'}
            </button>
          )}
        </div>
      )}

      <ConfirmDialog open={!!deleteId} onClose={() => setDeleteId(null)} onConfirm={handleDelete} title="Delete Post" message="Are you sure you want to delete this post? This action cannot be undone." confirmLabel="Delete" loading={deleting} />
    </div>
  );
};

export default FeedPage;
