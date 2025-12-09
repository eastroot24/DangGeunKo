<template>
    <div>
        <Map/>
        <RouterView></RouterView>
        <Footer/>
    </div>
</template>

<script setup>
import Map from './components/CourseMap.vue';
import Footer from './components/CousreFooter.vue';
</script>

<style scoped>

</style>