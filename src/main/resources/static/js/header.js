let currentLang = 'ko';

document.addEventListener("DOMContentLoaded", () => {
  // 텍스트 백업
  document.querySelectorAll('[data-translate]').forEach(el => {
    const original = el.innerText.trim();
    if (original.length > 0) {
      el.setAttribute('data-original', original);
    }
  });

  document.querySelectorAll('input[placeholder]').forEach(el => {
    el.setAttribute('data-original-placeholder', el.placeholder);
  });

  // Language ▼ 토글 동작
  const langToggle = document.querySelector('.language-toggle');
  if (langToggle) {
    langToggle.addEventListener('click', (e) => {
      e.preventDefault();
      const dropdown = langToggle.nextElementSibling;
      if (dropdown) {
        dropdown.style.display = dropdown.style.display === 'block' ? 'none' : 'block';
      }
    });
  }
});

async function toggleLanguage(targetLang) {
  if (targetLang === currentLang) return;

  const textElements = document.querySelectorAll('[data-translate]');
  const inputElements = document.querySelectorAll('input[placeholder][data-original-placeholder]');

  if (targetLang === 'ko') {
    textElements.forEach(el => {
      el.innerText = el.getAttribute('data-original');
    });

    inputElements.forEach(el => {
      el.placeholder = el.getAttribute('data-original-placeholder');
    });

  } else {
    const texts = Array.from(textElements).map(el => el.getAttribute('data-original'));

    const response = await fetch('/translate', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ texts, target: targetLang })
    });

    const translated = await response.json();

    textElements.forEach((el, i) => {
      el.innerText = translated[i];
    });

    inputElements.forEach(el => {
      el.placeholder = '検索';
    });
  }

  currentLang = targetLang;
}
