import { useEffect, useRef } from 'react';

export function useAutoLock(timeoutMs: number, onLock: () => void) {
  const timerRef = useRef<number | null>(null);

  useEffect(() => {
    const resetTimer = () => {
      if (timerRef.current !== null) {
        window.clearTimeout(timerRef.current);
      }
      timerRef.current = window.setTimeout(onLock, timeoutMs);
    };

    const events = ['mousemove', 'keydown', 'click', 'touchstart', 'scroll'];
    
    events.forEach(event => {
      window.addEventListener(event, resetTimer);
    });

    resetTimer(); // Initialize

    return () => {
      if (timerRef.current !== null) {
        window.clearTimeout(timerRef.current);
      }
      events.forEach(event => {
        window.removeEventListener(event, resetTimer);
      });
    };
  }, [timeoutMs, onLock]);
}
