<template>
  <div class="course-card">
    <div class="course-img">
      <img :src="`/api-course/course/${course.courseId}/thumbnail?t=${Date.now()}`" />
    </div>

    <div class="course-info">
      <div class="course-name">{{ course.courseName }}</div>
      <div class="course-meta">
        평균 별점: {{ course.avgRating }}
        | 조회수: {{ course.viewCnt }}
        | 거리 {{ course.distance_km }}
        | 난이도 {{ course.difficulty }}
        | 게시일 {{ course.createdAt }}
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  course: {
    type: Object,
    required: true
  }
})
</script>

<style scoped>
body {
  margin: 0;
  padding: 0;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI",
    sans-serif;
  background: #f0f0f0;
  display: flex;
  justify-content: center;
}

/* 지도 배경 느낌 */
.map-bg {
  position: absolute;
  inset: 0;
  background: repeating-linear-gradient(0deg,
      #f9f9f9,
      #f9f9f9 20px,
      #efefef 21px),
    repeating-linear-gradient(90deg,
      #f9f9f9,
      #f9f9f9 20px,
      #efefef 21px);
  opacity: 0.7;
  pointer-events: none;
}

.content {
  position: relative;
  padding: 12px 12px 80px;
}

.search-icon {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 16px;
  color: var(--orange);
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
  height: 360px;
  /* 기본 높이 */
  overflow-y: auto;
  transition: height 0.1s ease-out;
}

/* CourseCard.vue 스코프 스타일 */
.course-img {
  position: relative;
  background: var(--card-bg);
  width: 100%;           /* 가로 전체 사용 */
  aspect-ratio: 1 / 1;   /* 가로 세로 1:1 비율 고정 (현대 브라우저 표준) */
  height: auto;          /* 기존의 height: 120px 제거 */
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.course-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;     /* 이미지가 찌그러지지 않게 꽉 채움 */
}

/* 단순한 도형들로 목업 느낌 */
.shape-wrap {
  display: flex;
  gap: 6px;
  align-items: center;
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
  position: relative;
  /* 중요: 내부 요소를 배치하기 위한 기준점 */
  height: 20px;
  /* 슬라이더가 들어갈 공간 확보 */

}
</style>