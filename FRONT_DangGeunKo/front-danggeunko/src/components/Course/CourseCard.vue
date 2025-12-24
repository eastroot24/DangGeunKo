<template>
  <div class="course-card">
    <div class="course-img">
      <img :src="`/api-course/course/${course.courseId}/thumbnail?t=${Date.now()}`" />
    </div>

    <div class="course-info">
      <div class="course-name">{{ course.courseName }}</div>
      <div class="course-meta">
        평균 별점: {{ course.avgRating? course.avgRating : "등록된 별점이 없습니다." }}
        | 조회수: {{ course.viewCnt }}
        | <br>거리 {{ course.distanceKm }} km
        | 난이도 {{ course.difficulty }}
        | 게시일 {{ course.createdAt.slice(0,10) }}
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

.course-info {
  padding: 1rem;
}

.course-name {
  text-align: left;
}

.course-meta {
  font-size: 14px;
  color: var(--text-sub);
  line-height: 1.4;
}

.range-slider-group {
  position: relative;
  /* 중요: 내부 요소를 배치하기 위한 기준점 */
  height: 20px;
  /* 슬라이더가 들어갈 공간 확보 */

}
</style>