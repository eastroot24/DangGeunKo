<template>
    <div >
        <div class="course-card">
                <div class="course-img">
                  <div class="shape-wrap">
                    <div class="shape-triangle"></div>
                    <div>
                      <div class="shape-circle"></div>
                      <div class="shape-square" style="margin-top: 4px"></div>
                    </div>
                  </div>
                  <div class="card-heart">♡</div>
                </div>
                <div class="course-info">
                  <div class="course-name">{{course.courseName}}</div>
                  <div class="course-meta">
                    평균 별점: {{ course.avgRating }} | 조회수: {{ course.viewCnt }} | 거리{{ course.distance_km }} | 난이도 {{ course.difficulty }}| 게시일{{ course.createdAt }}
                  </div>
                </div>
            </div>
    </div> 
</template>

<script setup>

const props = defineProps({
  course: Object,
})


</script>

<style scoped>
     :root {
      --orange: #ff7a00;
      --light-orange: #ffe4c4;
      --bg: #f7f7f7;
      --gray: #dddddd;
      --text-main: #222222;
      --text-sub: #888888;
      --card-bg: #f3f3f3;
      --nav-gray: #c7c7c7;
      --shadow-soft: 0 4px 12px rgba(0, 0, 0, 0.08);
      --radius-large: 24px;
    }

    * {
      box-sizing: border-box;
      -webkit-tap-highlight-color: transparent;
    }

    body {
      margin: 0;
      padding: 0;
      font-family: system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI",
        sans-serif;
      background: #f0f0f0;
      display: flex;
      justify-content: center;
    }

    /* 모바일 화면 프레임 느낌 */
    .app {
      position: relative;
      width: 390px;
      min-height: 100vh;
      background: #ffffff;
      overflow: hidden;
    }

    /* 지도 배경 느낌 */
    .map-bg {
      position: absolute;
      inset: 0;
      background: repeating-linear-gradient(
          0deg,
          #f9f9f9,
          #f9f9f9 20px,
          #efefef 21px
        ),
        repeating-linear-gradient(
          90deg,
          #f9f9f9,
          #f9f9f9 20px,
          #efefef 21px
        );
      opacity: 0.7;
      pointer-events: none;
    }

    .content {
      position: relative;
      padding: 12px 12px 80px;
    }

    /* 상단 검색 영역 */
    .top-search-row {
      display: flex;
      gap: 8px;
      align-items: center;
      margin-bottom: 12px;
    }

    .category-btn {
      position: relative;
      padding: 8px 10px;
      border-radius: 999px;
      border: none;
      background: var(--orange);
      color: #fff;
      font-size: 13px;
      font-weight: 600;
      display: flex;
      align-items: center;
      gap: 4px;
      cursor: pointer;
      box-shadow: var(--shadow-soft);
      flex-shrink: 0;
    }

    .category-btn span.caret {
      font-size: 10px;
      margin-top: 1px;
    }

    .category-dropdown {
      position: absolute;
      top: 110%;
      left: 0;
      width: 120px;
      background: #ffffff;
      border-radius: 14px;
      box-shadow: var(--shadow-soft);
      padding: 6px 0;
      display: none;
      z-index: 10;
    }

    .category-dropdown.open {
      display: block;
    }

    .category-dropdown button {
      width: 100%;
      padding: 6px 12px;
      border: none;
      background: transparent;
      text-align: left;
      font-size: 13px;
      cursor: pointer;
    }

    .category-dropdown button:hover {
      background: #f3f3f3;
    }

    .search-input-wrap {
      flex: 1;
      position: relative;
    }

    .search-input {
      width: 100%;
      padding: 9px 34px 9px 12px;
      border-radius: 999px;
      border: none;
      box-shadow: var(--shadow-soft);
      font-size: 13px;
      outline: none;
    }

    .search-icon {
      position: absolute;
      right: 10px;
      top: 50%;
      transform: translateY(-50%);
      font-size: 16px;
      color: var(--orange);
    }

    /* 메인 카드 패널 */
    .panel {
      margin-top: 4px;
      background: #fff;
      border-radius: 28px;
      box-shadow: var(--shadow-soft);
      padding: 18px 16px 12px;
      backdrop-filter: blur(10px);
    }

    .panel-title {
      font-size: 18px;
      font-weight: 700;
      margin-bottom: 14px;
    }

    /* 필터 버튼들 */
    .filter-row {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 10px;
      font-size: 13px;
    }

    .filter-buttons {
      display: flex;
      gap: 6px;
      flex: 1;
    }

    .pill-btn {
      padding: 6px 10px;
      border-radius: 999px;
      border: 1px solid #e4e4e4;
      background: #fafafa;
      font-size: 12px;
      display: inline-flex;
      align-items: center;
      gap: 4px;
      cursor: pointer;
      color: #555;
      white-space: nowrap;
    }

    .pill-btn.active {
      background: var(--orange);
      border-color: var(--orange);
      color: #fff;
    }

    .pill-btn .caret {
      font-size: 9px;
      margin-top: 1px;
    }

    .sort-wrap {
      position: relative;
      font-size: 12px;
    }

    .sort-btn {
      border: none;
      background: transparent;
      display: inline-flex;
      align-items: center;
      gap: 4px;
      cursor: pointer;
      color: #666;
      font-size: 12px;
    }

    .sort-dropdown {
      position: absolute;
      right: 0;
      top: 130%;
      width: 150px;
      background: #fff;
      border-radius: 14px;
      box-shadow: var(--shadow-soft);
      padding: 6px 0;
      display: none;
      z-index: 10;
    }

    .sort-dropdown.open {
      display: block;
    }

    .sort-dropdown button {
      width: 100%;
      border: none;
      background: transparent;
      text-align: left;
      padding: 6px 12px;
      font-size: 12px;
      cursor: pointer;
    }

    .sort-dropdown button:hover {
      background: #f5f5f5;
    }

    /* 거리 / 지역 / 난이도 옵션 영역 */
    .filter-options {
      margin-bottom: 6px;
      font-size: 11px;
    }

    .distance-control,
    .region-control,
    .difficulty-control {
      display: none;
      padding: 8px 10px;
      border-radius: 14px;
      background: #fafafa;
      border: 1px solid #ededed;
      margin-bottom: 6px;
    }

    .distance-top {
      display: flex;
      justify-content: space-between;
      margin-bottom: 6px;
      font-size: 11px;
      color: var(--text-sub);
    }

    .distance-slider-row {
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .distance-slider-row input[type="range"] {
      flex: 1;
    }

    .distance-btn {
      padding: 4px 6px;
      border-radius: 10px;
      border: 1px solid #e0e0e0;
      background: #fff;
      font-size: 10px;
      cursor: pointer;
    }

    .region-selects {
      display: flex;
      gap: 6px;
    }

    .region-selects select {
      flex: 1;
      padding: 6px 8px;
      border-radius: 10px;
      border: 1px solid #dddddd;
      font-size: 11px;
      background: #fff;
    }

    .difficulty-chips {
      display: flex;
      gap: 6px;
    }

    .difficulty-chip {
      border-radius: 999px;
      padding: 5px 10px;
      border: 1px solid #e0e0e0;
      background: #fff;
      font-size: 11px;
      cursor: pointer;
      color: #555;
    }

    .difficulty-chip.active {
      background: var(--orange);
      border-color: var(--orange);
      color: #fff;
    }

    /* 코스 리스트 패널 (드래그로 높이 조절) */
    .course-panel-wrapper {
      margin-top: 4px;
    }

    .drag-handle {
      width: 40%;
      height: 4px;
      margin: 0 auto 8px;
      border-radius: 999px;
      background: #e0e0e0;
      cursor: ns-resize;
    }

    .course-panel {
      background: #fff;
      border-radius: 20px;
      padding: 8px 2px 6px;
      height: 360px; /* 기본 높이 */
      overflow-y: auto;
      transition: height 0.1s ease-out;
    }

    .course-grid {
      display: grid;
      grid-template-columns: repeat(2, minmax(0, 1fr));
      gap: 10px;
      padding: 4px 10px 10px;
    }

    .course-card {
      background: #fff;
      border-radius: 16px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
      overflow: hidden;
      font-size: 11px;
    }

    .course-img {
      position: relative;
      background: var(--card-bg);
      height: 120px;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    /* 단순한 도형들로 목업 느낌 */
    .shape-wrap {
      display: flex;
      gap: 6px;
      align-items: center;
    }

    .shape-triangle,
    .shape-square,
    .shape-circle {
      background: #e0e0e0;
      opacity: 0.9;
    }

    .shape-triangle {
      width: 40px;
      height: 40px;
      clip-path: polygon(50% 0%, 0% 100%, 100% 100%);
    }

    .shape-square {
      width: 24px;
      height: 24px;
      border-radius: 6px;
    }

    .shape-circle {
      width: 24px;
      height: 24px;
      border-radius: 50%;
    }

    .card-heart {
      position: absolute;
      right: 8px;
      bottom: 8px;
      width: 24px;
      height: 24px;
      border-radius: 50%;
      background: rgba(255, 255, 255, 0.9);
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 14px;
      color: #bbbbbb;
      cursor: pointer;
      box-shadow: var(--shadow-soft);
    }

    .card-heart.active {
      color: var(--orange);
    }

    .course-info {
      padding: 6px 8px 8px;
    }

    .course-name {
      font-size: 11px;
      font-weight: 600;
      margin-bottom: 2px;
    }

    .course-meta {
      font-size: 10px;
      color: var(--text-sub);
      line-height: 1.4;
    }

    /* 플로팅 + 버튼 */
    .fab {
      position: fixed;
      right: calc(50% - 390px / 2 + 16px);
      bottom: 76px;
      width: 46px;
      height: 46px;
      border-radius: 50%;
      background: var(--orange);
      color: #fff;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 26px;
      border: none;
      cursor: pointer;
      box-shadow: 0 6px 14px rgba(0, 0, 0, 0.22);
      z-index: 20;
    }

    /* 하단 내브바 */
    .navbar {
      position: fixed;
      left: 50%;
      transform: translateX(-50%);
      bottom: 0;
      width: 390px;
      height: 60px;
      background: #ffffff;
      border-top: 1px solid #eeeeee;
      display: flex;
      justify-content: space-around;
      align-items: center;
      box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.05);
      font-size: 10px;
    }

    .nav-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      gap: 2px;
      color: var(--nav-gray);
      cursor: pointer;
    }

    .nav-item .icon {
      width: 18px;
      height: 18px;
      border-radius: 6px;
      border: 2px solid currentColor;
    }

    .nav-item.course .icon {
      border-radius: 4px;
    }

    .nav-item.active {
      color: var(--orange);
    }

    /* 스크롤바 살짝만 보이게 */
    .course-panel::-webkit-scrollbar {
      width: 4px;
    }

    .course-panel::-webkit-scrollbar-track {
      background: transparent;
    }

    .course-panel::-webkit-scrollbar-thumb {
      background: #e0e0e0;
      border-radius: 999px;
    }
    .range-slider-group {
    position: relative; /* 중요: 내부 요소를 배치하기 위한 기준점 */
    height: 20px; /* 슬라이더가 들어갈 공간 확보 */

}

