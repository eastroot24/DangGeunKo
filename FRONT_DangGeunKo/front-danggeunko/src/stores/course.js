import { ref } from 'vue'
import { defineStore } from "pinia"
import axios from 'axios'

const REST_API_URL = "http://localhost:8080/api-course/course"

export const useCourseStore = defineStore('course', () => {
    const CourseList = ref([])

    const getCourseList = function(){
        axios.get(REST_API_URL)
        .then((res)=>{
            CourseList.value = res.data
        })
        .catch((err)=>{
            console.log(err)
        })
    }

    return { CourseList, getCourseList }
})