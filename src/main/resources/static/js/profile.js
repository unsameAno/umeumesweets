document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("profileForm");

  if (!form) return;

  form.addEventListener("submit", (e) => {
    // 🔐 현재 비밀번호 확인
    const currentPassword = prompt("현재 비밀번호를 입력해주세요:");
    const actualPassword = form.getAttribute("data-current-password");

    if (currentPassword !== actualPassword) {
      alert("현재 비밀번호가 일치하지 않습니다.");
      e.preventDefault(); // 제출 중단
      return;
    }

    // 📱 전화번호 병합
    const phone1 = document.querySelector("input[name='phone1']").value.trim();
    const phone2 = document.querySelector("input[name='phone2']").value.trim();
    const phone3 = document.querySelector("input[name='phone3']").value.trim();
    const fullPhone = `${phone1}-${phone2}-${phone3}`;
    document.getElementById("fullPhone").value = fullPhone;
  });
});

// 📫 주소 검색 함수
function execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function (data) {
      document.getElementById("postcode").value = data.zonecode;
      document.getElementById("roadAddress").value = data.roadAddress;
    }
  }).open();
}
