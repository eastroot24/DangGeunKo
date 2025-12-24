<template>
  <div class="app">
    <div class="top-bar">
      <div class="back-btn" @click="goLogin"><i class="fi fi-rs-angle-left"></i></div>
      <div class="title">회원가입</div>
    </div>

    <div class="form">
      <div class="profile-section">
        <div class="profile-image-container">
          <img :src="imagePreview" class="profile-preview">
          <label for="file-input" class="edit-icon-btn">
            <i class="fi fi-rs-pencil"></i>
          </label>
          <input id="file-input" type="file" @change="handleFileUpload" accept="image/*" style="display: none;">
        </div>
        <label class="profile-label">프로필 사진</label>
      </div>

      <label>이름</label>
      <input type="text" v-model="user.userName" :class="{ 'input-error': errors.userName }" placeholder="이름을 입력해주세요">
      <div v-if="errors.userName" class="error-msg">{{ errors.userName }}</div>

      <label>이메일</label>
      <div class="row">
        <input class="duple-input" type="email" v-model="user.userEmail" :class="{ 'input-error': errors.userEmail }"
          placeholder="example@email.com">
        <button class="mini-btn" @click="handleEmailCheck">중복확인</button>
        <div v-if="errors.userEmail" class="error-msg">{{ errors.userEmail }}</div>
        <div v-else-if="user.userEmail && emailRule && store.emailAvailable === null" class="success-msg">
          사용 가능한 형식입니다. 중복 확인을 눌러주세요.
        </div>
        <div v-else-if="store.emailAvailable === true" class="success-msg">
          사용 가능한 이메일입니다.
        </div>
      </div>


      <label>별명</label>
      <div class="row">
        <input class="duple-input" type="text" v-model="user.nickname" :class="{ 'input-error': errors.nickname }"
          placeholder="2자 이상 입력">
        <button class="mini-btn" @click="handleNicknameCheck">중복확인</button>
        <div v-if="errors.nickname" class="error-msg">{{ errors.nickname }}</div>
        <div v-else-if="store.nicknameAvailable === true" class="success-msg">사용 가능한 별명입니다.</div>
      </div>


      <label>비밀번호</label>
      <input type="password" v-model="user.userPassword" :class="{ 'input-error': errors.userPassword }"
        placeholder="영어, 숫자, 특수문자 포함 8자 이상">
      <div class="small" :style="{ color: user.userPassword ? (pwRule ? 'green' : 'red') : '#555' }">
        {{ user.userPassword ? (pwRule ? '사용 가능한 비밀번호입니다.' : '영어, 숫자, 특수문자 포함 8자 이상') : '' }}
      </div>
      <div v-if="errors.userPassword" class="error-msg">{{ errors.userPassword }}</div>

      <label>비밀번호 확인</label>
      <input type="password" v-model="passwordCheck" :class="{ 'input-error': errors.passwordCheck }">
      <div class="small" :style="{ color: passwordCheck ? (isPwMatch ? 'green' : 'red') : '#555' }">
        {{ passwordCheck ? (isPwMatch ? '비밀번호가 일치합니다' : '비밀번호가 일치하지 않습니다') : '' }}
      </div>
      <div v-if="errors.passwordCheck" class="error-msg">{{ errors.passwordCheck }}</div>

      <label>성별</label>
      <select v-model="user.gender" :class="{ 'input-error': errors.gender }">
        <option value="" disabled selected>성별 선택</option>
        <option value="M">남성</option>
        <option value="F">여성</option>
        <option value="N">선택안함</option>
      </select>
      <div v-if="errors.gender" class="error-msg">{{ errors.gender }}</div>

      <label>나이</label>
      <div class="row">
        <input type="number" v-model="user.age" placeholder="숫자 입력" :class="{ 'input-error': errors.age }">
      </div>
      <div v-if="errors.age" class="error-msg">{{ errors.age }}</div>

      <label>지역</label>
      <div class="row">
        <select v-model="user.userCity" :class="{ 'input-error': errors.userDistrict }">
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
      <input type="number" v-model="user.prefDistance" placeholder="숫자 입력"
        :class="{ 'input-error': errors.prefDistance }">
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
        회원가입 완료
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user';

