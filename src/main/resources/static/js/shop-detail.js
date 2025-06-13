window.onload = function () {
  if (typeof kakao === "undefined") {
    console.error("❌ Kakao API 로드 실패");
    return;
  }

  kakao.maps.load(function () {
    const mapElement = document.getElementById("map");
    if (!mapElement) {
      console.error("❌ map element 없음");
      return;
    }

    const address = mapElement.dataset.address;
    const map = new kakao.maps.Map(mapElement, {
      center: new kakao.maps.LatLng(37.5665, 126.978),
      level: 3,
    });

    const geocoder = new kakao.maps.services.Geocoder();
    geocoder.addressSearch(address, function (result, status) {
      if (status === kakao.maps.services.Status.OK) {
        const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
        new kakao.maps.Marker({ map: map, position: coords });
        map.setCenter(coords);
      } else {
        console.error("❌ 주소 검색 실패:", status);
      }
    });
  });
};

document.addEventListener("DOMContentLoaded", function () {
  const desc = document.querySelector("#descWrapper .description");
  const wrapper = document.getElementById("descWrapper");
  const btn = document.getElementById("btnMore");

  if (desc.scrollHeight > 120) {
    btn.style.display = "inline-block";
  }

  btn.addEventListener("click", function () {
    wrapper.classList.toggle("expanded");
    btn.textContent = wrapper.classList.contains("expanded")
      ? "접기"
      : "더보기";
  });
});
