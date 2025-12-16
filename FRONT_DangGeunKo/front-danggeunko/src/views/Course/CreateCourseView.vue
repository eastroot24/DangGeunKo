<template>
  <div class="panel">
    <div class="drag-handle"></div>
    <div class="panel-title">코스 등록하기</div>

    <DrawingMap
  v-if="!isDrawing"
  @is-done="onDrawingDone"
/>

<CourseForm
  v-else
  :points="points"
  :distanceKm="distanceKm"
  :startAddress="startAddress"
  :endAddress="endAddress"
  @is-done="retryDrawing"
/>

  </div>
</template>

<script setup>
import { ref } from 'vue'
import CourseForm from '@/components/Course/Create/CourseForm.vue'
import DrawingMap from '@/components/Course/Create/DrawingMap.vue'

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

</script>
