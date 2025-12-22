import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import api from '@/api/axios'
import router from '@/router'
/* ===============================
   JWT base64url 디코딩 (원본 유지)
================================ */
function base64UrlDecode(str) {
  str = str.replace(/-/g, "+").replace(/_/g, "/")
  while (str.length % 4) {
    str += "="
  }
  try {
    return decodeURIComponent(
      Array.prototype.map
        .call(atob(str), c => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
        .join("")
    )
  } catch (e) {
    return atob(str)
  }
}

const REST_USER_API_URL = "http://localhost:8080/api-user/user/"
const REST_AUTH_API_URL = "http://localhost:8080/api-auth"

export const useUserStore = defineStore('user', () => {
  /* ===============================
     state
  ================================ */
  const userList = ref([])
  const loginUserId = ref(null)
  const user = ref({
    userName: '',
    nickname: '',
    userEmail: '',
    userPassword: '',
    gender: '',
    age: null,
    userCity:'',
    userDistrict:'',
    prefDistance: '',
    prefDifficulty: '',
    profileImg: null,
  })
  const isCoachOpen = ref(false);
const getUserByNickname = async (nickname) => {
  try {
    const res = await api.get(`${REST_USER_API_URL}nickname/${nickname}`);
    return res.data; 
  } catch (e) {
    console.error("유저를 찾을 수 없습니다.");
    return null;
  }
};
  /* 🔥 닉네임 중복확인 */
const nicknameAvailable = ref(null)

const checkNickname = async (nickname) => {
  try {
    const res = await api.get(`${REST_USER_API_URL}check/nickname/${nickname}`)
    const data = res.data

    nicknameAvailable.value =
      typeof data === 'boolean' ? data : data.available

    if (nicknameAvailable.value) {
      alert("사용 가능한 닉네임입니다.")
    } else {
      alert("이미 사용 중인 닉네임입니다.")
    }
  } catch (e) {
    nicknameAvailable.value = false
    alert("닉네임 확인 중 오류가 발생했습니다.")
  }
}

/* 🔥 이메일 중복확인 */
const emailAvailable = ref(null)

const checkEmail = async (email) => {
  try {
    const res = await api.get(`${REST_USER_API_URL}check/email/${email}`)
    const data = res.data

    emailAvailable.value =
      typeof data === 'boolean' ? data : data.available

    if (emailAvailable.value) {
      alert("사용 가능한 이메일입니다.")
    } else {
      alert("이미 사용 중인 이메일입니다.")
    }
  } catch (e) {
    emailAvailable.value = false
    alert("이메일 확인 중 오류가 발생했습니다.")
  }
}

  const followingList = ref([])
  const followerList = ref([])

  const isPwVerified = ref(false)

  /* 🔥 로그인 상태 (로컬스토리지 기반) */
  const loginStatus = ref(!!localStorage.getItem("accessToken"))

  /* ===============================
     computed
  ================================ */
  const isLoggedIn = computed(() => loginStatus.value)

  /* ===============================
     🔥 공통: token → userId 세팅
  ================================ */
  const setLoginUserFromToken = (token) => {
    const payload = JSON.parse(base64UrlDecode(token.split(".")[1]))
    loginUserId.value = payload.userId
    loginStatus.value = true
  }

  /* ===============================
     auth
  ================================ */
  const userLogin = async (id, password) => {
    try {
      const res = await axios.post(`${REST_AUTH_API_URL}/login`, { id, password })

      const token = res.data.accessToken
      localStorage.setItem("accessToken", token)

      setLoginUserFromToken(token)

      return true
    } catch (err) {
      console.log("로그인 실패", err)
      return false
    }
  }

  const userLogout = async () => {
    // 1. 먼저 정보를 비우기 전에 이동할 준비를 함
    localStorage.removeItem("accessToken");
    // 2. 상태값들을 먼저 초기화 (반응형 변수들)
    loginStatus.value = false;
    loginUserId.value = null;
    isLoggedIn.value = false;
    user.value = { };
    
    // 3. 마지막에 홈으로 이동
    await router.replace('/login'); 
};

  /* ===============================
     🔥 핵심 추가: 새로고침 / 직접 접근 대응
  ================================ */
  const initAuth = () => {
    const token = localStorage.getItem("accessToken")
    if (!token) {
      loginStatus.value = false
      loginUserId.value = null
      return
    }

    try {
      setLoginUserFromToken(token)
    } catch (e) {
      localStorage.removeItem("accessToken")
      loginStatus.value = false
      loginUserId.value = null
    }
  }

  /* ===============================
     user
  ================================ */
  const getAllUsers = () => {
    axios.get(REST_USER_API_URL)
      .then(res => userList.value = res.data)
  }

  const getUserById = async (id) => {
    if (!id) return
    const res = await api.get(`${REST_USER_API_URL}${id}`)
    user.value = res.data
    return res.data
  }
const addUser = async (formData) => {
  try {
    // axios는 formData를 보내면 자동으로 Content-Type을 multipart/form-data로 설정합니다.
    await axios.post(REST_USER_API_URL, formData);
    alert("당근코와 함께 달려봐요!");
  } catch (err) {
    console.error("회원가입 실패:", err);
    alert("회원가입 중 오류가 발생했습니다.");
  }
}

  const updateUser = async (formData) => { 
  try {
    // formData에서 userId를 꺼내거나 loginUserId를 사용
    const res = await api.put(`${REST_USER_API_URL}${loginUserId.value}`, formData);
    
    // 수정 후 최신 정보를 다시 불러
    await getUserById(loginUserId.value); 
    return true;
  } catch (err) {
    console.error("수정 실패:", err);
    return false;
  }
};

  const deleteUser = async (id) => {
    await api.delete(`${REST_USER_API_URL}${id}`)
  }

  /* ===============================
     follow
  ================================ */
  const addFollow = async (targetId) => {
    await api.post(`${REST_USER_API_URL}follow`, {
      followingId: loginUserId.value,
      followerId: targetId
    })
  }

  const deleteFollow = async (targetId) => {
    await api.delete(`${REST_USER_API_URL}follow`, {
      data: {
        followingId: loginUserId.value,
        followerId: targetId
      }
    })
  }

 /* user.js 의 follow 섹션 수정 */

const getFollowing = async (id) => {
    if (!id) return;
    const res = await api.get(`${REST_USER_API_URL}follow/following/${id}`);
    
    // 타인의 팔로잉 목록을 볼 때도, "내"가 그들을 팔로우 중인지 체크해야 함
    const myFingRes = await api.get(`${REST_USER_API_URL}follow/following/${loginUserId.value}`);
    const myFollowings = myFingRes.data;

    followingList.value = res.data.map(u => ({
        ...u,
        isFollowing: myFollowings.some(f => Number(f.userId) === Number(u.userId))
    }));
};

const getFollower = async (id) => {
    if (!id) return;
    
    // 1. 내 팔로잉 리스트 로드 (버튼 상태 체크용)
    const myFingRes = await api.get(`${REST_USER_API_URL}follow/following/${loginUserId.value}`);
    const myFollowings = myFingRes.data;

    // 2. 대상의 팔로워 리스트 로드
    const res = await api.get(`${REST_USER_API_URL}follow/follower/${id}`);
    
    // 3. 데이터 규격화 및 isFollowing 주입
    followerList.value = res.data.map(u => ({
        ...u,
        // 백엔드 응답에 따라 u.userId가 없을 경우 u.id 등으로 매핑 확인 필요
        isFollowing: myFollowings.some(f => Number(f.userId) === Number(u.userId))
    }));
  };

  /* ===============================
     pw check
  ================================ */
  const verifyPassword = () => (isPwVerified.value = true)
  const resetPwVerified = () => (isPwVerified.value = false)

  /* ===============================
     return
  ================================ */
  return {
    // state
    userList,
    user,
    loginUserId,
    nicknameAvailable,
    emailAvailable,
    followingList,
    followerList,
    isPwVerified,
    isCoachOpen,
    checkEmail,
    checkNickname,

    // computed
    isLoggedIn,

    // auth
    userLogin,
    userLogout,
    initAuth,

    // user
    getAllUsers,
    getUserById,
    addUser,
    updateUser,
    deleteUser,
    getUserByNickname,

    // follow
    addFollow,
    deleteFollow,
    getFollowing,
    getFollower,

    // pw
    verifyPassword,
    resetPwVerified,
  }
})
