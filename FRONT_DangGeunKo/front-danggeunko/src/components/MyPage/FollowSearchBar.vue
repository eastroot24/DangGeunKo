<template>
    <div class="search-bar">
        <input :value="modelValue" @input="$emit('update:modelValue', $event.target.value)"
            placeholder="보고 싶은 사람을 검색하세요">
        <div class="icon"><i class="fi fi-rs-search"></i></div>
    </div>
</template>

<script setup>
// 부모로부터 값을 받고, 값을 보낼 이벤트를 정의함
defineProps(['modelValue'])
defineEmits(['update:modelValue'])
</script>

<style scoped>
.search-bar {
    margin: auto;
    width: 100%;
    position: relative;
}

.search-bar input {
    width: 100%;
    padding: 10px 12px;
    border-radius: 10px;
    border: 1px solid #ddd;
    font-size: 13px;
}

.search-bar .icon {
    position: absolute;
    right: 10px;
    top: 10px;
    font-size: 18px;
    color: #666;
}
</style>