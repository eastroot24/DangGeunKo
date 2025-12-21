<template>
    <div class="panel" ref="panelRef">
        <div class="drag-handle" @mousedown="(e) => startDrag(e, panelRef)"></div>
        <div class="panel-title">주간 코스 랭킹</div>
        <RankingFilter></RankingFilter>
        <div class="course-panel-wrapper"><RankingList></RankingList></div>
        
    </div>
</template>

<script setup>
import RankingFilter from '@/components/Ranking/RankingFilter.vue';
import RankingList from '@/components/Ranking/RankingList.vue';
import { ref, watch, onMounted } from 'vue'
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