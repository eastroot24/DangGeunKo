<template>
  <div class="app">
    <div class="top-bar">
      <div class="back-btn" @click="goBack"><i class="fi fi-rs-angle-left"></i></div>
      <div class="title">프로필 수정</div>
    </div>

    <div class="form">
      <label>프로필 사진</label>
      <div class="row">
        <img :src="imagePreview"
          style="width:100px; height:100px; border-radius:50%; object-fit:cover; background-color: #eee;">
        <input type="file" @change="handleFileUpload" accept="image/*">
      </div>

      <label>이름</label>
      <input type="text" v-model="user.userName" disabled class="disabled-input">

      <label>이메일</label>
      <input type="email" v-model="user.userEmail" disabled class="disabled-input">

      <label>별명</label>
      <div class="row">
        <input type="text" v-model="user.nickname" :class="{ 'input-error': errors.nickname }">
        <button class="btn-duple" @click="handleNicknameCheck">중복확인</button>
      </div>
      <div v-if="errors.nickname" class="error-msg">{{ errors.nickname }}</div>
      <div v-else-if="store.nicknameAvailable === true && user.nickname !== originalNickname" class="success-msg">
        사용 가능한 별명입니다.
      </div>
      <div v-else-if="user.nickname === originalNickname && originalNickname !== ''" class="success-msg">
        현재 사용 중인 별명입니다.
      </div>

      <label>새 비밀번호 (변경 시에만 입력)</label>
      <input type="password" v-model="user.userPassword" :class="{ 'input-error': errors.userPassword }">
      <div class="small" :style="{ color: user.userPassword ? (pwRule ? 'green' : 'red') : '#555' }">
        {{ user.userPassword ? (pwRule ? '사용 가능한 비밀번호입니다.' : '영어, 숫자, 특수문자 포함 8자 이상') : '' }}
      </div>
      <div v-if="errors.userPassword" class="error-msg">{{ errors.userPassword }}</div>

      <label>새 비밀번호 확인</label>
      <input type="password" v-model="passwordCheck" :class="{ 'input-error': errors.passwordCheck }">
      <div class="small" :style="{ color: passwordCheck ? (isPwMatch ? 'green' : 'red') : '#555' }">
        {{ passwordCheck ? (isPwMatch ? '비밀번호가 일치합니다' : '비밀번호가 일치하지 않습니다') : '' }}
      </div>
      <div v-if="errors.passwordCheck" class="error-msg">{{ errors.passwordCheck }}</div>

      <label>성별</label>
      <select v-model="user.gender">
        <option value="M">남성</option>
        <option value="F">여성</option>
        <option value="N">선택안함</option>
      </select>

      <label>나이</label>
      <div class="row">
        <input type="number" v-model="user.age">
        <span class="unit">세</span>
      </div>

      <label>지역</label>
      <div class="row">
        <select v-model="user.userCity" @change="user.userDistrict = ''"
          :class="{ 'input-error': errors.userDistrict }">
          <option value="">시/도 선택</option>
          <option v-for="city in Object.keys(regionDB)" :key="city" :value="city">{{ city }}</option>
        </select>

        <select v-model="user.userDistrict" :disabled="!user.userCity" :class="{ 'input-error': errors.userDistrict }">
          <option value="">시/군/구 선택</option>
          <option v-for="gu in regionDB[user.userCity]" :key="gu" :value="gu">{{ gu }}</option>
        </select>
      </div>
      <div v-if="errors.userDistrict" class="error-msg">{{ errors.userDistrict }}</div>

      <label>최근 러닝 평균 거리 (km)</label>
      <input type="number" v-model="user.prefDistance">

      <label>실력 수준</label>
      <select v-model="user.prefDifficulty">
        <option>런린이</option>
        <option>러너</option>
        <option>런고수</option>
      </select>

      <button class="btn-orange" @click="handleUpdate">
        정보 수정 완료
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const store = useUserStore()
const router = useRouter()

// 원래 닉네임을 저장해두기 위한 변수
const originalNickname = ref('')

const user = ref({
  userName: '',
  nickname: '',
  userEmail: '',
  userPassword: '',
  gender: '',
  age: null,
  userCity: '',      // 필드명 변경: region -> userCity
  userDistrict: '',  // 필드명 변경: region -> userDistrict
  prefDistance: '',
  prefDifficulty: '',
  profileImg: null,
})

const passwordCheck = ref('')
const imagePreview = ref('')
const errors = ref({
  nickname: '',
  userPassword: '',
  passwordCheck: '',
  age: '',
  userDistrict: '',
  prefDistance: ''
})

