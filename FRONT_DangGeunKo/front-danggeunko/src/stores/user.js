import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'

const REST_USER_API_URL = "http://localhost:8080/api-user/user/"
const loginUserId = ref(10)   // 예시값
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
export const useUserStore = defineStore('user', () => {
  const userList = ref([])
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
    axios.put(`${REST_USER_API_URL}${id}`, user.value)
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






  return { userList,  getAllUsers, getUserById, addUser, updateUser, deleteUser,
    nicknameAvailable, emailAvailable, checkNickname, checkEmail,addFollow, deleteFollow, 
    followingList, followerList, 
    getFollowing, getFollower, loginUserId, user}
})
