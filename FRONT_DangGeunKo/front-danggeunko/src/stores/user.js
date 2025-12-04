import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'

const REST_USER_API_URL = "http://localhost:8080/api-user/user"

export const useUserStore = defineStore('user', () => {
  const userList = ref([])
  const getUserList = () => {
    axios.get(REST_USER_API_URL)
    .then((res) => {
        userList.value = res.data
        console.log(res.data)
    })
  }

  return { userList,  getUserList}
})
