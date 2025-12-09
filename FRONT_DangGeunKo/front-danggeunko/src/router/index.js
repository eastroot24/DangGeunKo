import { createRouter, createWebHistory } from 'vue-router'
import OnBoardingView from '@/views/OnBoardingView.vue'
import LoginView from '@/views/LoginView.vue'
import SignUpView from '@/views/SignUpView.vue'
import App from '@/App.vue'
import MyInfoView from '@/views/MyInfoView.vue'
import EditProfileView from '@/views/EditProfileView.vue'
import FollowView from '@/views/FollowView.vue'
import MyCourseListView from '@/views/MyCourseListView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/onBoarding",
      name: "onBoarding",
      component: OnBoardingView,
    },
    {
      path: "/login",
      name: "login",
      component: LoginView,
    },
    {
      path: "/signUp",
      name: "signUp",
      component: SignUpView,
    },
    {
      path: "/myInfo",
      name: "myInfo",
      component: MyInfoView,
    },
    {
      path: "/editProfile",
      name: "editProfile",
      component: EditProfileView,
    },
    {
      path: "/follow",
      name: "follow",
      component: FollowView,
    },
    {
      path: "/myCourseList",
      name: "myCourseList",
      component: MyCourseListView,
    },
  ],
})

export default router
