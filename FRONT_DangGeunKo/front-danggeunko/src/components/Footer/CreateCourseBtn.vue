<template>
    <div>
        <RouterLink to="/course/create" class="plus-btn"><i class="fi fi-rs-plus"></i></RouterLink>
    </div>
</template>

<script setup>

</script>

<style scoped>
.plus-btn {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    background: #ff8a24;
    color: #f5f5f5;
    font-size: 20px;
    border: none;
    display: flex;
    align-items: center;
    justify-content: center;
    position: fixed;
    right: calc(50% - 800px/2 + 30px);
    bottom: 130px;
    cursor: pointer;
    text-decoration: none;
    /* 기본 그림자 (피그마보다 살짝 약하게) */
    box-shadow: 0 4px 4px rgba(0, 0, 0, 0.25);

    /* 호버 애니메이션용 */
    transition:
        transform 0.2s ease,
        box-shadow 0.2s ease,
        background-color 0.2s ease;
    z-index: 1100;
}

.plus-btn i {
    display: flex;
  align-items: center;
  justify-content: center;
  line-height: 1; 
}

.plus-btn:hover {
    transform: translateY(-2px);
    background-color: #ff6a00;
}
</style>