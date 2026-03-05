import { LucideIcon } from 'lucide-react';

interface EmptyStateProps {
  icon: LucideIcon;
  title: string;
  description: string;
  action?: React.ReactNode;
}

const EmptyState = ({ icon: Icon, title, description, action }: EmptyStateProps) => (
  <div className="flex flex-col items-center justify-center py-16 px-4 text-center">
    <div className="h-16 w-16 rounded-2xl bg-primary/8 flex items-center justify-center mb-4">
      <Icon className="h-8 w-8 text-primary/60" />
    </div>
    <h3 className="font-display text-lg font-semibold text-foreground mb-1">{title}</h3>
    <p className="text-muted-foreground text-sm max-w-sm mb-6">{description}</p>
    {action}
  </div>
);

export default EmptyState;
