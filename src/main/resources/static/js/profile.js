document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("profileForm");

  if (!form) return;

  form.addEventListener("submit", (e) => {
    const currentPassword = prompt("현재 비밀번호를 입력해주세요:");
    const actualPassword = form.getAttribute("data-current-password");

    if (currentPassword === null) {
      e.preventDefault();
      return;
    }

    if (currentPassword !== actualPassword) {
      alert("현재 비밀번호가 일치하지 않습니다.");
      e.preventDefault();
      return;
    }

    const name = document.getElementById("name").value.trim();
    const email = document.getElementById("email").value.trim();
    const phone = document.getElementById("phone").value.trim();
    const roadAddress = document.getElementById("roadAddress").value.trim();
    const detailAddress = document.getElementById("detailAddress").value.trim();

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const phoneRegex = /^010-\d{4}-\d{4}$/;

    if (name === "") {
      alert("이름을 입력해주세요.");
      e.preventDefault();
      return;
    }

    if (!emailRegex.test(email)) {
      alert("올바른 이메일을 입력해주세요.");
      e.preventDefault();
      return;
    }

    if (!phoneRegex.test(phone)) {
      alert("전화번호는 010-1234-5678 형식으로 입력해주세요.");
      e.preventDefault();
      return;
    }

    if (roadAddress === "" || detailAddress === "") {
      alert("주소와 상세주소를 모두 입력해주세요.");
      e.preventDefault();
      return;
    }
  });
});

function execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function (data) {
      document.getElementById("postcode").value = data.zonecode;
      document.getElementById("roadAddress").value = data.roadAddress;
    }
  }).open();
}

function confirmDelete() {
  const password = prompt("회원 탈퇴를 위해 비밀번호를 입력해주세요:");
  const actualPassword = document.getElementById("profileForm").getAttribute("data-current-password");

  if (password === null) {
    return;
  }

  if (password !== actualPassword) {
    alert("비밀번호가 일치하지 않습니다.");
    return;
  }

  if (confirm("정말 탈퇴하시겠습니까? 탈퇴하면 계정 정보가 삭제됩니다.")) {
    location.href = "/user/delete";
  }
}
