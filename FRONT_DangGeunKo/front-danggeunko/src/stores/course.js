import { ref } from 'vue'
import { defineStore } from "pinia"
import axios from 'axios'

const REST_API_COURSE_URL = "http://localhost:8080/api-course/course"

export const useCourseStore = defineStore('course', () => {
    const courseList = ref([])
    const course = ref({})
    const searchInfo = ref({ 
        key: 'courseName',
        value: '',
        courseCity: '',
        courseDistrict: '',
        difficulty: '',
        minRange: 1,     
        maxRange: 50,     
        orderBy: 'createdAt', 
        orderByDir: 'desc'    
        })
    // 전체 코스 조회
    const getCourseList = function(){
        axios.get(REST_API_COURSE_URL)
        .then((res)=>{
            courseList.value = res.data
        })
        .catch((err)=>{
            console.log("전체 조회 오류:", err)
        })
    }
    //코스 검색
    const searchCourseList = () =>{
        axios.get(`${REST_API_COURSE_URL}/search`, {
            params: searchInfo.value
        })
        .then((res)=>{
            courseList.value = res.data
        })
        .catch((err)=>{
            console.log("코스 검색 오류:", err)
        })
    }

    //코스 상세 조회(단일)
    const getCourseDetailById= (id) =>{
        axios.get(`${REST_API_COURSE_URL}/${id}`)
        .then((res)=>{
            course.value = res.data
        })
        .catch((err) => {
            console.log("상세 조회 오류:",  err)
        })
    }


    //코스 등록



    //코스 수정


    //코스 삭제


    //코스 랭킹 조회


    //코스 좋아요














    return { course, searchInfo, courseList, getCourseDetailById, getCourseList, searchCourseList }
})