<template>
    <div>
        <div class="back" @click="retry">← 코스 다시 그리기</div>
        <!-- 입력 폼 -->
        <div class="form-wrap">
            <label style="font-weight: bold;">코스 이름</label>
            <input v-model="courseInfo.courseName" type="text" placeholder="코스 이름">

            <label style="font-weight: bold;">지역구</label>
            <div class="row-3">
                <select ref="city"></select>
                <select ref="district"></select>
            </div>
            <label style="font-weight: bold;">출발지 주소</label>
            <input v-model="courseInfo.startAddress" type="text" placeholder="출발지 주소">
            <label style="font-weight: bold;">도착지 주소</label>
            <input v-model="courseInfo.endAddress" type="text" placeholder="도착지 주소">

            <div class="row-3">
                <label style="font-weight: bold;">전체거리</label>
                <input v-model="courseInfo.distanceKm" type="number" placeholder="전체 거리 (km)">
                <label style="font-weight: bold;">소요 시간</label>
                <input v-model="courseInfo.durationMin" type="number" placeholder="소요 시간 (분)">
                <label style="font-weight: bold;">평균 페이스</label>
                <input v-model="courseInfo.paceMin" type="number" placeholder="평균 페이스 (/km)">
            </div>

            <div class="row-3">
                <label style="font-weight: bold;">러닝 유형</label>
                <select v-model="courseInfo.courseType">
                    <option value="1">인터벌</option>
                    <option value="2">템포</option>
                    <option value="3">파틀렉</option>
                    <option value="4">장거리</option>
                </select>
                <label style="font-weight: bold;">코스 난이도</label>
                <select v-model="courseInfo.difficulty">
                    <option value="초급">런린이</option>
                    <option value="중급">러너</option>
                    <option value="고급">런고수</option>
                </select>
            </div>
            <label style="font-weight: bold;">코스 설명 및 편의 시설</label>
            <textarea v-model="courseInfo.description" placeholder="코스의 특징, 주의 사항 등을 적어주세요."></textarea>

            <div class="row-3" style="margin-top:10px;">
                <label style="font-weight: bold;">횡단보도 유무</label>
                <select v-model="courseInfo.hasCrosswalk">
                    <option value="true">있음</option>
                    <option value="false">없음</option>
                </select>
                <label style="font-weight: bold;">공중화장실 유무</label>
                <select v-model="courseInfo.hasToilet">
                    <option value="true">있음</option>
                    <option value="false">없음</option>
                </select>
            </div>
        </div>
        <button @click="addCourse" class="submit">등록하기</button>
    </div>
</template>

<script setup>
import { useCourseStore } from '@/stores/course'
import { useUserStore } from '@/stores/user'
import { storeToRefs } from 'pinia'
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const emit = defineEmits(['isDone'])

const retry = () => emit('isDone', false)

// 🔹 stores
const userStore = useUserStore()
const courseStore = useCourseStore()
const { loginUserId } = storeToRefs(userStore)

// 🔹 props
const props = defineProps({
  points: { type: Array, default: () => [] },
  distanceKm: { type: Number, default: 0 },
  startAddress: { type: String, default: '' },
  endAddress: { type: String, default: '' }
})

// 🔹 courseInfo
const courseInfo = ref({
  userId: loginUserId.value,
  courseName: '',
  courseCity: '',
  courseDistrict: '',
  startAddress: '',
  endAddress: '',
  distanceKm: 0,
  durationMin: 0,
  paceMin: 0,
  courseType: 0,
  difficulty: '',
  description: '',
  hasCrosswalk: false,
  hasToilet: false,
  coursePoints: []
})

onMounted(() => {
  courseInfo.value.startAddress = props.startAddress
  courseInfo.value.endAddress = props.endAddress
  courseInfo.value.distanceKm = props.distanceKm
  courseInfo.value.coursePoints = props.points
})

watch(() => props.points, v => {
  courseInfo.value.coursePoints = v ?? []
})

watch(() => props.distanceKm, v => {
  courseInfo.value.distanceKm = v ?? 0
})

watch(() => props.startAddress, v => {
  courseInfo.value.startAddress = v ?? ''
})

watch(() => props.endAddress, v => {
  courseInfo.value.endAddress = v ?? ''
})

/* ✅ 페이스 자동 계산 */
watch(
  () => [courseInfo.value.distanceKm, courseInfo.value.durationMin],
  ([distance, duration]) => {
    if (distance > 0 && duration > 0) {
      courseInfo.value.paceMin = Number((duration / distance).toFixed(1))
    }
  }
)

/* ✅ 로그인 ID 늦게 들어올 경우 */
watch(loginUserId, v => {
  courseInfo.value.userId = v ?? null
})

// 🔹 등록
const addCourse = async () => {
  try {
    const payload = {
      ...courseInfo.value,
      coursePoints: courseInfo.value.coursePoints.map((p, idx) => ({
        courseId: null,     
        sequence: idx + 1,          
        latitude: p.latitude,
        longitude: p.longitude
      }))
    }

    const newCourseId = await courseStore.registCourse(payload)

    if (newCourseId) {
      router.replace({ name: 'courseDetail', params: { id: newCourseId } })
    }
  } catch (e) {
    console.error('코스 등록 실패', e)
  }
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

const city = ref(null)
const district = ref(null)

onMounted(() => {
    Object.keys(regionDB).forEach(c => {
        city.value.innerHTML += `<option>${c}</option>`
    })

    const loadDistricts = () => {
        district.value.innerHTML = ""
        regionDB[city.value.value].forEach(gu => {
            district.value.innerHTML += `<option>${gu}</option>`
        })
        courseInfo.value.courseCity = city.value.value
        courseInfo.value.courseDistrict = district.value.value
    }

    city.value.addEventListener("change", loadDistricts)
    loadDistricts()
    if (district.value) {
        courseInfo.value.courseDistrict = district.value.value;
    }
})

</script>

<style scoped>
.back {
    font-size: 10px;
    cursor: pointer;
    color: #ff7a00;
}

.form-wrap {
    padding: 18px;
}

label {
    font-size: 12px;
    color: #444;
    margin-bottom: 4px;
    display: block;
}

input,
select,
textarea {
    width: 100%;
    padding: 10px;
    border-radius: 10px;
    border: 1px solid #ddd;
    font-size: 12px;
    margin-bottom: 14px;
}

textarea {
    height: 100px;
    resize: none;
}

.row-3 {
    display: flex;
    gap: 6px;
    margin-bottom: 12px;
}

.row-3 input,
.row-3 select {
    flex: 1;
}

.submit {
    width: 85%;
    height: 48px;
    border-radius: 25px;
    background: #ff7a00;
    color: #fff;
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    bottom: 78px;
    border: none;
    font-size: 15px;
    font-weight: 600;
    cursor: pointer;
}
</style>