<template>
    <div class="panel" ref="panelRef">
        <div class="drag-handle" @mousedown="(e) => startDrag(e, panelRef)"></div>
        <CourseDetail :courseId="route.params.id"></CourseDetail>
        <Review :courseId="route.params.id"></Review>
    </div>
</template>

<script setup>
import CourseDetail from '@/components/Course/CourseDetail.vue';
import Review from '@/components/Review/Review.vue';
import { useRoute } from 'vue-router';
import { ref, watch, onMounted } from 'vue'
import { useSlidingPanel } from '@/assets/script.js'

const route = useRoute()

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

<style scoped></style>