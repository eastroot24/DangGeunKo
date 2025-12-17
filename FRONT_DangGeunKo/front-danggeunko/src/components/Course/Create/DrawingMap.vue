<template>
  <div>
    <div class="map-box">
      <div>마커를 찍어 코스를 그려보세요!</div>

      <CourseMapCreate @update:points="coursePoints = $event" @update:distance="distanceKm = $event"
        @update:startAddress="startAddress = $event" @update:endAddress="endAddress = $event" />
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

const draw = () => {
  emit('isDone', {
    points: coursePoints.value,
    distanceKm: distanceKm.value,
    startAddress: startAddress.value,
    endAddress: endAddress.value
  })
}
</script>