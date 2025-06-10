  document.addEventListener("DOMContentLoaded", function () {
    const deleteBtns = document.querySelectorAll(".comment-delete-btn[data-toggle-password]");

    deleteBtns.forEach(btn => {
      btn.addEventListener("click", function (e) {
        e.preventDefault();
        const target = btn.closest(".comment-item").querySelector(".comment-password-box");
        if (target) {
          target.classList.toggle("active");
        }
      });
    });
  });