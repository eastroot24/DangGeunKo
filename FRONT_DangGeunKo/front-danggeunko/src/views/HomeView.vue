
<template>
    <div class="body">
      <div class="app">
      <CourseMap></CourseMap>
        <RouterView></RouterView>
      <CousreFooter></CousreFooter>
      </div>
      
    </div>
</template>

<script setup>
import CousreFooter from '@/components/Footer/CousreFooter.vue';
import CourseMap from '@/components/Main/CourseMap.vue';

</script>

<style src="@/assets/style.css">
</style>