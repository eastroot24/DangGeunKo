<template>
    <div class="app">
        <div class="back" @click="goMyInfo">←</div>
        <div class="panel-title">등록/찜 코스 목록</div>
        <div class="tab-toggle-wrap">
          <div class="tab-toggle">
            <button 
              class="tab-btn" 
              :class="{ active: tab==='registered' }"
              @click="tab='registered'"
            >
              등록 코스
            </button>

            <button 
              class="tab-btn" 
              :class="{ active: tab==='liked' }"
              @click="tab='liked'"
            >
              찜한 코스
            </button>
        </div>
        </div>
        <CourseSearchBar></CourseSearchBar>
        <CourseFilter></CourseFilter>
        <RegistCourse v-if="tab==='registered'" :allView="true" />
        <LikeCourse v-else :allView="true"></LikeCourse>
    </div>
</template>

<script setup>
import {ref} from 'vue'
import { useRoute } from 'vue-router'

import CourseSearchBar from '@/components/Main/CourseSearchBar.vue';
import CourseFilter from '@/components/CourseFilter.vue';
import RegistCourse from '@/components/MyPage/RegistCourse.vue';
import LikeCourse from '@/components/MyPage/LikeCourse.vue';

const route = useRoute()

const tab = ref(route.query.tab ?? "registered")

import { useRouter } from "vue-router";

const router = useRouter()

const goMyInfo = () => {
    router.push({ name: "myInfo" })
}
</script>

<style scoped>
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
      font-size: 11px;
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