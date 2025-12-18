<template>
    <div class="panel">
        <div class="drag-handle" id="dragHandle"></div>
        <h2>주간 코스 랭킹</h2>
        <RankingFilter></RankingFilter>
        <RankingList></RankingList>
    </div>
</template>

<script setup>
import RankingFilter from '@/components/Ranking/RankingFilter.vue';
import RankingList from '@/components/Ranking/RankingList.vue';


</script>

<style scoped></style>