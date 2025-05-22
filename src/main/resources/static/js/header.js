document.addEventListener("DOMContentLoaded", () => {
  const toggleBtn = document.getElementById("user-icon-toggle");
  const dropdown = document.getElementById("user-dropdown");

  if (toggleBtn && dropdown) {
    toggleBtn.addEventListener("click", (e) => {
      e.preventDefault();
      dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
    });

    // 다른 곳 클릭 시 닫기
    document.addEventListener("click", (e) => {
      if (!toggleBtn.contains(e.target) && !dropdown.contains(e.target)) {
        dropdown.style.display = "none";
      }
    });
  }
});
