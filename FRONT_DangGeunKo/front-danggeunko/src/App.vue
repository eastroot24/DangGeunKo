
<template>
    <div class="body">
      <div class="app">
      <Map />
        <RouterView></RouterView>
      <Footer />
      </div>
      
    </div>
</template>

<script setup>

import Map from './components/Main/CourseMap.vue';
import Footer from './components/Footer/CousreFooter.vue';
</script>

<style src="@/assets/style.css">
</style>