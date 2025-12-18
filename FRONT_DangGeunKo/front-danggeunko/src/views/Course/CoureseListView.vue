<template>
  <div class="panel" ref="panelRef">
    <div class="drag-handle" @mousedown="startDrag"></div>
    <div class="panel-title">우리동네 코스 구경하기</div>
    <CourseFilter></CourseFilter>
    <CourseList></CourseList>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import CourseList from '@/components/Course/CourseList.vue'
import CourseFilter from '@/components/CourseFilter.vue'

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


<style scoped></style>
