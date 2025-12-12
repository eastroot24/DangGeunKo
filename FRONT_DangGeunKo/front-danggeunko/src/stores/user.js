import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import api from '@/api/axios'   // 🔥 interceptor 적용 axios

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
    prefDifficulty: ''
  })

  const nicknameAvailable = ref(null)
  const emailAvailable = ref(null)

  const followingList = ref([])
  const followerList = ref([])

  const isPwVerified = ref(false)

  /* ===============================
     computed
  ================================ */
  const isLoggedIn = computed(() => !!localStorage.getItem("accessToken"))

  /* ===============================
     auth
  ================================ */
  const userLogin = async (id, password) => {
    try {
      const res = await axios.post(`${REST_AUTH_API_URL}/login`, { id, password })

      const token = res.data.accessToken
      localStorage.setItem("accessToken", token)

      const payload = JSON.parse(base64UrlDecode(token.split(".")[1]))
      loginUserId.value = payload.userId

      return true
    } catch (err) {
      console.log("로그인 실패", err)
      return false
    }
  }

  const userLogout = async () => {
    localStorage.removeItem("accessToken")
    loginUserId.value = null
    user.value = {}
  }

  // 🔥 새로고침 / 뒤로가기 대응
  const restoreLoginFromToken = () => {
    const token = localStorage.getItem("accessToken")
    if (!token) return

    try {
      const payload = JSON.parse(base64UrlDecode(token.split(".")[1]))
      loginUserId.value = payload.userId
    } catch (e) {
      localStorage.removeItem("accessToken")
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

  const addUser = (data) => {
    axios.post(REST_USER_API_URL, data)
      .then(() => alert("당근코와 함께 달려봐요!"))
  }

  const updateUser = async (id, data) => {
    const res = await api.put(`${REST_USER_API_URL}${id}`, data)
    user.value = res.data
  }

  const deleteUser = async (id) => {
    await api.delete(`${REST_USER_API_URL}${id}`)
  }

  /* ===============================
     nickname / email check
  ================================ */
  const checkNickname = (nickname) => {
    axios.get(`${REST_USER_API_URL}check/nickname/${nickname}`)
      .then(res => {
        nicknameAvailable.value = res.data === true || res.data?.available
        alert(nicknameAvailable.value ? "사용 가능한 닉네임입니다." : "이미 사용 중인 닉네임입니다.")
      })
      .catch(() => nicknameAvailable.value = false)
  }

  const checkEmail = (email) => {
    axios.get(`${REST_USER_API_URL}check/email/${email}`)
      .then(res => {
        emailAvailable.value = res.data === true || res.data?.available
        alert(emailAvailable.value ? "사용 가능한 이메일입니다." : "이미 사용 중인 이메일입니다.")
      })
      .catch(() => emailAvailable.value = false)
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
    const res = await api.get(`${REST_USER_API_URL}follow/following/${id}`)
    followingList.value = res.data.map(u => ({ ...u, isFollowing: true }))
  }

  const getFollower = async (id) => {
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

    // computed
    isLoggedIn,

    // auth
    userLogin,
    userLogout,
    restoreLoginFromToken,

    // user
    getAllUsers,
    getUserById,
    addUser,
    updateUser,
    deleteUser,

    // check
    checkNickname,
    checkEmail,

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
