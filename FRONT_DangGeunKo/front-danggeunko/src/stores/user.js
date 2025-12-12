import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'

// base64url 디코딩 함수 (한글 깨짐 방지)
function base64UrlDecode(str) {
  // base64url -> base64로 변환
  str = str.replace(/-/g, "+").replace(/_/g, "/");
  // 패딩 추가
  while (str.length % 4) {
    str += "=";
  }
  // 디코딩 (한글 지원)
  try {
    return decodeURIComponent(
      Array.prototype.map
        .call(
          atob(str),
          (c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2)
        )
        .join("")
    );
  } catch (e) {
    return atob(str);
  }
}



const REST_USER_API_URL = "http://localhost:8080/api-user/user/"
const REST_AUTH_API_URL = "http://localhost:8080/api-auth"

export const useUserStore = defineStore('user', () => {
  const userList = ref([])
  const loginUserId = ref(null)   // 예시값
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

  const isAuthenticated = computed(() => {
    return !!localStorage.getItem("accessToken") 
  })

  const userLogin = function(id, password){
    axios.post(`${REST_AUTH_API_URL}/login`, {
     id, password
    })
    .then((res)=>{
      console.log(res.data)

      //loginUser 정보는 Back서버에서 토큰과 함께 데이터를 더 넘기는 것이 좋다. 편하다.
      const token = res.data["accessToken"]
      const refreshToken = res.data["refreshToken"]

      const payloadEncoding = token.split(".")[1]
      const payloadDecoding = JSON.parse(base64UrlDecode(payloadEncoding))
      const userId = payloadDecoding["userId"]

      loginUserId.value = userId

      localStorage.setItem("accessToken", token)
      localStorage.setItem("refreshToken", refreshToken)
      
      console.log("로그인 성공!")
      console.log("유저정보:", loginUserId)
    })
    .catch((err)=>{
      console.log("로그인에러", err)
    })
  }
  //로그아웃
  const userLogout = function(){
    const refreshToken = localStorage.getItem("refreshToken") 
    // const tokenToInvalidate = localStorage.getItem("accessToken") // 혹은 "refresh-token"
    
    // 백엔드가 @RequestParam을 사용하므로, 쿼리 스트링으로 전달합니다.
    axios.post(`${REST_AUTH_API_URL}/logout?refreshToken=${refreshToken}`) 
    .then((res)=>{
      console.log(res.data)
      console.log("잘가라~!")
      localStorage.removeItem("accessToken") // 로컬 토큰 제거
      localStorage.removeItem("refreshToken")
      // loginUser.value = null 등 Pinia 상태 초기화
    })
    .catch((err)=>{
      console.log("로그아웃실패", err)
    })
}


  const getAllUsers = () => {
    axios.get(REST_USER_API_URL)
    .then((res) => {
        userList.value = res.data
        console.log(res.data)
    })
  }
  const getUserById = function(id){
    axios.get(`${REST_USER_API_URL}${id}`)
    .then((res) => {
        user.value = res.data
    })
    .catch((err)=>{
      console.log(err)
    })
  }
  const addUser = function(user){
    axios({
      url: REST_USER_API_URL,
      method: "POST",
      data: user
    })
    .then((res)=>{
      console.log(res.data)
      alert("당근코와 함께 달려봐요!")
      router.push('/')
    })
    .catch((err)=>{
      console.log(err)
    })
  }
  /* 🔥 닉네임 중복확인 */
  const nicknameAvailable = ref(null)
  const checkNickname = (nickname) => {
  axios.get(`${REST_USER_API_URL}check/nickname/${nickname}`)
    .then(res => {
      const data = res.data
      nicknameAvailable.value = (typeof data === 'boolean' ? data : data.available)
      if (nicknameAvailable.value) alert("사용 가능한 닉네임입니다.")
      else alert("이미 사용 중인 닉네임입니다.")
    })
    .catch(() => {
      nicknameAvailable.value = false
    })
  }

  /* 🔥 이메일 중복확인 */
  const emailAvailable = ref(null)
  const checkEmail = (email) => { // ← email 직접 전달받기
  axios.get(`${REST_USER_API_URL}check/email/${email}`)
    .then(res => {
      const data = res.data
      emailAvailable.value = (typeof data === 'boolean' ? data : data.available)
      if (emailAvailable.value) alert("사용 가능한 이메일입니다.")
      else alert("이미 사용 중인 이메일입니다.")
    })
    .catch(() => {
      emailAvailable.value = false
    })
  }
  const updateUser = function(id, user){
    axios.put(`${REST_USER_API_URL}${id}`, user)
    .then((res)=>{
      user.value = res.data
      console.log(res.data)
    })
    .catch((err)=>{
      console.log(err)
    })
  }
  const deleteUser = function(id){
    axios.delete(`${REST_USER_API_URL}${id}`)
    .then((res)=>{
    })
    .catch((err)=>{
      console.log(err)
    })
  }
  // 팔로우
const addFollow = async (targetId) => {
    try {
        await axios.post(`${REST_USER_API_URL}follow`, {
            followingId: loginUserId.value,
            followerId:  targetId
        })
        return true
    } catch (e) {
      console.log(e)
        return false
    }
}

// 언팔로우
const deleteFollow = async (targetId) => {
    try {
        await axios.delete(`${REST_USER_API_URL}follow`, {
            data: { 
                followingId: loginUserId.value,
            followerId:  targetId 
            }
        })
        return true
    } catch (e) {
      console.log(e)
        return false
    }
}


  /* 내가 팔로우하는 목록 */
const followingList = ref([])

const getFollowing = async (userId) => {
  try {
    const res = await axios.get(`${REST_USER_API_URL}follow/following/${userId}`)
    followingList.value = res.data.map(u => ({ ...u, isFollowing: true }))
  } catch (err) {
    console.log(err)
  }
}


/* 나를 팔로우하는 목록 */
const followerList = ref([])

const getFollower = async (userId) => {
  try {
    // 📌 팔로잉 목록 먼저 불러오기
    await getFollowing(userId)

    const res = await axios.get(`${REST_USER_API_URL}follow/follower/${userId}`)
    followerList.value = res.data.map(user => ({
      ...user,
      isFollowing: followingList.value.some(f => f.userId === user.userId)
    }))

  } catch (err) {
    console.log(err)
  }
}

const isPwVerified = ref(false)

const verifyPassword = () => {
  isPwVerified.value = true
}

const resetPwVerified = () => {
  isPwVerified.value = false
}

  return { userList,  getAllUsers, getUserById, addUser, updateUser, deleteUser,
    nicknameAvailable, emailAvailable, checkNickname, checkEmail,addFollow, deleteFollow, 
    followingList, followerList, 
    getFollowing, getFollower, loginUserId, user, isPwVerified, verifyPassword, resetPwVerified, 
    userLogin, userLogout,isAuthenticated,
  }
})
