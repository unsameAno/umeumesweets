  function execDaumPostcode() {
    new daum.Postcode({
      oncomplete: function(data) {
        const addr = data.roadAddress || data.jibunAddress;
        document.querySelector("input[name='zipcode']").value = data.zonecode;
        document.querySelector("input[name='address']").value = addr;
        document.querySelector("input[name='addressDetail']").focus();
      }
    }).open();
  }