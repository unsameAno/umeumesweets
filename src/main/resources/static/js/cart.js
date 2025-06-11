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
