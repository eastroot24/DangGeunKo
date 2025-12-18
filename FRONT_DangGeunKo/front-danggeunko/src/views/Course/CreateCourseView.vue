<template>
  <div class="panel" ref="panelRef">
    <div class="drag-handle" @mousedown="startDrag"></div>
    <button class="back-btn" @click="goBack">
      ←
    </button>

    <div class="panel-title">코스 등록하기</div>

    <DrawingMap v-if="!isDrawing" @is-done="onDrawingDone" />

    <CourseForm v-else :points="points" :distanceKm="distanceKm" :startAddress="startAddress" :endAddress="endAddress"
      @is-done="retryDrawing" />

  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import CourseForm from '@/components/Course/Create/CourseForm.vue'
import DrawingMap from '@/components/Course/Create/DrawingMap.vue'
import { useRouter } from 'vue-router'

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

let startY = 0
let startHeight = 0
let dragging = false

const startDrag = (e) => {
  dragging = true
  startY = e.clientY
  startHeight = panelRef.value.offsetHeight

  document.addEventListener('mousemove', onMouseMove)
  document.addEventListener('mouseup', stopDrag)
}

const onMouseMove = (e) => {
  if (!dragging) return

  const diff = startY - e.clientY
  let newHeight = startHeight + diff

  newHeight = Math.max(200, Math.min(newHeight, window.innerHeight * 0.85))
  panelRef.value.style.height = `${newHeight}px`
}

const stopDrag = () => {
  dragging = false
  document.removeEventListener('mousemove', onMouseMove)
  document.removeEventListener('mouseup', stopDrag)
}

onMounted(() => {
  panelRef.value.style.height = '360px'
})

onBeforeUnmount(() => {
  document.removeEventListener('mousemove', onMouseMove)
  document.removeEventListener('mouseup', stopDrag)
})

</script>

<style>
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