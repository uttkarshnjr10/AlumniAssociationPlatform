const COLORS = [
  'from-primary to-accent',
  'from-accent to-[hsl(330,81%,60%)]',
  'from-success to-[hsl(180,70%,45%)]',
  'from-warning to-[hsl(20,90%,55%)]',
  'from-[hsl(200,80%,55%)] to-primary',
  'from-[hsl(280,70%,60%)] to-accent',
];

export const getInitials = (name: string) => {
  const parts = name.trim().split(/\s+/);
  if (parts.length >= 2) return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
  return name.slice(0, 2).toUpperCase();
};

export const getAvatarColor = (name: string) => {
  let hash = 0;
  for (let i = 0; i < name.length; i++) hash = name.charCodeAt(i) + ((hash << 5) - hash);
  return COLORS[Math.abs(hash) % COLORS.length];
};
