<template>
  <div class="panel" ref="panelRef">
    <div class="drag-handle" @mousedown="(e) => startDrag(e, panelRef)"></div>
    <div class="panel-title">우리동네 코스 구경하기</div>
    <CourseFilter></CourseFilter>
    <div class="course-panel-wrapper"><CourseList></CourseList></div>
    
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import CourseList from '@/components/Course/CourseList.vue'
import CourseFilter from '@/components/CourseFilter.vue'
import { useSlidingPanel } from '@/assets/script.js'

const panelRef = ref(null)

const {
  panelState,
  setPanelState,
  syncTapCount,
  applyHeight,
  startDrag
} = useSlidingPanel()

onMounted(() => {
  // 1. 이 페이지 진입 시 초기 높이를 'half(1)'로 설정
  setPanelState(1)
  // 2. 탭 카운트도 1회 클릭된 상태로 동기화 (그래야 다음 클릭 시 2단계인 full이 됨)
  syncTapCount(1)

  if (panelRef.value) {
    applyHeight(panelRef.value)
  }
})

watch(panelState, () => {
  if (panelRef.value) {
    applyHeight(panelRef.value)
  }
}, { immediate: true })

</script>


<style scoped></style>
