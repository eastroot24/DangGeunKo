<template>
  <div class="app">
    <div class="top-bar">
      <div class="back" @click="goLogin">←</div>
      <div class="title">회원가입</div>
    </div>

    <div class="form">
      <label>프로필 사진</label>
      <div class="row">
        <img :src="imagePreview"
          style="width:100px; height:100px; border-radius:50%; object-fit:cover; background-color: #eee;">
        <input type="file" @change="handleFileUpload" accept="image/*">
      </div>

      <label>이름</label>
      <input type="text" v-model="user.userName" :class="{ 'input-error': errors.userName }">
      <div v-if="errors.userName" class="error-msg">{{ errors.userName }}</div>

      <label>이메일</label>
      <div class="row">
        <input type="email" v-model="user.userEmail" :class="{ 'input-error': errors.userEmail }" required>
        <button class="btn-duple" @click="handleEmailCheck">중복확인</button>
      </div>
      <div v-if="errors.userEmail" class="error-msg">{{ errors.userEmail }}</div>

      <label>별명</label>
      <div class="row">
        <input type="text" v-model="user.nickname" :class="{ 'input-error': errors.nickname }">
        <button class="btn-duple" @click="handleNicknameCheck">중복확인</button>
      </div>
      <div v-if="errors.nickname" class="error-msg">{{ errors.nickname }}</div>

      <label>비밀번호</label>
      <input type="password" v-model="user.userPassword" :class="{ 'input-error': errors.userPassword }">
      <div class="small" :style="{ color: user.userPassword ? (pwRule ? 'green' : 'red') : '#555' }">
        {{
          user.userPassword
            ? (pwRule
              ? '사용 가능한 비밀번호입니다.'
              : '비밀번호는 영어, 숫자, 특수문자를 포함하여 8자 이상이어야 합니다.'
            )
            : ''
        }}
      </div>
      <div v-if="errors.userPassword" class="error-msg">{{ errors.userPassword }}</div>

      <label>비밀번호 확인</label>
      <input type="password" v-model="passwordCheck" :class="{ 'input-error': errors.passwordCheck }">
      <div class="small" :style="{ color: passwordCheck ? (isPwMatch ? 'green' : 'red') : '#555' }">
        {{ passwordCheck ? (isPwMatch ? '비밀번호가 일치합니다' : '비밀번호가 일치하지 않습니다') : '' }}
      </div>
      <div v-if="errors.passwordCheck" class="error-msg">{{ errors.passwordCheck }}</div>

      <label>성별</label>
      <div class="row gender">
        <select v-model="user.gender" :class="{ 'input-error': errors.gender }">
          <option value="" disabled selected>성별 선택</option>
          <option value="M">남성</option>
          <option value="F">여성</option>
          <option value="N">선택안함</option>
        </select>
      </div>
      <div v-if="errors.gender" class="error-msg">{{ errors.gender }}</div>

      <label>나이</label>
      <div class="row">
        <input type="number" v-model="user.age" placeholder="숫자 입력" :class="{ 'input-error': errors.age }">
        <span style="padding-top:12px;font-size:13px;">세</span>
      </div>
      <div v-if="errors.age" class="error-msg">{{ errors.age }}</div>

      <label>지역</label>
      <div class="row">
        <select v-model="selectedCity" :class="{ 'input-error': errors.region }">
          <option value="">시/도 선택</option>
          <option v-for="city in Object.keys(regionDB)" :key="city" :value="city">
            {{ city }}
          </option>
        </select>

        <select v-model="user.region" :disabled="!selectedCity" :class="{ 'input-error': errors.region }">
          <option value="">시/군/구 선택</option>
          <option v-for="gu in regionDB[selectedCity]" :key="gu" :value="gu">
            {{ gu }}
          </option>
        </select>
      </div>
      <div v-if="errors.region" class="error-msg">{{ errors.region }}</div>

      <label>최근 러닝 평균 거리</label>
      <div class="row">
        <input type="number" v-model="user.prefDistance" placeholder="숫자 입력"
          :class="{ 'input-error': errors.prefDistance }">
        <span style="padding-top:12px;font-size:13px;">km</span>
      </div>
      <div v-if="errors.prefDistance" class="error-msg">{{ errors.prefDistance }}</div>

      <label>실력 수준</label>
      <select v-model="user.prefDifficulty" :class="{ 'input-error': errors.prefDifficulty }">
        <option value="" disabled selected>수준 선택</option>
        <option>런린이</option>
        <option>러너</option>
        <option>런고수</option>
      </select>
      <div v-if="errors.prefDifficulty" class="error-msg">{{ errors.prefDifficulty }}</div>

      <button class="btn-orange" @click="goHome">
        회원가입
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user';

