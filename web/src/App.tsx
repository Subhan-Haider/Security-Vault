import React, { useState } from 'react';
import { DecoyCalculator } from './components/DecoyCalculator';
import { useAutoLock } from './hooks/useAutoLock';

function App() {
  const [isUnlocked, setIsUnlocked] = useState(false);

  // Auto lock after 3 minutes of inactivity
  useAutoLock(3 * 60 * 1000, () => {
    setIsUnlocked(false);
  });

  if (!isUnlocked) {
    return <DecoyCalculator onUnlock={() => setIsUnlocked(true)} />;
  }

  return (
    <div className="vault-app flex-center" style={{ minHeight: '100vh', flexDirection: 'column' }}>
      <header className="glass-panel" style={{ padding: '2rem', maxWidth: '800px', width: '90%', textAlign: 'center' }}>
        <h1 className="text-gradient" style={{ fontSize: '3rem', marginBottom: '1rem' }}>StealthVault</h1>
        <p style={{ color: 'var(--text-secondary)', marginBottom: '2rem' }}>
          Welcome to the secure web companion. Your files are encrypted and safe.
        </p>
        
        <button 
          onClick={() => setIsUnlocked(false)} 
          style={{ 
            padding: '0.75rem 1.5rem', 
            background: 'hsl(var(--accent-red))', 
            color: 'white', 
            borderRadius: 'var(--radius-md)',
            fontWeight: '600'
          }}
        >
          Lock Vault
        </button>
      </header>
    </div>
  );
}

export default App;
