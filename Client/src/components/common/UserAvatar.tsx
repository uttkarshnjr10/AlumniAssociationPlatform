import { getInitials, getAvatarColor } from '@/utils/avatar';
import { getProfileImageUrl } from '@/utils/images';

interface UserAvatarProps {
  name: string;
  profilePictureUrl?: string | null;
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl';
  className?: string;
}

const sizeMap = {
  xs: 'h-6 w-6 text-[10px]',
  sm: 'h-8 w-8 text-xs',
  md: 'h-10 w-10 text-sm',
  lg: 'h-16 w-16 text-lg',
  xl: 'h-24 w-24 text-2xl',
};

const UserAvatar = ({ name, profilePictureUrl, size = 'md', className = '' }: UserAvatarProps) => {
  const imageUrl = getProfileImageUrl(profilePictureUrl ?? null);
  const initials = getInitials(name);
  const color = getAvatarColor(name);

  return (
    <div className={`relative shrink-0 rounded-full ring-2 ring-card overflow-hidden ${sizeMap[size]} ${className}`}>
      {imageUrl ? (
        <img
          src={imageUrl}
          alt={name}
          className="h-full w-full object-cover"
          onError={(e) => {
            (e.target as HTMLImageElement).style.display = 'none';
            (e.target as HTMLImageElement).nextElementSibling?.classList.remove('hidden');
          }}
        />
      ) : null}
      <div className={`${imageUrl ? 'hidden' : ''} h-full w-full flex items-center justify-center bg-gradient-to-br ${color} text-white font-semibold`}>
        {initials}
      </div>
    </div>
  );
};

export default UserAvatar;
