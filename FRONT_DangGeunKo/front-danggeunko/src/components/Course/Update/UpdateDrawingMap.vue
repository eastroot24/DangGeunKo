<template>
    <div>
    <div class="map-box" id="mapBox">
        <div > 
            마커를 찍어 코스를 그려보세요!
        </div>
        <!-- 여기에 지도 API이용해서 지도그리는 칸 들어갈거임 -->
    </div>

    <button @click="draw">코스 그리기 완료</button> 
    </div>
</template>

<script setup>
const emit = defineEmits(["draw"])
const draw = () => {
    emit("isDone", true)
}

</script>

<style lang="scss" scoped>

</style>