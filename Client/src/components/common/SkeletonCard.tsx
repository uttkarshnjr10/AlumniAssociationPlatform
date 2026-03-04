const SkeletonCard = () => (
  <div className="bg-card rounded-2xl p-6 space-y-4 animate-pulse">
    <div className="flex items-center gap-3">
      <div className="h-10 w-10 rounded-full bg-muted" />
      <div className="space-y-2 flex-1">
        <div className="h-4 bg-muted rounded w-1/3" />
        <div className="h-3 bg-muted rounded w-1/4" />
      </div>
    </div>
    <div className="space-y-2">
      <div className="h-4 bg-muted rounded w-full" />
      <div className="h-4 bg-muted rounded w-5/6" />
      <div className="h-4 bg-muted rounded w-2/3" />
    </div>
    <div className="h-48 bg-muted rounded-xl" />
    <div className="flex gap-4">
      <div className="h-8 bg-muted rounded-lg w-20" />
      <div className="h-8 bg-muted rounded-lg w-20" />
    </div>
  </div>
);

export default SkeletonCard;
