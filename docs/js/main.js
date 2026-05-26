/* ============================================
   STEALTH VAULT — Shared JavaScript
   ============================================ */

// ── Nav hamburger ──────────────────────────────
(function () {
  const ham = document.getElementById('hamburger');
  const menu = document.getElementById('mobileMenu');
  if (ham && menu) {
    ham.addEventListener('click', () => {
      menu.classList.toggle('open');
    });
    document.addEventListener('click', (e) => {
      if (!ham.contains(e.target) && !menu.contains(e.target)) {
        menu.classList.remove('open');
      }
    });
  }
})();

// ── Scroll reveal ──────────────────────────────
(function () {
  const els = document.querySelectorAll('.reveal');
  if (!els.length) return;
  const io = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          entry.target.classList.add('visible');
          io.unobserve(entry.target);
        }
      });
    },
    { threshold: 0.12 }
  );
  els.forEach((el) => io.observe(el));
})();

// ── Nav scroll shadow ──────────────────────────
(function () {
  const nav = document.querySelector('.nav');
  if (!nav) return;
  window.addEventListener('scroll', () => {
    nav.style.background =
      window.scrollY > 20 ? 'rgba(10,14,26,0.97)' : 'rgba(10,14,26,0.8)';
  });
})();

// ── Active nav link ────────────────────────────
(function () {
  const links = document.querySelectorAll('.nav-links a, .mobile-menu a');
  let path = location.pathname.split('/').pop();
  if (path === '' || path === 'index.html') {
    path = './';
  }
  links.forEach((a) => {
    const href = a.getAttribute('href');
    if (href === path) {
      a.classList.add('active');
    } else {
      a.classList.remove('active');
    }
  });
})();

// ── Copy to clipboard helper ───────────────────
function copyCode(btn) {
  const block = btn.closest('.code-wrap').querySelector('code');
  navigator.clipboard.writeText(block.innerText).then(() => {
    btn.textContent = '✓ Copied';
    btn.style.color = 'var(--green)';
    setTimeout(() => {
      btn.textContent = 'Copy';
      btn.style.color = '';
    }, 2000);
  });
}

// ── Counter animation ──────────────────────────
function animateCounters() {
  document.querySelectorAll('[data-count]').forEach((el) => {
    const target = +el.dataset.count;
    const suffix = el.dataset.suffix || '';
    let start = 0;
    const step = Math.ceil(target / 60);
    const timer = setInterval(() => {
      start = Math.min(start + step, target);
      el.textContent = start.toLocaleString() + suffix;
      if (start >= target) clearInterval(timer);
    }, 20);
  });
}

const statsSection = document.querySelector('.stats-section');
if (statsSection) {
  const io = new IntersectionObserver(
    (entries) => {
      if (entries[0].isIntersecting) {
        animateCounters();
        io.disconnect();
      }
    },
    { threshold: 0.5 }
  );
  io.observe(statsSection);
}

// ── FAQ accordion ──────────────────────────────
document.querySelectorAll('.faq-item').forEach((item) => {
  const q = item.querySelector('.faq-q');
  if (!q) return;
  q.addEventListener('click', () => {
    const open = item.classList.contains('open');
    document.querySelectorAll('.faq-item').forEach((i) => i.classList.remove('open'));
    if (!open) item.classList.add('open');
  });
});
