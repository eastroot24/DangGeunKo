<template>
    <div>
        <h2>UserTest</h2>
        <router-link to="/">Home</router-link> |
        <router-link :to="{name: 'user'}">유저 페이지</router-link>
        <router-view></router-view>
    </div>
</template>

<script setup>

</script>

<style scoped>

</style>