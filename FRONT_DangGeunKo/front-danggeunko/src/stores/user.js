import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import api from '@/api/axios'

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
    region: '',
    prefDistance: '',
    prefDifficulty: '',
    profileImg: null,
  })

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
    localStorage.removeItem("accessToken")
    loginStatus.value = false
    loginUserId.value = null
    user.value = {}
  }

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

  const updateUser = async (id, data) => {
    const res = await api.put(`${REST_USER_API_URL}${id}`, data)
    user.value = res.data
  }

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

  const getFollowing = async (id) => {
    if (!id) return
    const res = await api.get(`${REST_USER_API_URL}follow/following/${id}`)
    followingList.value = res.data.map(u => ({ ...u, isFollowing: true }))
  }

  const getFollower = async (id) => {
    if (!id) return
    await getFollowing(id)
    const res = await api.get(`${REST_USER_API_URL}follow/follower/${id}`)
    followerList.value = res.data.map(u => ({
      ...u,
      isFollowing: followingList.value.some(f => f.userId === u.userId)
    }))
  }

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
