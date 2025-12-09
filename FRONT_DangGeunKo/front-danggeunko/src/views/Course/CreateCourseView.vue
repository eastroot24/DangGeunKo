<template>
    <div class="panel">
        <div class="drag-handle"></div>
        <div class="panel-title">코스 등록하기</div>
        <!-- 코스지도 그리기 -->
        <DrawingMap v-if="!isDrawing" 
        @is-done="updateDrawing"></DrawingMap>
        <!-- 코스 정보 입력 폼 -->
        <CourseForm v-if="isDrawing" 
        @is-done="updateDrawing"></CourseForm>
    </div>
</template>

<script setup>
import CourseForm from '@/components/Course/Create/CourseForm.vue';
import DrawingMap from '@/components/Course/Create/DrawingMap.vue';
import { ref } from 'vue';

let isDrawing = ref(false)
const updateDrawing = (bool) =>{
    isDrawing.value = bool
}

</script>

<style scoped>
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