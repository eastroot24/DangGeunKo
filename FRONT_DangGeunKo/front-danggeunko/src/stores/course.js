import { ref } from 'vue'
import { defineStore, storeToRefs } from "pinia"
import axios from 'axios'
import { useUserStore } from './user'
import { useMapStore } from './map'

const REST_API_COURSE_URL = "http://localhost:8080/api-course/course"

export const useCourseStore = defineStore('course', () => {
    const userStore = useUserStore();
    const mapStore = useMapStore();
    const courseList = ref([])
    const registCourseList = ref([])
    const likeCourseList = ref([])
    const rankingCourseList = ref([])
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
        orderByDir: 'desc',
        userId: userStore.loginUserId ? userStore.loginUserId : '',   
        })
   const setCourseMarkers = (courses, isRanking = false) => {
    const dataArray = Array.isArray(courses) ? courses : (courses ? [courses] : []);
    const loginUserId = userStore.loginUserId;

    const markers = dataArray.map((course, index) => {
        let markerType = 'default'; 

        // 1. 우선순위 판별
        if (isRanking && index === 0) {
            // 랭킹 리스트이고 1등인 경우 (최우선)
            markerType = 'rank1';
        } else if (loginUserId && String(course.userId) === String(loginUserId)) {
            // 내가 등록한 코스인 경우
            markerType = 'regist';
        } else if (course.liked) {
            markerType = 'like';
        }

        return {
            id: course.courseId,
            lat: course.startLat,
            lng: course.startLng,
            name: course.courseName,
            type: markerType,
        };
    });
    
    mapStore.setMarkers(markers);
};


    // 전체 코스 조회
    const getCourseList = async () => {
        // loginUserId는 pinia store에 저장된 값이므로 null 또는 실제 ID가 될 수 있습니다.
        const userId = userStore.loginUserId; 
        const params = userId ? { userId: userId } : {};
        try {
            const response = await axios.get(REST_API_COURSE_URL, {
                params: params,
            });
            courseList.value = response.data;
            setCourseMarkers(courseList.value); // 마커 표시 로직 실행
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
            const data = res.data || []
            courseList.value = data
            setCourseMarkers(courseList.value);
            if (data.length > 0) {
                const firstCourse = data[0];
                // 서버 필드명(startLat, startLng) 확인 필수
                if (firstCourse.startLat && firstCourse.startLng) {
                    mapStore.setMapCenter({
                        lat: firstCourse.startLat,
                        lng: firstCourse.startLng,
                        zoom: 14 // 적절한 줌 레벨 설정
                    });
                }
            }
        })
        .catch((err)=>{
            console.log("코스 검색 오류:", err)
        })
    }

    //검색 초기화
    const resetSearchInfo = () => {
        searchInfo.value = {
    key: 'courseName',
    value: '',
    courseCity: '',
    courseDistrict: '',
    difficulty: '',
    minRange: 1,
    maxRange: 50,
    orderBy: 'createdAt',
    orderByDir: 'desc'
        }
    }

    //코스 상세 조회(단일)
    const getCourseDetailById = async (id, visited=false) => { 
        try {
            const userId = userStore.loginUserId
            const res = await axios.get(`${REST_API_COURSE_URL}/${id}`, {
                params: {
                    visited,
                    userId,
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
// 코스 랭킹 조회
const getWeeklyRanking = async () => {
    try {
        const res = await axios.get(`${REST_API_COURSE_URL}/ranking`, {
            params: searchInfo.value
        });
        
        const data = res.data || [];
        rankingCourseList.value = data;

        // 마커 생성 및 지도 중심 이동
        setCourseMarkers(rankingCourseList.value, true);

        if (data.length > 0 && data[0].startLat && data[0].startLng) {
            mapStore.setMapCenter({
                lat: data[0].startLat,
                lng: data[0].startLng,
                zoom: 14
            });
        }
    } catch (err) {
        console.error("랭킹 로드 중 에러 발생:", err);
    }
};

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
    const url = `${REST_API_COURSE_URL}/${courseId}/like?userId=${userId}`;

    try {
        const response = await axios.post(url);
        // 여기서 getCourseList()를 호출하는 것은 전체 리스트용이므로 
        // 마이페이지 실시간 반영과는 직접적 연관이 적을 수 있습니다. 필요없다면 제거하거나 유지하세요.
        await getCourseList(); 
        return response.data; // 서버에서 바뀐 하트 상태(true/false)를 반환한다고 가정
    } catch (err) {
        console.error("코스 좋아요 토글 에러:", err);
        throw err;
    }
};



    //등록한 코스 리스트
   // stores/course.js
    const getRegistCourseList = async (targetId) => {
        try {
            const response = await axios.get(`${REST_API_COURSE_URL}/regist`, {
                params: { 
                    targetUserId: targetId, // 목록 주인
                    loginUserId: userStore.loginUserId // 하트 색깔 결정 주체
                }
            });
            registCourseList.value = response.data;
        } catch (error) {
            console.error("목록 로드 실패", error);
        }
    };

// getLikeCourseList도 동일하게 targetUserId와 loginUserId를 params로 전송하도록 수정하세요.
    //찜한 코스 리스트
     const getLikeCourseList = async (targetId) => {
        try {
            const response = await axios.get(`${REST_API_COURSE_URL}/like`, {
                params: { 
                    targetUserId: targetId, // 목록 주인
                    loginUserId: userStore.loginUserId // 하트 색깔 결정 주체
                }
            });
            likeCourseList.value = response.data;
        } catch (error) {
            console.error("목록 로드 실패", error);
        }
    };





    return { course, searchInfo, courseList, registCourseList, likeCourseList, rankingCourseList, resetSearchInfo,
        getCourseDetailById, getCourseList, searchCourseList, 
        getWeeklyRanking, registCourse, updateCourseById,
        deleteCourseById, addLike, setCourseMarkers, getRegistCourseList, getLikeCourseList}
})