const store = useUserStore()
const router = useRouter()
const passwordCheck = ref('')
const isPwMatch = computed(() => user.value.userPassword === passwordCheck.value)
const pwRule = computed(() => {
  const pw = user.value.userPassword
  const regex = /^(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$/
  return regex.test(pw)
})
const emailRule = computed(() => {
  const email = user.value.userEmail;
  // 표준 이메일 형식 정규표현식
  const regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  return regex.test(email);
});
// <script setup> 내부에 추가
const handleEmailCheck = () => {
  // 1. 에러 메시지 초기화
  errors.value.userEmail = '';

  // 2. 이메일 형식 검사 (emailRule 사용)
  if (!user.value.userEmail) {
    errors.value.userEmail = '이메일을 입력해주세요.';
    return;
  }

  if (!emailRule.value) {
    errors.value.userEmail = '올바른 이메일 형식이 아닙니다.';
    return;
  }

  // 3. 형식이 올바를 때만 서버에 중복 확인 요청
  store.checkEmail(user.value.userEmail);
};
const handleNicknameCheck = () => {
  // 1. 에러 메시지 초기화
  errors.value.nickname = '';

  // 2. 빈 값 검사
  if (!user.value.nickname || user.value.nickname.trim() === '') {
    errors.value.nickname = '별명을 입력해주세요.';
    return;
  }

  // (선택사항) 추가 규칙이 있다면 여기에 작성 (예: 2자 이상)
  if (user.value.nickname.length < 2) {
    errors.value.nickname = '별명은 2자 이상 입력해주세요.';
    return;
  }

  // 3. 검사를 통과했을 때만 서버에 중복 확인 요청
  store.checkNickname(user.value.nickname);
};
const user = ref({
  userName: '',
  nickname: '',
  userEmail: '',
  userPassword: '',
  gender: '',
  age: null,
  region: '',
  prefDistance: '',
  prefDifficulty: '',
  profileImg: null,
})
const imagePreview = ref('http://localhost:8080/uploads/dgk-default-profile.png');
// <script setup> 내부
const errors = ref({
  userName: '',
  nickname: '',
  userEmail: '',
  userPassword: '',
  passwordCheck: '',
  gender: '',
  age: '',
  region: '',
  prefDistance: '',
  prefDifficulty: ''
});
// 파일 선택 핸들러
const handleFileUpload = (event) => {
  const file = event.target.files[0];
  if (file) {
    user.value.profileImg = file;
    // 파일을 선택하면 선택한 파일의 미리보기로 교체
    imagePreview.value = URL.createObjectURL(file);
  }
};

const goHome = async () => {
  // 에러 초기화
  Object.keys(errors.value).forEach(key => errors.value[key] = '');

  let isValid = true;

  // 필수 항목 체크
  if (!user.value.userName) { errors.value.userName = '이름을 입력해주세요.'; isValid = false; }
  if (!user.value.userEmail) { errors.value.userEmail = '이메일을 입력해주세요.'; isValid = false; }
  else if (store.emailAvailable !== true) { errors.value.userEmail = '이메일 중복 확인이 필요합니다.'; isValid = false; }

  if (!user.value.nickname) { errors.value.nickname = '별명을 입력해주세요.'; isValid = false; }
  else if (store.nicknameAvailable !== true) { errors.value.nickname = '별명 중복 확인이 필요합니다.'; isValid = false; }

  if (!user.value.userPassword) { errors.value.userPassword = '비밀번호를 입력해주세요.'; isValid = false; }
  else if (!pwRule.value) { errors.value.userPassword = '비밀번호 형식이 올바르지 않습니다.'; isValid = false; }

  if (!passwordCheck.value) { errors.value.passwordCheck = '비밀번호 확인을 입력해주세요.'; isValid = false; }
  else if (!isPwMatch.value) { errors.value.passwordCheck = '비밀번호가 일치하지 않습니다.'; isValid = false; }

  if (!user.value.gender) { errors.value.gender = '성별을 선택해주세요.'; isValid = false; }
  if (!user.value.age) { errors.value.age = '나이를 입력해주세요.'; isValid = false; }
  if (!user.value.region) { errors.value.region = '지역을 선택해주세요.'; isValid = false; }
  if (!user.value.prefDistance) { errors.value.prefDistance = '거리를 입력해주세요.'; isValid = false; }
  if (!user.value.prefDifficulty) { errors.value.prefDifficulty = '실력 수준을 선택해주세요.'; isValid = false; }

  if (!isValid) {
    // 가장 상단에 있는 에러 메시지로 스크롤 이동 (선택 사항)
    window.scrollTo({ top: 0, behavior: 'smooth' });
    return;
  }

  // 유효성 검사 통과 시 기존 FormData 로직 실행
  const formData = new FormData();
  const userData = { ...user.value };
  const profileFile = userData.profileImg;
  delete userData.profileImg;

  formData.append("user", new Blob([JSON.stringify(userData)], { type: "application/json" }));
  if (profileFile) formData.append("file", profileFile);

  await store.addUser(formData);
  router.replace('/login');
}


const goLogin = () => {
  router.push({ name: "login" })
}

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
  "전남": ["강진군", "고흥군", "곡성군", "광양시", "구례군", "나주시", "담양군", "목포시", "무안군", "보성군", "순천시", "신안군", "여수시", "영광군", "영암군", "완도군", "장성군", "장흥군", "진도군", "함평군", "해남군", "화순군"],
  "경북": ["경산시", "경주시", "고령군", "구미시", "김천시", "문경시", "봉화군", "상주시", "성주군", "안동시", "영덕군", "영양군", "영주시", "영천시", "예천군", "울릉군", "울진군", "의성군", "청도군", "청송군", "칠곡군", "포항시 남구", "포항시 북구"],
  "경남": ["거제시", "거창군", "고성군", "김해시", "남해군", "밀양시", "사천시", "산청군", "양산시", "의령군", "진주시", "진해구", "창녕군", "창원시 마산합포구", "창원시 마산회원구", "창원시 성산구", "창원시 의창구", "창원시 진해구", "통영시", "하동군", "합천군"],
  "제주특별자치도": ["제주시", "서귀포시"]
}

const selectedCity = ref("")

</script>
<style scoped>
.input-error {
  border: 1px solid #ff4d4f !important;
}

.error-msg {
  color: #ff4d4f;
  font-size: 12px;
  margin-top: 4px;
  margin-left: 4px;
  font-weight: bold;
}
</style>