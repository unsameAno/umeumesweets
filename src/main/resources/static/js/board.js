document.addEventListener("DOMContentLoaded", function () {
  // 댓글 삭제 버튼 (댓글 우측 ❌ 버튼)
  const deleteBtns = document.querySelectorAll(".comment-delete-btn[data-toggle-password]");

  deleteBtns.forEach(btn => {
    btn.addEventListener("click", function (e) {
      e.preventDefault();

      const parent = btn.closest(".comment-item");
      const pwdBox = parent.querySelector(".comment-password-box");
      const directForm = parent.querySelector(".comment-delete-form-direct");

      // 로그인 유저면 form 바로 submit
      if (directForm) {
        directForm.submit();
        return;
      }

      // 비회원이면 비번 입력폼 토글
      if (pwdBox) {
        pwdBox.classList.toggle("active");
      }
    });
  });

  // 비회원 게시글 수정/삭제 버튼 처리
  const guestEditBtn = document.getElementById('guest-edit-btn');
  const guestDeleteBtn = document.getElementById('guest-delete-btn');
  const guestEditForm = document.getElementById('guest-edit-form');
  const guestDeleteForm = document.getElementById('guest-delete-form');

  if (guestEditBtn && guestEditForm) {
    guestEditBtn.addEventListener('click', () => {
      guestEditForm.style.display = guestEditForm.style.display === 'none' ? 'block' : 'none';
    });
  }

  if (guestDeleteBtn && guestDeleteForm) {
    guestDeleteBtn.addEventListener('click', () => {
      guestDeleteForm.style.display = guestDeleteForm.style.display === 'none' ? 'block' : 'none';
    });
  }
});

function togglePasswordBox(button) {
  const form = button.closest('.comment-box').querySelector('.comment-password-form');
  if (form.style.display === 'none') {
    form.style.display = 'flex';
  } else {
    form.style.display = 'none';
  }
}