/* ================================== */
/* 🛠️ 이중 범위 슬라이더 커스텀 스타일 수정 */
/* ================================== */

/* 컨테이너는 relative로 설정하여 내부 요소들의 absolute 기준점이 되도록 함 */
.distance-slider-row.range-slider-group {
    position: relative;
    /* height를 충분히 확보하여 thumb가 잘리지 않도록 합니다. */
    height: 36px; 
    display: block; /* input[type=range]가 absolute로 겹치도록 block으로 변경 */
    align-items: center;
    padding: 0 12px; /* thumb가 좌우로 완전히 나오도록 여백 추가 (thumb 너비의 절반) */
}

/* 회색 트랙 배경 (::before 사용) */
.distance-slider-row.range-slider-group::before {
    content: '';
    position: absolute;
    /* padding을 고려하여 left/right 조정 */
    left: 0px; 
    right: 0px;
    height: 8px; /* 트랙 높이 */
    top: 50%;
    transform: translateY(-50%);
    background: var(--gray); /* 회색 트랙 배경 */
    border-radius: 4px;
    z-index: 1; /* 채움 요소보다 아래 */
}

/* 채움 요소 스타일 */
.slider-range-fill {
    position: absolute;
    height: 8px; /* 트랙 높이 */
    /* padding을 고려하여 left/right 조정 */
    top: 50%;
    transform: translateY(-50%);
    background: var(--orange); /* 오렌지색 */
    border-radius: 4px;
    z-index: 2; /* 두 슬라이더 핸들 사이에 위치 */
    pointer-events: none; /* 클릭 방지 (핸들 조작을 위해) */
}

