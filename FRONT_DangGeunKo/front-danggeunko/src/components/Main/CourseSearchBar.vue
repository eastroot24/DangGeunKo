<template>
  <div class="top-search-row">
    <div>
      <select v-model="searchInfo.key" class="category-btn" id="categorySelect">
        <option value="courseName">제목</option>
        <option value="userName">작성자</option>
        <option value="description">내용</option>
      </select>
    </div>

    <div class="search-input-wrap">
      <input type="text" class="search-input" placeholder="코스 제목을 입력하세요" v-model="searchInfo.value"
        @keyup.enter="searchCourseList" />
      <button @click="searchCourseList" class="search-btn">🔍검색</button>
    </div>
  </div>
</template>

<script setup>
import { useCourseStore } from '@/stores/course'
import { storeToRefs } from 'pinia'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const store = useCourseStore()

const { searchInfo } = storeToRefs(store)

const searchCourseList = () => {

  if (route.path == '/') {
    router.push({ name: 'course' })
      .then(() => {
        store.searchCourseList()
      })
      .catch((err) => {
        console.log("라우팅 오류:", err)
      })
  } else {
    store.searchCourseList()

  }

}
</script>

<style scoped></style>