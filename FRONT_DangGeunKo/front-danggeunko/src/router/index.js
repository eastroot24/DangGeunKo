import { createRouter, createWebHistory } from 'vue-router'
import CoureseListView from '@/views/Course/CoureseListView.vue'
import CreateCourseView from '@/views/Course/CreateCourseView.vue'
import CourseDetailView from '@/views/Course/CourseDetailView.vue'
import RankingListView from '@/views/Course/RankingListView.vue'
import OnBoardingView from '@/views/OnBoardingView.vue'
import LoginView from '@/views/LoginView.vue'
import SignUpView from '@/views/SignUpView.vue'
import App from '@/App.vue'
import MyInfoView from '@/views/MyPage/MyInfoView.vue'
import EditProfileView from '@/views/MyPage/EditProfileView.vue'
import FollowView from '@/views/MyPage/FollowView.vue'
import MyCourseListView from '@/views/MyPage/MyCourseListView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: App,
    },
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
      path: '/course/detail/:id',
      name: 'courseDetail',
      component: CourseDetailView,
      props: true,
    },
    {
      path: '/course/ranking',
      name: 'ranking',
      component: RankingListView},
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
