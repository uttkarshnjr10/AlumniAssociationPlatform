import { useState } from 'react';
import { motion } from 'framer-motion';
import { processDonation } from '@/services/donation';
import { Heart, CreditCard, Building, Smartphone, CheckCircle, Loader2 } from 'lucide-react';

const amounts = [10, 25, 50, 100, 250, 500];
const methods = [
  { id: 'credit_card', label: 'Credit Card', icon: CreditCard },
  { id: 'paypal', label: 'PayPal', icon: Smartphone },
  { id: 'bank_transfer', label: 'Bank Transfer', icon: Building },
];

const DonatePage = () => {
  const [selected, setSelected] = useState<number | null>(null);
  const [custom, setCustom] = useState('');
  const [useCustom, setUseCustom] = useState(false);
  const [message, setMessage] = useState('');
  const [method, setMethod] = useState('credit_card');
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);

  const amount = useCustom ? Number(custom) : selected;

  const handleDonate = async () => {
    if (!amount || amount <= 0) return;
    setLoading(true);
    try {
      await processDonation({ amount, message: message || undefined, paymentMethod: method });
      setSuccess(true);
    } catch { } finally { setLoading(false); }
  };

  if (success) {
    return (
      <motion.div initial={{ opacity: 0, scale: 0.9 }} animate={{ opacity: 1, scale: 1 }} className="max-w-md mx-auto text-center py-20">
        <div className="h-20 w-20 rounded-full bg-success/10 flex items-center justify-center mx-auto mb-6">
          <CheckCircle className="h-10 w-10 text-success" />
        </div>
        <h2 className="font-display text-2xl font-bold text-foreground mb-2">Thank You! 🎉</h2>
        <p className="text-muted-foreground mb-6">Your donation of ${amount} has been received.</p>
        <button onClick={() => { setSuccess(false); setSelected(null); setCustom(''); setMessage(''); }} className="px-6 py-2.5 rounded-xl gradient-primary text-primary-foreground text-sm font-medium">Donate Again</button>
      </motion.div>
    );
  }

  return (
    <div className="max-w-lg mx-auto">
      <div className="bg-card rounded-2xl shadow-[0_1px_3px_rgba(0,0,0,0.04)] p-6 md:p-8">
        <div className="text-center mb-8">
          <div className="h-14 w-14 rounded-2xl bg-primary/8 flex items-center justify-center mx-auto mb-4"><Heart className="h-7 w-7 text-primary" /></div>
          <h1 className="font-display text-2xl font-bold text-foreground">Support Your Alumni Network</h1>
          <p className="text-muted-foreground text-sm mt-1">Every contribution helps strengthen our community</p>
        </div>

        <div className="grid grid-cols-3 gap-3 mb-4">
          {amounts.map((a) => (
            <button
              key={a}
              onClick={() => { setSelected(a); setUseCustom(false); }}
              className={`py-3 rounded-xl text-sm font-semibold transition-all active:scale-95 border ${!useCustom && selected === a ? 'border-primary bg-primary/8 text-primary' : 'border-border text-foreground hover:border-primary/40'}`}
            >
              ${a}
            </button>
          ))}
        </div>

        <div className="mb-6">
          <button onClick={() => { setUseCustom(true); setSelected(null); }} className={`text-sm font-medium mb-2 ${useCustom ? 'text-primary' : 'text-muted-foreground'}`}>Custom amount</button>
          {useCustom && (
            <div className="relative">
              <span className="absolute left-4 top-1/2 -translate-y-1/2 text-muted-foreground font-medium">$</span>
              <input type="number" value={custom} onChange={(e) => setCustom(e.target.value)} placeholder="0.00" min="1" className="w-full pl-8 pr-4 py-3 rounded-xl border border-border bg-background text-foreground text-sm focus:outline-none focus:ring-2 focus:ring-ring/50 focus:border-primary" />
            </div>
          )}
        </div>

        <div className="mb-6">
          <label className="block text-sm font-medium text-foreground mb-1.5">Message (optional)</label>
          <textarea value={message} onChange={(e) => setMessage(e.target.value)} placeholder="Leave a message..." rows={2} className="w-full px-4 py-3 rounded-xl border border-border bg-background text-foreground text-sm placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-ring/50 focus:border-primary resize-none" />
        </div>

        <div className="mb-6">
          <label className="block text-sm font-medium text-foreground mb-2">Payment Method</label>
          <div className="grid grid-cols-3 gap-3">
            {methods.map(({ id, label, icon: Icon }) => (
              <button
                key={id}
                onClick={() => setMethod(id)}
                className={`flex flex-col items-center gap-1.5 py-3 rounded-xl text-xs font-medium border transition-all ${method === id ? 'border-primary bg-primary/8 text-primary' : 'border-border text-muted-foreground hover:border-primary/40'}`}
              >
                <Icon className="h-5 w-5" />
                {label}
              </button>
            ))}
          </div>
        </div>

        {amount && amount > 0 && (
          <div className="text-center mb-4">
            <p className="text-sm text-muted-foreground">Total</p>
            <p className="font-display text-3xl font-bold text-foreground">${amount}</p>
          </div>
        )}

        <button
          onClick={handleDonate}
          disabled={!amount || amount <= 0 || loading}
          className="w-full py-3 rounded-xl gradient-primary text-primary-foreground font-medium text-sm hover:opacity-90 active:scale-[0.98] transition-all disabled:opacity-50 flex items-center justify-center gap-2"
        >
          {loading ? <><Loader2 className="h-4 w-4 animate-spin" /> Processing...</> : <><Heart className="h-4 w-4" /> Donate {amount ? `$${amount}` : ''}</>}
        </button>
      </div>
    </div>
  );
};

export default DonatePage;
