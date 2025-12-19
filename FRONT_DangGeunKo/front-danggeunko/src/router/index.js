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
import UpdateCourseView from '@/views/Course/UpdateCourseView.vue'
import HomeView from '@/views/HomeView.vue'
import App from '@/App.vue'


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
      meta: { requiresAuth: true },
    },
    {
      path: '/course/detail/:id',
      name: 'courseDetail',
      component: CourseDetailView,
      props: true,
    },
    {
      path: '/course/update/:id',
      name: 'courseUpdate',
      component: UpdateCourseView,
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
      meta: { alreadyAuth: true },
    },
    {
      path: "/signUp",
      name: "signUp",
      component: SignUpView,
      meta: { alreadyAuth: true },
    },
    {
      path: "/myInfo/:nickname",
      name: "myInfo",
      component: MyInfoView,
      meta: { requiresAuth: true },
    },
    {
      path: "/editProfile",
      name: "editProfile",
      component: EditProfileView,
      meta: { requiresPwCheck: true },
    },
    {
      path: "/follow/:type/:nickname",
      name: "follow",
      component: FollowView,
      meta: { requiresAuth: true },
    },
    {
      path: "/myCourseList/:nickname",
      name: "myCourseList",
      component: MyCourseListView,
      meta: { requiresAuth: true },
    },
  ],
})

router.beforeEach((to, from, next) => {
  const store = useUserStore();

  // 1. 로그인 중 로그인/회원가입 페이지 접근 시
  if (to.meta.alreadyAuth && store.isLoggedIn) {
    alert("로그아웃 후 이용해주세요.");
    
    // 이전 페이지 이름이 있으면 거기로, 없으면 'course'로
    const destination = from.name ? { name: from.name } : { name: 'home' };
    
    // 만약 주소창에 직접 입력해서 들어온 경우(from.name이 없음) 
    // 혹은 이전 페이지로 돌아가야 하는 경우
    return next(destination);
  }

  // 2. 로그인 인증이 필요한 경우
  if (to.meta.requiresAuth && !store.isLoggedIn) {
    alert("로그인이 필요한 서비스입니다.");
    return next({ name: 'login' });
  }

  // 3. 비밀번호 확인이 필요한 경우
  if (to.meta.requiresPwCheck && !store.isPwVerified) {
    alert("비밀번호 확인 후 접근 가능합니다.");
    return next({ name: 'myInfo' });
  }

  next(); // 위 조건들에 해당하지 않으면 정상 이동
});

export default router
