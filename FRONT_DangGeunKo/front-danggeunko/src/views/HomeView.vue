<template>
  <div class="body">
    <div class="app">
      <div class="map-layer" v-if="!isMyPage">
        <CourseMap />
        <div v-if="showOverlayBackdrop" class="map-backdrop" @click="goHome"></div>
      </div>
      <div class="panel-layer"><RouterView /></div>
      
      <div v-if="!isOnBoarding">
        <CousreFooter></CousreFooter>
      </div>

    </div>

  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import CousreFooter from '@/components/Footer/CousreFooter.vue';
import CourseMap from '@/components/Main/CourseMap.vue';

const route = useRoute();
const router = useRouter();

const hiddenPaths = [
  'myInfo',
  'login',
  'signup',
  'editProfile',
  'myCourseList',
  'follow',
  'onboarding'
];

const hiddenAllPaths = [
  'onboarding',
]

// 현재 경로(route.path)에 위 키워드 중 하나라도 포함되어 있는지 확인
const isMyPage = computed(() => {
  return hiddenPaths.some(path => route.path.includes(path));
});

const isOnBoarding = computed(() => {
  return hiddenAllPaths.some(path => route.path.includes(path));
});

const showOverlayBackdrop = computed(() => {
  return !isMyPage.value && route.path !== '/';
});

const goHome = () => {
  router.replace('/');
};

</script>

<style scoped>
/* 지도 영역 */
.map-layer {
  position: relative;
  width: 100%;
  height: 100%;
}

/* 지도 위 클릭용 투명 레이어 */
.map-backdrop {
  position: absolute;
  inset: 0;
  background: transparent;
}

/* 패널은 항상 위 */
.panel-layer {
  position: relative;
  z-index: 10;
}
</style>