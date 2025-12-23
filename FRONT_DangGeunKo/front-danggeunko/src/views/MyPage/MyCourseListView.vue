<template>
  <div class="app">

    <div class="top-bar">
      <div class="back-btn" @click="goBack">
        <i class="fi fi-rs-angle-left"></i>
      </div>
      <div class="title">
        등록/찜 코스 목록
      </div>
    </div>

    <div class="mylist-header">
      <div class="tab-toggle-wrap">
        <div class="tab-toggle">
          <button class="tab-btn" :class="{ active: tab === 'registered' }" @click="tab = 'registered'">
            등록 코스
          </button>

          <button class="tab-btn" :class="{ active: tab === 'liked' }" @click="tab = 'liked'">
            찜한 코스
          </button>
        </div>
      </div>
      <CourseSearchBar></CourseSearchBar>
      <CourseFilter></CourseFilter>
    </div>

    <div class="course-panel-wrapper">
      <RegistCourse v-if="tab === 'registered'" :targetUserId="targetUser?.userId" :allView="true" />
      <LikeCourse v-else :targetUserId="targetUser?.userId" :allView="true"></LikeCourse>
    </div>


  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'

import CourseSearchBar from '@/components/Main/CourseSearchBar.vue';
import CourseFilter from '@/components/CourseFilter.vue';
import RegistCourse from '@/components/MyPage/RegistCourse.vue';
import LikeCourse from '@/components/MyPage/LikeCourse.vue';

const route = useRoute()
const userStore = useUserStore()
const targetUser = ref(null)
const tab = ref(route.query.tab ?? "registered")

import { useRouter } from "vue-router";
import { useUserStore } from '@/stores/user';

const router = useRouter()

const goBack = () => {
  router.back()
}
onMounted(async () => {
  targetUser.value = await userStore.getUserByNickname(route.params.nickname);
})
</script>

<style scoped>
.mylist-header {
  padding: 1rem;
}

.top-bar {
  margin: 1rem;
}


.tab-toggle-wrap {
  display: flex;
  justify-content: center;
  margin-bottom: 8px;
}

.tab-toggle {
  display: inline-flex;
  border-radius: 999px;
  background: #f5f5f5;
  padding: 3px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08) inset;
  gap: 2px;
}

.tab-btn {
  border: none;
  background: transparent;
  border-radius: 999px;
  padding: 5px 14px;
  font-size: 14px;
  cursor: pointer;
  color: #777;
  transition: all 0.15s ease-out;
  min-width: 78px;
}

.tab-btn.active {
  background: var(--orange);
  color: #fff;
  box-shadow: var(--shadow-soft);
}
</style>