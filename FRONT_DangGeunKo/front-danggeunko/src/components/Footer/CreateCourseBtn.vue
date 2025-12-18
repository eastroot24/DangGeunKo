<template>
    <div>
        <RouterLink to="/course/create" class="plus-btn">＋</RouterLink>
    </div>
</template>

<script setup>

</script>

<style scoped>
.plus-btn {
    width: 48px;
    /* 크기를 약간 키움 */
    height: 48px;
    border-radius: 50%;
    background: #ff7a00;
    color: #fff;
    font-size: 26px;
    border: none;
    display: flex;
    align-items: center;
    justify-content: center;
    position: fixed;
    /* absolute에서 fixed로 변경하여 화면 기준 고정 */
    left: calc(50% + 130px);
    /* 390px 화면 기준 우측 배치 */
    bottom: 80px;
    /* 내브바 높이(62px)보다 높게 설정 */
    cursor: pointer;
    text-decoration: none;
    box-shadow: 0 4px 12px rgba(255, 122, 0, 0.3);
    z-index: 1100;
    /* 내브바보다 위에 위치 */
}
</style>