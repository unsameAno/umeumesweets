// static/js/register.js
function execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function (data) {
      document.getElementById("postcode").value = data.zonecode;
      document.getElementById("roadAddress").value = data.roadAddress;
    }
  }).open();
}

document.addEventListener("DOMContentLoaded", () => {
  document.getElementById("registerForm").addEventListener("submit", function (e) {
    const pw = document.getElementById("password").value;
    const pwConfirm = document.getElementById("confirmPassword").value;

    if (pw !== pwConfirm) {
      alert("비밀번호가 일치하지 않습니다.");
      e.preventDefault();
      return;
    }

    const p1 = document.querySelector("input[name='phone1']").value;
    const p2 = document.querySelector("input[name='phone2']").value;
    const p3 = document.querySelector("input[name='phone3']").value;

    document.getElementById("fullPhone").value = p1 + p2 + p3;
  });
});
