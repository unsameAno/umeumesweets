document.addEventListener('DOMContentLoaded', () => {
  setupCarousel();
  setupSorting();
  setupCategoryButtons();
  setupDessertCardClick();
});

// -------------------------------
// 🔹 캐러셀 슬라이드 관련 로직
// -------------------------------
function setupCarousel() {
  const container = document.querySelector('#cafesContainer');
  if (!container) return;

  const cafeItems = container.querySelectorAll('.cafe-item');
  if (cafeItems.length === 0) return;

  let index = 1;
  let transitioning = false;

  const itemWidth = () => document.querySelector('.cafe-item').offsetWidth + 15;

  // 클론 추가
  const lastClone = cafeItems[cafeItems.length - 1].cloneNode(true);
  const firstClone = cafeItems[0].cloneNode(true);
  container.insertBefore(lastClone, cafeItems[0]);
  container.appendChild(firstClone);

  // 초기 위치
  container.style.transform = `translateX(-${index * itemWidth()}px)`;

  // 이동 함수
  const goToIndex = (i) => {
    if (transitioning) return;
    transitioning = true;
    container.style.transition = 'transform 0.4s ease';
    container.style.transform = `translateX(-${i * itemWidth()}px)`;
    index = i;
  };

  const jumpToIndex = (i) => {
    transitioning = false;
    container.style.transition = 'none';
    container.style.transform = `translateX(-${i * itemWidth()}px)`;
    index = i;
  };

  // 버튼 이벤트
  document.querySelector('.next-btn')?.addEventListener('click', () => goToIndex(index + 1));
  document.querySelector('.prev-btn')?.addEventListener('click', () => goToIndex(index - 1));

  // 슬라이드 끝 처리
  container.addEventListener('transitionend', () => {
    const itemsLength = container.querySelectorAll('.cafe-item').length;
    if (index === 0) jumpToIndex(itemsLength - 2);
    else if (index === itemsLength - 1) jumpToIndex(1);
    else transitioning = false;
  });

  // 윈도우 리사이즈 시 위치 조정
  window.addEventListener('resize', () => {
    if (!transitioning) {
      container.style.transition = 'none';
      container.style.transform = `translateX(-${index * itemWidth()}px)`;
    }
  });
}

// -------------------------------
// 🔹 정렬 드롭다운 이벤트
// -------------------------------
function setupSorting() {
  const sortSelect = document.getElementById('sortSelect');
  if (!sortSelect) return;

  sortSelect.addEventListener('change', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const category = urlParams.get('category') || '';
    const sortOption = sortSelect.value;
    window.location.href = `/products?category=${encodeURIComponent(category)}&sort=${sortOption}`;
  });
}

// -------------------------------
// 🔹 카테고리 버튼 필터링
// -------------------------------
function setupCategoryButtons() {
  document.querySelectorAll('.category-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      const category = btn.getAttribute('data-category');
      const sortOption = document.getElementById('sortSelect')?.value || 'created_desc';
      window.location.href = `/products?category=${encodeURIComponent(category)}&sort=${sortOption}`;
    });
  });
}

// -------------------------------
// 🔹 디저트 카드 클릭 → 상세 페이지
// -------------------------------
function setupDessertCardClick() {
  document.querySelectorAll('.dessert-card').forEach(card => {
    const id = card.getAttribute('data-id');
    if (id) {
      card.style.cursor = 'pointer';
      card.addEventListener('click', () => {
        window.location.href = `/products/${id}`;
      });
    }
  });

  // 로그인 후 리뷰 작성 안내
  document.addEventListener("DOMContentLoaded", function () {
    const params = new URLSearchParams(window.location.search);
    const message = params.get("message");

    console.log("Parsed message:", message); // 디버깅 추가

    if (message === "login_required") {
      alert("리뷰 작성은 로그인 후 이용 가능합니다.");
    }
  });

}
