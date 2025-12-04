
import HomeVue from '@/views/HomeView.vue'
import UserView from '@/views/UserView.vue'
import CourseView from '@/views/CourseView.vue'
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeVue,
    },
    {
      path: "/user",
      name: "user",
      component: UserView,
      path: '/course',
      name: 'course',
      component: CourseView,
    },
  ],
})

export default router