/* 기본 슬라이더 인풋 스타일 (포지션 설정 및 기본 스타일 제거) */
.range-slider-group input[type="range"] {
    position: absolute;
    width: calc(100% + 3px); /* 패딩을 고려하여 너비 조정 */
    top: 50%;
    left: -1px; /* 패딩만큼 좌측 이동 */
    transform: translateY(-50%);
    background: transparent;
    margin: 0;
    padding: 0;
    -webkit-appearance: none; /* WebKit 기본 스타일 제거 */
    pointer-events: none; /* 기본적으로 클릭 방지 (thumb만 허용) */
    z-index: 3; /* thumb가 채움/트랙 위에 오도록 */
}

/* WebKit/Blink (Chrome, Safari) Thumb 스타일 */
.range-slider-group input[type="range"]::-webkit-slider-thumb {
    -webkit-appearance: none;
    height: 24px; /* thumb 크기 통일 */
    width: 24px; /* thumb 크기 통일 */
    border-radius: 50%;
    background: white;
    border: 2px solid var(--orange); /* 오렌지색 테두리 추가 */
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
    cursor: grab;
    pointer-events: all; /* Thumb만 클릭 가능하도록 설정 */
    margin-top: -8px; /* 트랙(8px) 중앙에 오도록 조정: (24px/2 - 8px/2) - 8px = 12-4-8 = 0 이지만, 브라우저별 차이로 인해 조정이 필요. 보통 -8px ~ -12px 사이 */
    z-index: 4;
}

/* Firefox Thumb 스타일 */
.range-slider-group input[type="range"]::-moz-range-thumb {
    height: 24px;
    width: 24px;
    border-radius: 50%;
    background: white;
    border: 2px solid var(--orange);
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
    cursor: grab;
    pointer-events: all;
    /* Firefox는 margin-top 대신 vertical-align: middle 등으로 조정하지만, 여기서는 -moz-appearance: none;만으로도 위치가 잘 잡히도록 시도합니다. */
}

/* Firefox에서 기본 트랙/채움 스타일 제거 */
.range-slider-group input[type="range"]::-moz-range-track,
.range-slider-group input[type="range"]::-moz-range-progress {
    background: transparent;
}
  </style>