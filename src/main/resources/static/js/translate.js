// ✅ placeholder 매칭 맵 추가
const placeholderMap = {
  // 예시: 페이지 공용 검색어
  search: {
    ko: "디저트를 검색해보세요 🍡",
    ja: "デザートを検索してみてください 🍡",
  },
  password: {
    ko: "비밀번호",
    ja: "パスワード",
  },
  nickname: {
    ko: "닉네임",
    ja: "ニックネーム",
  },
  comment: {
    ko: "댓글을 입력하세요",
    ja: "コメントを入力してください",
  },
  review: {
    ko: "리뷰를 입력하세요",
    ja: "レビューを入力してください",
  },
  addressDetail: {
    ko: "상세주소",
    ja: "詳細住所",
  },
  address: {
    ko: "주소",
    ja: "住所",
  },
  phone: {
    ko: "연락처",
    ja: "連絡先",
  },
  receiverName: {
    ko: "수령인",
    ja: "受領人",
  },
  zipcode: {
    ko: "우편번호",
    ja: "郵便番号",
  },
};

async function toggleLanguage(targetLang, force = false) {
  if (!force && targetLang === currentLang) return;

  const textElements = document.querySelectorAll("[data-translate]");
  const inputElements = document.querySelectorAll(
    "input[placeholder][data-original-placeholder][data-placeholder-key], " +
      "textarea[placeholder][data-original-placeholder][data-placeholder-key]"
  );

  if (targetLang === "ko") {
    // 기존 처리 유지
    textElements.forEach((el) => {
      el.innerText = el.getAttribute("data-original");
    });
    inputElements.forEach((el) => {
      el.placeholder = el.getAttribute("data-original-placeholder");
    });
  } else {
    // 텍스트 번역 (기존 fetch 유지)
    const texts = Array.from(textElements).map((el) =>
      el.getAttribute("data-original")
    );
    const response = await fetch("/translate", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ texts, target: targetLang }),
    });
    const translated = await response.json();
    textElements.forEach((el, i) => {
      el.innerText = translated[i];
    });

    // ✅ placeholder는 맵에서 가져오기
    inputElements.forEach((el) => {
      const key = el.getAttribute("data-placeholder-key");
      const map = placeholderMap[key];
      if (map && map[targetLang]) {
        el.placeholder = map[targetLang];
      }
      // 맵에 없으면 기본 placeholder 유지하거나, 자동 fallback
    });
  }

  currentLang = targetLang;
  setCookie("lang", targetLang);
}
