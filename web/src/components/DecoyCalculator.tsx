import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Clock, CheckCircle2, ChevronDown, Delete } from 'lucide-react';
import './DecoyCalculator.css';

interface DecoyCalculatorProps {
  onUnlock: () => void;
}

const SECRET_PIN = '1337';

const formatNumber = (numStr: string) => {
  if (numStr === 'Error' || numStr === 'Infinity') return numStr;
  const parts = numStr.split('.');
  const intPart = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  return parts.length > 1 ? `${intPart}.${parts[1]}` : intPart;
};

export function DecoyCalculator({ onUnlock }: DecoyCalculatorProps) {
  const [display, setDisplay] = useState('0');
  const [expression, setExpression] = useState('');
  const [pinBuffer, setPinBuffer] = useState('');
  const [memory, setMemory] = useState(0);
  const [history, setHistory] = useState<{expr: string, res: string}[]>([]);
  const [showHistory, setShowHistory] = useState(false);
  const [isScientific, setIsScientific] = useState(false);
  const [copied, setCopied] = useState(false);

  const hapticFeedback = (duration: number = 10) => {
    if (navigator.vibrate) {
      navigator.vibrate(duration);
    }
  };

  const handleInput = (val: string) => {
    hapticFeedback(10);
    
    setPinBuffer(prev => {
      const next = prev + val;
      if (next === SECRET_PIN) {
        setTimeout(onUnlock, 150);
      }
      return next;
    });

    if (display === '0' || display === 'Error') {
      setDisplay(val);
    } else {
      if (display.replace(/,/g, '').length < 15) {
        setDisplay(prev => prev + val);
      }
    }
  };

  const handleOp = (op: string) => {
    hapticFeedback(15);
    setPinBuffer('');
    if (display !== 'Error') {
      if (expression && !expression.includes('=')) {
        // Evaluate previous op
        handleEquals(false);
        setExpression(`${display} ${op}`);
      } else {
        setExpression(`${display} ${op}`);
        setDisplay('0');
      }
    }
  };

  const handleEquals = (logHistory = true) => {
    hapticFeedback(25);
    
    if (pinBuffer === SECRET_PIN || display === SECRET_PIN) {
      onUnlock();
      return;
    }

    try {
      if (!expression || expression.includes('=')) return;
      const fullExpr = `${expression} ${display}`;
      const sanitized = fullExpr.replace(/×/g, '*').replace(/÷/g, '/').replace(/−/g, '-');
      // Safe eval equivalent for a basic decoy
      // eslint-disable-next-line no-new-func
      let result = new Function('return ' + sanitized)();
      
      // Handle floating point precision issues
      result = Math.round(result * 10000000000) / 10000000000;
      
      const resStr = String(result);
      setDisplay(resStr);
      setExpression(`${fullExpr} =`);
      if (logHistory) {
        setHistory(prev => [{expr: fullExpr, res: resStr}, ...prev]);
      }
    } catch (e) {
      setDisplay('Error');
    }
    setPinBuffer('');
  };

  const handleClear = () => {
    hapticFeedback(15);
    if (display === '0') {
      setExpression('');
    }
    setDisplay('0');
    setPinBuffer('');
  };

  const handleDelete = () => {
    hapticFeedback(5);
    if (display.length > 1 && display !== 'Error') {
      setDisplay(display.slice(0, -1));
    } else {
      setDisplay('0');
    }
    setPinBuffer(prev => prev.slice(0, -1));
  };

  const copyToClipboard = async () => {
    hapticFeedback([10, 30, 10]);
    try {
      await navigator.clipboard.writeText(display);
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    } catch (err) {
      console.error('Failed to copy');
    }
  };

  const handleMemory = (type: string) => {
    hapticFeedback(15);
    const curr = parseFloat(display);
    if (isNaN(curr)) return;
    
    switch (type) {
      case 'M+': setMemory(m => m + curr); setDisplay('0'); break;
      case 'M-': setMemory(m => m - curr); setDisplay('0'); break;
      case 'MR': setDisplay(String(memory)); break;
      case 'MC': setMemory(0); break;
    }
  };

  // Swipe gesture for backspace
  const onDragEnd = (event: any, info: any) => {
    if (info.offset.x < -30 || info.offset.x > 30) {
      handleDelete();
    }
  };

  return (
    <div className="calc-wrapper">
      <div className="calc-body">
        
        <div className="calc-header">
          <button className="icon-btn" onClick={() => setShowHistory(!showHistory)}>
            <Clock size={20} />
          </button>
          <button className="icon-btn" onClick={() => setIsScientific(!isScientific)}>
            <ChevronDown size={20} style={{ transform: isScientific ? 'rotate(180deg)' : 'none', transition: '0.3s' }} />
          </button>
        </div>

        <motion.div 
          className="calc-display-container"
          drag="x"
          dragConstraints={{ left: 0, right: 0 }}
          onDragEnd={onDragEnd}
          onDoubleClick={copyToClipboard}
        >
          <div className="calc-expression">{expression}</div>
          <div className="calc-display" style={{ fontSize: display.length > 10 ? '3rem' : '4.5rem' }}>
            {formatNumber(display)}
          </div>
          <AnimatePresence>
            {copied && (
              <motion.div 
                initial={{ opacity: 0, y: 10 }} 
                animate={{ opacity: 1, y: 0 }} 
                exit={{ opacity: 0 }}
                className="copy-toast"
              >
                <CheckCircle2 size={16} /> Copied
              </motion.div>
            )}
          </AnimatePresence>
        </motion.div>

        <AnimatePresence>
          {showHistory && (
            <motion.div 
              initial={{ opacity: 0, height: 0 }} 
              animate={{ opacity: 1, height: '180px' }} 
              exit={{ opacity: 0, height: 0 }}
              className="calc-history"
            >
              {history.length === 0 ? (
                <div className="history-empty">No History</div>
              ) : (
                history.map((item, idx) => (
                  <div key={idx} className="history-item" onClick={() => setDisplay(item.res)}>
                    <div className="history-expr">{item.expr}</div>
                    <div className="history-res">={item.res}</div>
                  </div>
                ))
              )}
            </motion.div>
          )}
        </AnimatePresence>

        <AnimatePresence>
          {isScientific && (
            <motion.div 
              initial={{ opacity: 0, height: 0 }} 
              animate={{ opacity: 1, height: 'auto' }} 
              exit={{ opacity: 0, height: 0 }}
              className="calc-scientific-pad"
            >
              <button onClick={() => handleMemory('MC')} className="calc-btn calc-btn-mem">MC</button>
              <button onClick={() => handleMemory('MR')} className="calc-btn calc-btn-mem">MR</button>
              <button onClick={() => handleMemory('M-')} className="calc-btn calc-btn-mem">M-</button>
              <button onClick={() => handleMemory('M+')} className="calc-btn calc-btn-mem">M+</button>
              
              <button onClick={() => setDisplay('3.14159265')} className="calc-btn calc-btn-sci">π</button>
              <button onClick={() => setDisplay('2.71828182')} className="calc-btn calc-btn-sci">e</button>
              <button onClick={() => {
                try { setDisplay(String(Math.sqrt(parseFloat(display)))) } catch (e) {}
              }} className="calc-btn calc-btn-sci">√x</button>
              <button onClick={() => {
                try { setDisplay(String(Math.pow(parseFloat(display), 2))) } catch (e) {}
              }} className="calc-btn calc-btn-sci">x²</button>
            </motion.div>
          )}
        </AnimatePresence>

        <div className="calc-pad">
          <button onClick={handleClear} className="calc-btn calc-btn-action">{display === '0' ? 'AC' : 'C'}</button>
          <button onClick={() => setDisplay(String(parseFloat(display) * -1))} className="calc-btn calc-btn-action">±</button>
          <button onClick={() => setDisplay(String(parseFloat(display) / 100))} className="calc-btn calc-btn-action">%</button>
          <button onClick={() => handleOp('÷')} className="calc-btn calc-btn-op">÷</button>
          
          <button onClick={() => handleInput('7')} className="calc-btn">7</button>
          <button onClick={() => handleInput('8')} className="calc-btn">8</button>
          <button onClick={() => handleInput('9')} className="calc-btn">9</button>
          <button onClick={() => handleOp('×')} className="calc-btn calc-btn-op">×</button>
          
          <button onClick={() => handleInput('4')} className="calc-btn">4</button>
          <button onClick={() => handleInput('5')} className="calc-btn">5</button>
          <button onClick={() => handleInput('6')} className="calc-btn">6</button>
          <button onClick={() => handleOp('−')} className="calc-btn calc-btn-op">−</button>
          
          <button onClick={() => handleInput('1')} className="calc-btn">1</button>
          <button onClick={() => handleInput('2')} className="calc-btn">2</button>
          <button onClick={() => handleInput('3')} className="calc-btn">3</button>
          <button onClick={() => handleOp('+')} className="calc-btn calc-btn-op">+</button>
          
          <button onClick={() => handleInput('0')} className="calc-btn calc-btn-zero">0</button>
          <button onClick={() => handleInput('.')} className="calc-btn">.</button>
          <button onClick={() => handleEquals(true)} className="calc-btn calc-btn-op">=</button>
        </div>
      </div>
    </div>
  );
}
