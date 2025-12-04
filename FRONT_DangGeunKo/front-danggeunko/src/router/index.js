import CourseView from '@/views/CourseView.vue'
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/course',
      name: 'course',
      component: CourseView,
    },
  ],
})

export default router
