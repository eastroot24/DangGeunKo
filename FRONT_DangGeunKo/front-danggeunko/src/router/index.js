
import { createRouter, createWebHistory } from 'vue-router'
import CoureseListView from '@/views/Course/CoureseListView.vue'
import CreateCourseView from '@/views/Course/CreateCourseView.vue'
import CourseDetailView from '@/views/Course/CourseDetailView.vue'
import RankingListView from '@/views/Course/RankingListView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/course',
      name: 'course',
      component: CoureseListView,
    },
    {
      path: '/course/create',
      name: 'courseCreate',
      component: CreateCourseView,
    },
    {
      path: '/course/detail',
      name: 'courseDetail',
      component: CourseDetailView,
    },
    {
      path: '/course/ranking',
      name: 'ranking',
      component: RankingListView,
    },
  ],
})

export default router
