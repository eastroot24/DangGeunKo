<template>
    <div class="search-bar">
        <input placeholder="보고 싶은 사람을 검색하세요">
        <div class="icon">🔍</div>
    </div>
</template>

<script setup>

</script>

<style scoped>
.search-bar{
    margin:18px auto 8px;width:86%;position:relative;
}
.search-bar input{
    width:100%;padding:10px 12px;border-radius:10px;border:1px solid #ddd;font-size:13px;
}
.search-bar .icon{
    position:absolute;right:10px;top:8px;font-size:18px;color:#666;
}
</style>