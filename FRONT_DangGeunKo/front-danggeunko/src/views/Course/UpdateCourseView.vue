<template>
  <div class="panel" ref="panelRef">
    <div class="drag-handle" @mousedown="(e) => startDrag(e, panelRef)"></div>
    <button class="back-btn" @click="goBack">
      ←
    </button>
    <div class="panel-title">코스 수정하기</div>
    <!-- 코스지도 그리기 -->
    <UpdateDrawingMap v-if="!isDrawing" @is-done="updateDrawing"></UpdateDrawingMap>
    <!-- 코스 정보 입력 폼 -->
    <UpdateCourse v-else :points="points" :distanceKm="distanceKm" :startAddress="startAddress" :endAddress="endAddress"
      @is-done="retryDrawing"></UpdateCourse>
  </div>
</template>

<script setup>
import UpdateCourse from '@/components/Course/Update/UpdateCourse.vue';
import UpdateDrawingMap from '@/components/Course/Update/UpdateDrawingMap.vue';
import { ref, watch, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useSlidingPanel } from '@/assets/script.js'

const router = useRouter()

const goBack = () => {
  router.back()
}

const isDrawing = ref(false)

const points = ref([])
const distanceKm = ref(0)
const startAddress = ref('')
const endAddress = ref('')

const updateDrawing = (payload) => {
  points.value = payload.points
  distanceKm.value = payload.distanceKm
  startAddress.value = payload.startAddress
  endAddress.value = payload.endAddress
  isDrawing.value = true
}

const retryDrawing = () => {
  isDrawing.value = false
}

const panelRef = ref(null)

const {
  panelState,
  setPanelState,
  applyHeight,
  startDrag
} = useSlidingPanel()

onMounted(() => {
  setPanelState(2)
  if (panelRef.value) {
    applyHeight(panelRef.value)
  }
})

// 2. 상태 변화 감시 및 높이 적용
watch(panelState, () => {
  if (panelRef.value) {
    applyHeight(panelRef.value)
  }
})

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

.back-btn {
  background: #fff;
  border: 1px solid #ff7a00;
  color: #ff7a00;
  font-weight: 600;
}

.back-btn:hover {
  background: #fff3e8;
}
</style>