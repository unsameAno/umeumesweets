// ✅ 초기 언어 설정 (기본은 'ko')
let currentLang = getCookie("lang") || "ko";

// ✅ 쿠키에서 언어 읽기
function getCookie(name) {
  const cookies = document.cookie.split(";");
  for (let c of cookies) {
    const [key, val] = c.trim().split("=");
    if (key === name) return val;
  }
  return null;
}

// ✅ 쿠키에 언어 저장
function setCookie(name, value, days = 1) {
  const expires = new Date(Date.now() + days * 86400000).toUTCString();
  document.cookie = `${name}=${value}; path=/; expires=${expires}`;
}

document.addEventListener("DOMContentLoaded", async () => {
  // 텍스트 백업
  document.querySelectorAll("[data-translate]").forEach((el) => {
    const original = el.innerText.trim();
    if (original.length > 0) {
      el.setAttribute("data-original", original);
    }
  });

  document.querySelectorAll("input[placeholder]").forEach((el) => {
    el.setAttribute("data-original-placeholder", el.placeholder);
  });

  // ✅ 현재 언어 적용
  if (currentLang !== "ko") {
    await toggleLanguage(currentLang, true);
  }

  // Language ▼ 토글 동작
  const langToggle = document.querySelector(".language-toggle");
  if (langToggle) {
    langToggle.addEventListener("click", (e) => {
      e.preventDefault();
      const dropdown = langToggle.nextElementSibling;
      if (dropdown) {
        dropdown.style.display =
          dropdown.style.display === "block" ? "none" : "block";
      }
    });
  }
});

// 회원정보 드롭다운 토글
document.addEventListener("DOMContentLoaded", function () {
  const toggleBtn = document.getElementById("user-icon-toggle");
  const dropdown = document.getElementById("user-dropdown");

  // 요소가 모두 존재할 때만 이벤트 설정
  if (toggleBtn && dropdown) {
    toggleBtn.addEventListener("click", function (e) {
      e.preventDefault();
      const isOpen = dropdown.style.display === "block";
      dropdown.style.display = isOpen ? "none" : "block";
    });

    // 바깥 클릭 시 드롭다운 닫기
    document.addEventListener("click", function (e) {
      const isClickInside =
        toggleBtn.contains(e.target) || dropdown.contains(e.target);
      if (!isClickInside) {
        dropdown.style.display = "none";
      }
    });
  }
});
