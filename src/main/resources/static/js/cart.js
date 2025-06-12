document.addEventListener('DOMContentLoaded', function () {
  const checkboxes = document.querySelectorAll('.cart-checkbox');
  const totalPriceEl = document.getElementById('totalPrice');
  const selectAllBtn = document.getElementById('selectAllBtn');

  function updateTotalPrice() {
    let total = 0;
    document.querySelectorAll('.cart-item').forEach(item => {
      const checkbox = item.querySelector('.cart-checkbox');
      const priceText = item.querySelector('.item-price').textContent;
      if (checkbox.checked) {
        const price = parseInt(priceText.replace(/[^0-9]/g, ''));
        total += price;
      }
    });
    totalPriceEl.textContent = total.toLocaleString() + '원';
  }

  checkboxes.forEach(cb => cb.addEventListener('change', updateTotalPrice));
  updateTotalPrice();

  selectAllBtn.addEventListener('click', () => {
    const allChecked = [...checkboxes].every(cb => cb.checked);
    checkboxes.forEach(cb => cb.checked = !allChecked);
    updateTotalPrice();
  });
});

document.querySelectorAll('.delete-btn').forEach(button => {
  button.addEventListener('click', () => {
    const cartItemId = button.getAttribute('data-id');

    fetch('/cart/delete', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: `cartItemId=${cartItemId}`
    })
    .then(res => {
      if (res.redirected) {
        window.location.href = res.url; // 서버가 redirect:/cart 보내면 새로고침
      } else {
        location.reload();
      }
    })
    .catch(err => {
      alert("삭제 중 오류가 발생했습니다.");
    });
  });
});


document.addEventListener("DOMContentLoaded", function () {
  const deleteBtn = document.getElementById("deleteSelectedBtn");
  if (deleteBtn) {
    deleteBtn.addEventListener("click", function () {
      const checked = document.querySelectorAll('.cart-checkbox:checked');
      if (checked.length === 0) {
        alert('삭제할 항목을 선택해주세요!');
        return;
      }

      const ids = Array.from(checked).map(cb => cb.value);
      document.getElementById("cartItemIdsInput").value = ids.join(',');
      document.getElementById("bulkDeleteForm").submit();
    });
  }
});

    document.getElementById("checkoutForm").addEventListener("submit", function (e) {
    const checked = [...document.querySelectorAll('.cart-checkbox:checked')]; 
    const ids = checked.map(cb => cb.value).join(",");
    document.getElementById("selectedCartIds").value = ids;
  });