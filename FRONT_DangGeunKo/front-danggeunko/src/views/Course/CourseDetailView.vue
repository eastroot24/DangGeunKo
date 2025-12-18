<template>
    <div class="panel" ref="panelRef">
        <div class="drag-handle" @mousedown="startDrag"></div>
        <CourseDetail :courseId="route.params.id"></CourseDetail>
        <Review :courseId="route.params.id"></Review>
    </div>
</template>

<script setup>
import CourseDetail from '@/components/Course/CourseDetail.vue';
import Review from '@/components/Review/Review.vue';
import { useRoute } from 'vue-router';
import { ref, onMounted, onBeforeUnmount } from 'vue'

const route = useRoute()

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