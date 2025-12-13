import { ref } from 'vue'
import { defineStore, storeToRefs } from "pinia"
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
    const getCourseDetailById = async (id) => { 
        try {
            const res = await axios.get(`${REST_API_COURSE_URL}/${id}`) 
            course.value = res.data
            return res.data; // Promise 반환을 위해 데이터 반환
        } catch (err) {
            console.log("상세 조회 오류:", err)
            throw err; // 오류 발생 시 던져서 호출자가 알 수 있게 함
        }
    }   

    //코스 랭킹 조회
    const getWeeklyRanking= () =>{
        axios.get(`${REST_API_COURSE_URL}/ranking`, {
            params: searchInfo.value
        })
        .then((res)=>{
            courseList.value = res.data
        })
        .catch((err)=>{
            console.log("검색조건:", searchInfo)
            console.log("랭킹 검색 오류:", err)
        })
    } 

    //코스 등록
    const registCourse = async function(course) {
        const response = await axios.post(REST_API_COURSE_URL, course)
        
        return response.data.courseId;
    };


    //코스 수정
    const updateCourseById = async (course) => {
       try{
        await  axios.put(`${REST_API_COURSE_URL}/${course.courseId}`, course)
        return true
       }catch (err){
        console.log("수정실패: ", err)
        return false
       }
    }

    //코스 삭제
    const deleteCourseById = async (id) => {
        try {
            await axios.delete(`${REST_API_COURSE_URL}/${id}`)
            return true 
        } catch (err) {
            console.error("삭제 실패:", err)
            return false 
        }
    }


    //코스 좋아요
    const addLike = async (courseId, userId) => {
        
        const url = `${REST_API_COURSE_URL}/${courseId}/like?userId=${userId}`; // ⭐️ 쿼리 파라미터로 userId 전달

        try {
            const response = await axios.post(url);
            console.log(response.data)
            return response.data
        } catch (err) {
            console.error("코스 좋아요 토글 에러:", err);
            throw new Error("좋아요 상태 변경에 실패했습니다.");
        }
    };













    return { course, searchInfo, courseList, 
        getCourseDetailById, getCourseList, searchCourseList, 
        getWeeklyRanking, registCourse, updateCourseById,
        deleteCourseById, addLike,  }
})