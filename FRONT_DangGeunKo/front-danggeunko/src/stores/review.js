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

    //리뷰 등록
    const addReview = async (review) => {
        try{
            await axios.post(`${REST_API_REVIEW_URL}`, review)
            searchInfo.value.courseId = reviewData.courseId;
            await getReviewsByCourse()
        }
        catch (err) {
            console.log("리뷰 등록 오류:", err)
        }
    }
    //리뷰 수정
    const updateReview = async (review) => {
        try{
            await axios.put(`${REST_API_REVIEW_URL}/${review.reviewId}`, review)
            await getReviewsByCourse()
        }
        catch (err) {
            console.log("리뷰 수정 오류:", err)
        }
    }

    //리뷰 삭제
    const deleteReview = async (id) => {
        try{
            await  axios.delete(`${REST_API_REVIEW_URL}/${id}`)
            await getReviewsByCourse()
        }
        catch (err) {
            console.log("리뷰 삭제 오류:", err)
        }
    }

    return { reviewList, review, searchInfo, 
        getReviewsByCourse, addReview, updateReview, deleteReview }
})