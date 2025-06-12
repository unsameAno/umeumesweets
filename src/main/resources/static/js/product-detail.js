document.addEventListener("DOMContentLoaded", function () {
  const buttons = document.querySelectorAll('.tab-button');
  const panels = document.querySelectorAll('.tab-panel');

  buttons.forEach(btn => {
    btn.addEventListener('click', () => {
      const target = btn.getAttribute('data-target');

      // 버튼 상태 변경
      buttons.forEach(b => b.classList.remove('active'));
      btn.classList.add('active');

      // 탭 내용 변경
      panels.forEach(panel => {
        panel.classList.remove('active');
        if (panel.id === target) {
          panel.classList.add('active');
        }
      });
    });
  });

  // 상세 설명 더보기 버튼
  const toggleBtn = document.getElementById('toggleBtn');
  const descWrapper = document.getElementById('descWrapper');
  if (toggleBtn) {
    toggleBtn.addEventListener('click', function () {
      descWrapper.classList.toggle('expanded');
      toggleBtn.textContent = descWrapper.classList.contains('expanded') ? '간단히' : '더보기';
    });
  }

  // 리뷰 삭제 버튼 처리
  const deleteButtons = document.querySelectorAll(".delete-review-btn");
  deleteButtons.forEach((btn) => {
    btn.addEventListener("click", function () {
      const reviewId = this.getAttribute("data-id");

      if (confirm("정말 이 리뷰를 삭제하시겠습니까?")) {
        fetch(`/reviews/${reviewId}`, {
          method: "DELETE"
        })
        .then((res) => {
          if (res.ok) {
            alert("삭제 완료되었습니다.");
            // 삭제한 리뷰 DOM에서 제거
            const reviewItem = btn.closest(".review-item");
            reviewItem.remove();

            // 리뷰가 전부 삭제되었는지 체크해서 안내문구 보여줌
            const reviewList = document.querySelectorAll(".review-item");
            if (reviewList.length === 0) {
                document.querySelector(".review-list").style.display = 'none';
                document.querySelector(".no-reviews").style.display = 'block';
            }
          } else {
            alert("삭제 실패! 다시 시도해주세요.");
          }
        });
      }
    });
  });
});


document.addEventListener("DOMContentLoaded", () => {
  const buyNowBtn = document.getElementById("buyNowBtn");

  if (buyNowBtn) {
    buyNowBtn.addEventListener("click", async () => {
      const productId = document.querySelector("input[name='productId']").value;
      const quantity = document.querySelector(".qty-select").value;

      try {
        const res = await fetch("/products/direct", {
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded",
          },
          body: new URLSearchParams({ productId, quantity }),
        });

        if (res.status === 401) {
          Swal.fire({
            icon: "warning",
            title: "로그인이 필요합니다",
            text: "상품 구매는 로그인 후 이용해주세요",
          });
          return;
        }

        // 🔥 중요: 응답 body에서 redirect URL 텍스트로 꺼냄
        const redirectUrl = await res.text();
        window.location.href = redirectUrl;

      } catch (err) {
        console.error("바로구매 실패", err);
        Swal.fire({
          icon: "error",
          title: "에러 발생",
          text: "바로구매 중 문제가 발생했습니다. 다시 시도해주세요."
        });
      }
    });
  }
});