// --- 유효성 검사 로직 ---
const pwRule = computed(() => {
  if (!user.value.userPassword) return true;
  return /^(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$/.test(user.value.userPassword)
})
const isPwMatch = computed(() => user.value.userPassword === passwordCheck.value)

// --- 실시간 감시 (Watch) ---

// 별명 수정 시: 원래 닉네임과 같으면 중복확인 된 것으로 간주
watch(() => user.value.nickname, (newVal) => {
  if (newVal && newVal.trim().length >= 2) errors.value.nickname = '';

  if (newVal === originalNickname.value) {
    store.nicknameAvailable = true; // 본인 원래 닉네임이면 즉시 통과
  } else {
    store.nicknameAvailable = null; // 새 닉네임이면 중복 확인 필요
  }
});

watch(() => user.value.userPassword, () => { if (pwRule.value) errors.value.userPassword = '' });
watch(() => passwordCheck.value, () => { if (isPwMatch.value) errors.value.passwordCheck = '' });
watch(() => user.value.age, (val) => { if (val) errors.value.age = '' });
watch(() => user.value.userDistrict, (val) => { if (val) errors.value.userDistrict = '' });

// --- 데이터 로드 ---
onMounted(async () => {
  await store.getUserById(store.loginUserId);
  const loginUser = store.user
  if (loginUser) {
    // 필드 매핑 및 기존 닉네임 보관
    user.value = {
      ...loginUser,
      userPassword: '',
      userCity: loginUser.userCity || '',
      userDistrict: loginUser.userDistrict || ''
    };
    originalNickname.value = loginUser.nickname;
    const baseUrl = 'http://localhost:8080/profileImg/';
    imagePreview.value = loginUser.profileImg 
      ? `${baseUrl}${loginUser.profileImg}` 
      : `${baseUrl}dgk-default-profile.png`;

    // 초기 상태는 본인 닉네임이므로 통과 상태
    store.nicknameAvailable = true;
  }
})

// --- 핸들러 ---
const handleNicknameCheck = async () => {
  if (user.value.nickname === originalNickname.value) {
    alert("현재 사용 중인 본인의 별명입니다.");
    store.nicknameAvailable = true;
    return;
  }
  if (!user.value.nickname || user.value.nickname.trim().length < 2) {
    errors.value.nickname = '별명은 2자 이상 입력해주세요.';
    return;
  }
  await store.checkNickname(user.value.nickname);
};
const handleFileUpload = (event) => {
  const file = event.target.files[0];
  if (file) {
    // 1. 서버 전송용 파일 객체 저장
    user.value.profileImg = file;

    // 2. 브라우저 미리보기용 URL 생성 및 할당
    // 기존에 생성된 URL이 있다면 메모리 해제를 해주는 것이 좋지만, 
    // 단순 구현 시 아래 한 줄만으로도 미리보기가 작동해야 합니다.
    imagePreview.value = URL.createObjectURL(file);

    console.log("이미지 경로:", imagePreview.value); // 콘솔에서 경로가 찍히는지 확인해보세요.
  }
};
const handleUpdate = async () => {
  let isValid = true;

  if (!user.value.nickname) { errors.value.nickname = '별명을 입력해주세요.'; isValid = false; }
  else if (store.nicknameAvailable !== true) { errors.value.nickname = '별명 중복 확인이 필요합니다.'; isValid = false; }

  if (user.value.userPassword) {
    if (!pwRule.value) { errors.value.userPassword = '비밀번호 형식을 확인해주세요.'; isValid = false; }
    if (!isPwMatch.value) { errors.value.passwordCheck = '비밀번호가 일치하지 않습니다.'; isValid = false; }
  }

  if (!user.value.age) { errors.value.age = '나이를 입력해주세요.'; isValid = false; }
  if (!user.value.userDistrict) { errors.value.userDistrict = '지역을 선택해주세요.'; isValid = false; }

  if (!isValid) return;

  const formData = new FormData();

  // 비밀번호가 비어있으면 기존 비밀번호를 유지하도록 처리 
  const userData = { ...user.value };
  const profileFile = userData.profileImg; // 파일 객체 추출
  delete userData.profileImg; // JSON에는 파일 객체가 포함되면 안 됨

  // 1. 유저 정보 (JSON) 추가
  formData.append("user", new Blob([JSON.stringify(userData)], { type: "application/json" }));

  // 2. 파일 추가 (선택한 파일이 있을 때만)
  if (profileFile) {
    formData.append("file", profileFile);
  }

  // Store 함수 호출 (인자로 formData 전달)
  const success = await store.updateUser(formData);

  if (success) {
    alert("프로필이 수정되었습니다.");
    router.push('/mypage');
  } else {
    alert("수정 중 오류가 발생했습니다.");
  }
}

const goBack = () => router.back();
const regionDB = {
  "서울특별시": ["강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구", "노원구", "도봉구", "동대문구", "동작구",
    "마포구", "서대문구", "서초구", "성동구", "성북구", "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구"],

  "부산광역시": ["강서구", "금정구", "기장군", "남구", "동래구", "부산진구", "북구", "사상구", "사하구", "서구", "수영구", "연제구", "영도구", "중구", "해운대구"],
  "대구광역시": ["군위군", "남구", "달서구", "달성군", "동구", "북구", "서구", "수성구", "중구"],
  "인천광역시": ["강화군", "계양구", "남동구", "동구", "미추홀구", "부평구", "서구", "연수구", "옹진군", "중구"],

  "경기도": [
    "가평군", "고양시 덕양구", "고양시 일산동구", "고양시 일산서구",
    "과천시", "광명시", "광주시", "구리시", "군포시", "김포시",
    "남양주시", "동두천시", "부천시 소사구", "부천시 오정구", "부천시 원미구",
    "성남시 분당구", "성남시 수정구", "성남시 중원구",
    "수원시 권선구", "수원시 영통구", "수원시 장안구", "수원시 팔달구",
    "시흥시", "안산시 단원구", "안산시 상록구", "안성시", "안양시 동안구", "안양시 만안구",
    "양주시", "양평군", "여주시", "연천군", "오산시",
    "용인시 기흥구", "용인시 수지구", "용인시 처인구", "의왕시", "의정부시",
    "이천시", "파주시", "평택시", "포천시", "하남시", "화성시"
  ],

  "강원도": ["강릉시", "고성군", "동해시", "삼척시", "속초시", "양구군", "양양군", "영월군", "원주시", "인제군", "정선군", "철원군", "춘천시", "태백시", "평창군", "홍천군", "화천군"],
  "충청북도": ["괴산군", "단양군", "보은군", "영동군", "옥천군", "음성군", "제천시", "증평군", "진천군", "청주시 상당구", "청주시 서원구", "청주시 청원구", "청주시 흥덕구", "충주시"],
  "충청남도": ["계룡시", "공주시", "금산군", "논산시", "당진시", "보령시", "부여군", "서산시", "아산시", "예산군", "천안시 동남구", "천안시 서북구", "청양군", "태안군", "홍성군"],
  "전라북도": ["고창군", "군산시", "김제시", "남원시", "무주군", "부안군", "순창군", "완주군", "익산시", "임실군", "장수군", "전주시 덕진구", "전주시 완산구", "정읍시", "진안군"],
  "전라남도": ["강진군", "고흥군", "곡성군", "광양시", "구례군", "나주시", "담양군", "목포시", "무안군", "보성군", "순천시", "신안군", "여수시", "영광군", "영암군", "완도군", "장성군", "장흥군", "진도군", "함평군", "해남군", "화순군"],
  "경상북도": ["경산시", "경주시", "고령군", "구미시", "김천시", "문경시", "봉화군", "상주시", "성주군", "안동시", "영덕군", "영양군", "영주시", "영천시", "예천군", "울릉군", "울진군", "의성군", "청도군", "청송군", "칠곡군", "포항시 남구", "포항시 북구"],
  "경상남도": ["거제시", "거창군", "고성군", "김해시", "남해군", "밀양시", "사천시", "산청군", "양산시", "의령군", "진주시", "진해구", "창녕군", "창원시 마산합포구", "창원시 마산회원구", "창원시 성산구", "창원시 의창구", "창원시 진해구", "통영시", "하동군", "합천군"],
  "제주특별자치도": ["제주시", "서귀포시"]
}
</script>

<style scoped>
.input-error {
  border: 1px solid #ff4d4f !important;
}

.error-msg {
  color: #ff4d4f;
  font-size: 11px;
  margin-top: 4px;
  font-weight: bold;
}
.success-msg {
  color: #52c41a;
  font-size: 11px;
  margin-top: 4px;
  font-weight: bold;
}

.disabled-input {
  background-color: #f5f5f5;
  color: #888;
  cursor: not-allowed;
  border: 1px solid #ddd;
}

.form{
  height: 100vh;
  overflow-y: auto;
  padding-bottom: 200px;
}

.form::-webkit-scrollbar{
  width: 0.5rem;
}

/* ★ 패널 스크롤바 핸들 (평소 색상) ★ */
.form::-webkit-scrollbar-thumb {
  background: #ff8a24;         /* 주황색 적용 */
  border-radius: 0.625rem;
  border: 0.0625rem solid #fff;      /* 주황색이 너무 답답해 보이지 않게 살짝 여백 */
}

/* 호버 시 조금 더 진한 주황색 */
.form::-webkit-scrollbar-thumb:hover {
  background: #e66e00;
}
</style>