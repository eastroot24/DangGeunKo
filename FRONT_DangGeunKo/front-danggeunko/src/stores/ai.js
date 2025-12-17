import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "axios";

// 백엔드(Spring)의 주소
const REST_API_AI_URL =  "http://localhost:8080/api-ai"

export const useAIStore = defineStore("ai", () => {
  // ======== State ========
  const comment = ref('');
  const loading = ref(false);
  const error = ref(null);

  // ======== Actions ========
  const sendMemoryMessage = async (text) => {
    if (!text) return;
    loading.value = true;
    
    try {
      const res = await axios.post(REST_API_AI_URL, { message: text });
      // 배열 push가 아니라 응답 텍스트를 바로 대입
      comment.value = res.data.answer; 
    } catch (err) {
      console.error(err);
      comment.value = "추천 정보를 가져오는 데 실패했습니다.";
    } finally {
      loading.value = false;
    }
  };

  const clearMemory = () => {
    comment.value = '';
  };

  return {
    // state
    comment,
    loading,
    error,

    // actions
    sendMemoryMessage,
    clearMemory,
  };
});
