import { ref } from 'vue'
import { defineStore } from "pinia"
import axios from 'axios'

const REST_API_REVIEW_URL = "http://localhost:8080/api-review/review"
 
export const useReviewStore = defineStore('review', () => {
    const reviewList = ref([])
    const review = ref({})
    const searchInfo = ref({ 
        courseId: '',
        sortBy: 'createdAt',
        sortDirection: 'desc',
        page: 0,
        size: 10,
        offset: '',
        })
    // 리뷰조회
    const getReviewsByCourse = function(){
        axios.get(REST_API_REVIEW_URL, {
            params: searchInfo.value
        })
        .then((res)=>{
            reviewList.value = res.data
        })
        .catch((err)=>{
            console.log("전체 조회 오류:", err)
        })
    }















    return { reviewList, review, searchInfo, getReviewsByCourse }
})