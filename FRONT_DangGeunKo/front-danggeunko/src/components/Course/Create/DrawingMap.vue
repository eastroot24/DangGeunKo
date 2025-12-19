<template>
  <div>
    <div class="map-box">
      <CourseMapCreate @update:points="(points) => {
        coursePoints = points
        if (points.length > 0) errorMessage = ''
      }" @update:distance="distanceKm = $event" @update:startAddress="startAddress = $event"
        @update:endAddress="endAddress = $event" />
    </div>

    <div v-if="errorMessage" class="error-text">
      {{ errorMessage }}
    </div>

    <button @click="draw">코스 그리기 완료</button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import CourseMapCreate from '@/components/Main/CourseMapCreate.vue'

const emit = defineEmits(['isDone'])

const coursePoints = ref([])
const distanceKm = ref(0)
const startAddress = ref('')
const endAddress = ref('')

const errorMessage = ref('')

const draw = () => {
  if (coursePoints.value.length === 0) {
    errorMessage.value = '마커를 찍어 코스를 그려주세요!'
    return
  }

  // 정상일 경우 에러 제거
  errorMessage.value = ''

  emit('isDone', {
    points: coursePoints.value,
    distanceKm: distanceKm.value,
    startAddress: startAddress.value,
    endAddress: endAddress.value
  })
}
</script>

<style scoped>
.error-text {
  margin-top: 8px;
  color: red;
  font-size: 13px;
}
</style>
