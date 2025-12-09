<template>
    <div>
    <div class="user-card" v-for="n in 5" :key="n">
        <div class="user-left">
            <img src="https://i.imgur.com/7b3cP6e.png" class="user-img">
            <div>
                <div class="user-name">팔로잉 {{ n }}</div>
                <div class="user-meta">서울시 서초구</div>
            </div>
        </div>
        <button class="follow-btn">팔로잉</button>
    </div>
</div>
</template>

<script setup>

</script>

<style scoped>

</style>