<template>
  <div class="panel" ref="panelRef">
    <div class="drag-handle" @mousedown="(e) => startDrag(e, panelRef)"></div>
    <button class="back-btn" @click="goBack">
      <i class="fi fi-rs-angle-left"></i>
    </button>

    <div class="panel-title">코스 등록하기</div>

    <DrawingMap v-if="!isDrawing" @is-done="onDrawingDone" />

    <CourseForm v-else :points="points" :distanceKm="distanceKm" :startAddress="startAddress" :endAddress="endAddress"
      @is-done="retryDrawing" />

  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import CourseForm from '@/components/Course/Create/CourseForm.vue'
import DrawingMap from '@/components/Course/Create/DrawingMap.vue'
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

const onDrawingDone = (payload) => {
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

<style>
.back-btn {
  background: #fff;
  color: #ff7a00;
  font-weight: 600;
}

.back-btn:hover {
  background: #fff3e8;
}
</style>