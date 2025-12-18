<template>
  <div class="panel">
    <div class="drag-handle" id="dragHandle"></div>
    <div class="panel-title">우리동네 코스 구경하기</div>
    <CourseFilter></CourseFilter>
    <CourseList></CourseList>

  </div>
</template>

<script setup>
import CourseList from '@/components/Course/CourseList.vue';
import CourseFilter from '@/components/CourseFilter.vue';

</script>

<style scoped>
.panel-title {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 14px;
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
</style>