const store = useUserStore()
const router = useRouter()

const user = ref({
  userName: '',
  nickname: '',
  userEmail: '',
  userPassword: '',
  gender: '',
  age: null,
  userCity: '',
  userDistrict: '',
  prefDistance: '',
  prefDifficulty: '',
  profileImg: null,
})

const passwordCheck = ref('')
// 초기 기본 이미지 경로 설정
const imagePreview = ref('http://localhost:8080/profileImg/dgk-default-profile.png');

const errors = ref({
  userName: '', nickname: '', userEmail: '', userPassword: '',
  passwordCheck: '', gender: '', age: '', userDistrict: '',
  prefDistance: '', prefDifficulty: ''
});

const emailRule = computed(() => /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(user.value.userEmail));
const pwRule = computed(() => /^(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$/.test(user.value.userPassword));
const isPwMatch = computed(() => user.value.userPassword === passwordCheck.value);

// Watchers
watch(() => user.value.userEmail, (newVal) => {
  if (!newVal) errors.value.userEmail = '';
  else if (!emailRule.value) errors.value.userEmail = '올바른 이메일 형식이 아닙니다.';
  else errors.value.userEmail = '';
  store.emailAvailable = null;
});

watch(() => user.value.nickname, (newVal) => {
  if (newVal && newVal.trim().length >= 2) errors.value.nickname = '';
  store.nicknameAvailable = null;
});

watch(() => user.value.userName, (val) => { if (val) errors.value.userName = '' });
watch(() => user.value.userPassword, () => { if (pwRule.value) errors.value.userPassword = '' });
watch(() => passwordCheck.value, () => { if (isPwMatch.value) errors.value.passwordCheck = '' });
watch(() => user.value.gender, (val) => { if (val) errors.value.gender = '' });
watch(() => user.value.age, (val) => { if (val) errors.value.age = '' });
watch(() => user.value.userDistrict, (val) => { if (val) errors.value.userDistrict = '' });
watch(() => user.value.prefDistance, (val) => { if (val) errors.value.prefDistance = '' });
watch(() => user.value.prefDifficulty, (val) => { if (val) errors.value.prefDifficulty = '' });

// Handlers
const handleEmailCheck = () => {
  if (!emailRule.value) { errors.value.userEmail = '올바른 이메일 형식이 아닙니다.'; return; }
  store.checkEmail(user.value.userEmail);
};

const handleNicknameCheck = () => {
  if (!user.value.nickname || user.value.nickname.length < 2) {
    errors.value.nickname = '별명은 2자 이상 입력해주세요.';
    return;
  }
  store.checkNickname(user.value.nickname);
};

const handleFileUpload = (event) => {
  const file = event.target.files[0];
  if (file) {
    user.value.profileImg = file;
    imagePreview.value = URL.createObjectURL(file);
  }
};

const goHome = async () => {
  let isValid = true;
  if (!user.value.userName) { errors.value.userName = '이름을 입력해주세요.'; isValid = false; }
  if (!emailRule.value) { errors.value.userEmail = '이메일 형식을 확인해주세요.'; isValid = false; }
  else if (store.emailAvailable !== true) { errors.value.userEmail = '이메일 중복 확인이 필요합니다.'; isValid = false; }

  if (!user.value.nickname) { errors.value.nickname = '별명을 입력해주세요.'; isValid = false; }
  else if (store.nicknameAvailable !== true) { errors.value.nickname = '별명 중복 확인이 필요합니다.'; isValid = false; }

  if (!pwRule.value) { errors.value.userPassword = '비밀번호 형식을 확인해주세요.'; isValid = false; }
  if (!isPwMatch.value) { errors.value.passwordCheck = '비밀번호가 일치하지 않습니다.'; isValid = false; }

  if (!user.value.gender) { errors.value.gender = '성별을 선택해주세요.'; isValid = false; }
  if (!user.value.age) { errors.value.age = '나이를 입력해주세요.'; isValid = false; }
  if (!user.value.userDistrict) { errors.value.userDistrict = '지역을 선택해주세요.'; isValid = false; }
  if (!user.value.prefDistance) { errors.value.prefDistance = '거리를 입력해주세요.'; isValid = false; }
  if (!user.value.prefDifficulty) { errors.value.prefDifficulty = '실력 수준을 선택해주세요.'; isValid = false; }

  if (!isValid) return;

  const formData = new FormData();
  const userData = { ...user.value };
  const profileFile = userData.profileImg;
  delete userData.profileImg;

  formData.append("user", new Blob([JSON.stringify(userData)], { type: "application/json" }));
  if (profileFile) formData.append("file", profileFile);

  await store.addUser(formData);
  router.replace('/login');
}

const goLogin = () => router.push('/login');

const regionDB = {
  "서울특별시": ["강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구", "노원구", "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구", "성동구", "성북구", "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구"],
  "부산광역시": ["강서구", "금정구", "기장군", "남구", "동래구", "부산진구", "북구", "사상구", "사하구", "서구", "수영구", "연제구", "영도구", "중구", "해운대구"],
  "대구광역시": ["군위군", "남구", "달서구", "달성군", "동구", "북구", "서구", "수성구", "중구"],
  "인천광역시": ["강화군", "계양구", "남동구", "동구", "미추홀구", "부평구", "서구", "연수구", "옹진군", "중구"],
  "경기도": ["가평군", "고양시 덕양구", "고양시 일산동구", "고양시 일산서구", "과천시", "광명시", "광주시", "구리시", "군포시", "김포시", "남양주시", "동두천시", "부천시 소사구", "부천시 오정구", "부천시 원미구", "성남시 분당구", "성남시 수정구", "성남시 중원구", "수원시 권선구", "수원시 영통구", "수원시 장안구", "수원시 팔달구", "시흥시", "안산시 단원구", "안산시 상록구", "안성시", "안양시 동안구", "안양시 만안구", "양주시", "양평군", "여주시", "연천군", "오산시", "용인시 기흥구", "용인시 수지구", "용인시 처인구", "의왕시", "의정부시", "이천시", "파주시", "평택시", "포천시", "하남시", "화성시"],
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
/* 프로필 섹션 전체 (중앙 정렬) */
.profile-section {
  display: flex;
  flex-direction: column;
  /* 수직 정렬로 변경 */
  justify-content: center;
  align-items: center;
  padding: 20px 0;
  width: 100%;
}

.profile-label {
  margin-top: 15px;
  /* 이미지와의 간격 */
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.profile-image-container {
  position: relative;
  width: 120px;
  height: 120px;
}

/* 연필 버튼 (우측 상단) */
.edit-icon-btn {
  position: absolute;
  top: 0;
  right: 0;
  width: 34px;
  height: 34px;
  background-color: #ff8a24;
  color: white;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  z-index: 2;
  border: 2px solid #fff;
}

.profile-preview {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
  background-color: #eee;
  border: 3px solid #fff;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.input-error {
  border: 1px solid #ff4d4f !important;
}

.error-msg {
  display: inline;
  color: #ff4d4f;
  font-size: 14px;
  margin-top: 4px;
  font-weight: bold;
}

.success-msg {
  display: inline;
  color: #52c41a;
  font-size: 14px;
  margin-top: 0.2rem;
  font-weight: bold;

}

.mini-btn {
  margin-right: 0.5rem;
  margin-bottom: 0.5rem;
}

.disabled-input {
  background-color: #f5f5f5;
  color: #888;
  cursor: not-allowed;
  border: 1px solid #ddd;
}

.form {
  height: 100vh;
  overflow-y: auto;
  padding-left: 2rem;
  padding-right: 0.5rem;
  padding-bottom: 200px;
}

.form::-webkit-scrollbar {
  width: 1.5rem;
}

/* ★ 패널 스크롤바 핸들 (평소 색상) ★ */
.form::-webkit-scrollbar-thumb {
  background: #ff8a24;
  /* 주황색 적용 */
  border-radius: 1rem;
  border: 0.6rem solid #f5f5f5;
  /* 주황색이 너무 답답해 보이지 않게 살짝 여백 */
  z-index: 1;
}

/* 호버 시 조금 더 진한 주황색 */
.form::-webkit-scrollbar-thumb:hover {
  background: #e66e00;
}

.duple-input {
  margin-bottom: 0.25rem;
}
</style>