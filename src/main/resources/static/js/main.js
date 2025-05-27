  document.addEventListener('DOMContentLoaded', function () {
    const container = document.querySelector('#cafesContainer');
    const cafeItems = container.querySelectorAll('.cafe-item');
    const itemWidth = cafeItems[0].offsetWidth + 15;

    let index = 1; // 실제 첫 번째 아이템 위치 (0은 복제된 마지막 아이템)
    let transitioning = false;

    // 초기 위치 (첫 번째 진짜 아이템)
    container.style.transform = `translateX(-${index * itemWidth}px)`;
    
    // 마지막 카페 클론
    const lastClone = cafeItems[cafeItems.length - 1].cloneNode(true);
    container.insertBefore(lastClone, cafeItems[0]);

    // 첫 번째 카페 클론
    const firstClone = cafeItems[0].cloneNode(true);
    container.appendChild(firstClone);

    function getItemWidth() {
        const currentItem = document.querySelector('.cafe-item');
        return currentItem.offsetWidth + 15;
    }

    function goToIndex(i) {
        transitioning = true;
        const itemWidth = getItemWidth(); // 📌 항상 최신 값으로 계산
        container.style.transition = 'transform 0.4s ease';
        container.style.transform = `translateX(-${i * itemWidth}px)`;
        index = i;
    }

    function jumpToIndex(i) {
        transitioning = false;
        const itemWidth = getItemWidth(); // 📌 항상 최신 값으로 계산
        container.style.transition = 'none';
        container.style.transform = `translateX(-${i * itemWidth}px)`;
        index = i;
    }


    document.querySelector('.next-btn').addEventListener('click', function () {
      if (transitioning) return;
      goToIndex(index + 1);
    });

    document.querySelector('.prev-btn').addEventListener('click', function () {
      if (transitioning) return;
      goToIndex(index - 1);
    });

    container.addEventListener('transitionend', function () {
      const itemsLength = cafeItems.length;
      if (index === 0) {
        jumpToIndex(itemsLength - 2); // 진짜 마지막 아이템
      } else if (index === itemsLength - 1) {
        jumpToIndex(1); // 진짜 첫 번째 아이템
      } else {
        transitioning = false;
      }
    });

    window.addEventListener('resize', () => {
        if (!transitioning) {
            const itemWidth = getItemWidth();
            container.style.transition = 'none';
            container.style.transform = `translateX(-${index * itemWidth}px)`;
        }
    });

  });