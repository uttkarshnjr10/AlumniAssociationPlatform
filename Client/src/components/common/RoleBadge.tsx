interface RoleBadgeProps {
  role: string;
  className?: string;
}

const roleStyles: Record<string, string> = {
  admin: 'bg-warning/10 text-warning',
  alumnus: 'bg-primary/10 text-primary',
  student: 'bg-success/10 text-success',
};

const roleLabels: Record<string, string> = {
  admin: 'Admin',
  alumnus: 'Alumni',
  student: 'Student',
};

const RoleBadge = ({ role, className = '' }: RoleBadgeProps) => {
  const style = roleStyles[role] || 'bg-muted text-muted-foreground';
  const label = roleLabels[role] || role;

  return (
    <span className={`inline-flex items-center text-xs font-medium px-2 py-0.5 rounded-full ${style} ${className}`}>
      {label}
    </span>
  );
};

export default RoleBadge;
