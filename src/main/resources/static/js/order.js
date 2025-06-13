function execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function (data) {
      const addr = data.roadAddress || data.jibunAddress;
      document.querySelector("input[name='zipcode']").value = data.zonecode;
      document.querySelector("input[name='address']").value = addr;
      document.querySelector("input[name='addressDetail']").focus();
    },
  }).open();
}

function cancelOrder(orderId) {
  Swal.fire({
    title: "주문을 취소하시겠습니까?",
    icon: "warning",
    showCancelButton: true,
    confirmButtonText: "예",
    cancelButtonText: "아니오",
  }).then((result) => {
    if (result.isConfirmed) {
      fetch(`/order/${orderId}/cancel`, { method: "POST" }).then(() => {
        // 주문 DOM 제거 또는 상태 변경
        const target = document.querySelector(`#order-${orderId}`);
        if (target) {
          target.querySelector(".cancel-btn").style.display = "none";
          const badge = target.querySelector(".cancel-badge");
          if (badge) badge.style.display = "inline-block";
        }

        Swal.fire("취소 완료", "주문이 취소되었습니다.", "success");
      });
    }
  });
}
