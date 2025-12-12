import { createRouter, createWebHistory } from 'vue-router'
import CoureseListView from '@/views/Course/CoureseListView.vue'
import CreateCourseView from '@/views/Course/CreateCourseView.vue'
import CourseDetailView from '@/views/Course/CourseDetailView.vue'
import RankingListView from '@/views/Course/RankingListView.vue'
import OnBoardingView from '@/views/OnBoardingView.vue'
import LoginView from '@/views/LoginView.vue'
import SignUpView from '@/views/SignUpView.vue'
import MyInfoView from '@/views/MyPage/MyInfoView.vue'
import EditProfileView from '@/views/MyPage/EditProfileView.vue'
import FollowView from '@/views/MyPage/FollowView.vue'
import MyCourseListView from '@/views/MyPage/MyCourseListView.vue'
import { useUserStore } from '@/stores/user'


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
      meta: { requiresAuth: true }
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
      meta: { requiresPwCheck: true },
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

router.beforeEach((to, from, next) => {
  const store = useUserStore()
  //비밀번호 확인 로직
  if (to.meta.requiresPwCheck && !store.isPwVerified) {
    alert("비밀번호 확인 후 접근 가능합니다.")
    return next({ name: 'myInfo' })
  }

  //로그인 인증
  if(to.meta.requiresAuth && !store.isAuthenticated){
    alert("로그인이 필요한 서비스입니다.")
    return next({name: 'login'})
  }

  next()
})

export default router
