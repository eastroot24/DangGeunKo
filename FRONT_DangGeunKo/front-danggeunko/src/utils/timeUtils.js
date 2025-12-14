
/**
 * 주어진 날짜/시간 문자열을 현재 시간을 기준으로 상대적인 시간으로 변환합니다.
 * 예: 3분 전, 2시간 전, 1일 전
 * @param {string} dateString 'YYYY-MM-DD HH:mm:ss' 형식의 날짜 문자열
 * @returns {string} 상대적인 시간 문자열
 */
export function timeAgo(dateString) {
    // 1. 입력된 시간을 Date 객체로 변환
    // Spring Boot에서 넘어온 ISO 8601 형식 문자열도 Date 객체로 잘 변환됩니다.
    const date = new Date(dateString); 
    const now = new Date();
    
    // 2. 시간 차이 계산 (밀리초 단위)
    const seconds = Math.floor((now - date) / 1000);

    // 3. 시간 단위 정의
    let interval = Math.floor(seconds / 31536000); // 1년
    if (interval >= 1) {
        return interval + "년 전";
    }
    interval = Math.floor(seconds / 2592000); // 1개월 (30일 기준)
    if (interval >= 1) {
        return interval + "개월 전";
    }
    interval = Math.floor(seconds / 86400); // 1일
    if (interval >= 1) {
        return interval + "일 전";
    }
    interval = Math.floor(seconds / 3600); // 1시간
    if (interval >= 1) {
        return interval + "시간 전";
    }
    interval = Math.floor(seconds / 60); // 1분
    if (interval >= 1) {
        return interval + "분 전";
    }
    
    // 1분 미만
    return "방금 전"; 
}