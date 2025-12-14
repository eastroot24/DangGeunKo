import { ref } from 'vue'
import { defineStore, storeToRefs } from "pinia"
import axios from 'axios'
import { useUserStore } from './user'

const REST_API_COURSE_URL = "http://localhost:8080/api-course/course"

export const useCourseStore = defineStore('course', () => {
    const userStore = useUserStore();
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
    const getCourseList = async () => {
        // loginUserId는 pinia store에 저장된 값이므로 null 또는 실제 ID가 될 수 있습니다.
        const userId = userStore.loginUserId; 
        const params = userId ? { userId: userId } : {};
        try {
            // userId가 없으면 파라미터 없이 요청이 전송됨
            const response = await axios.get(REST_API_COURSE_URL, {
                params: params,
            });
            courseList.value = response.data;
            // console.log(userId)
            // console.log(courseList.value)

            // 비회원의 경우 isLiked는 SQL에 의해 FALSE로 설정되어 전송됩니다.
        } catch (error) {
            console.error("코스 목록 조회 실패:", error);
        }
    };
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
    const getCourseDetailById = async (id, visited=false) => { 
        try {
            const res = await axios.get(`${REST_API_COURSE_URL}/${id}`, {
                params: {
                    visited,
                }
            }) 
            course.value = res.data
            // console.log(course.value)
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
    const addLike = async (courseId) => {
        const userId = userStore.loginUserId; 
        const url = `${REST_API_COURSE_URL}/${courseId}/like?userId=${userId}`; // ⭐️ 쿼리 파라미터로 userId 전달

        try {
            const response = await axios.post(url);
            await getCourseList()
            // console.log(response.data)
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