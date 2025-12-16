
<template>
    <div class="body">
      <div class="app">
      <div v-if="route.path !== '/myInfo'">
         <CourseMap></CourseMap>
      </div>
      <RouterView></RouterView>
      <CousreFooter></CousreFooter>
      </div>
      
    </div>
</template>

<script setup>
import CousreFooter from '@/components/Footer/CousreFooter.vue';
import CourseMap from '@/components/Main/CourseMap.vue';
import { useRoute } from 'vue-router';

const route = useRoute()

</script>

<style src="@/assets/style.css">
</style>