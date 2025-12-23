<template>
  <div class="filter-row">
    <div class="filter-buttons">
      <button class="pill-btn" id="distanceBtn">
        거리
        <span class="caret">▾</span>
      </button>
      <button class="pill-btn" id="regionBtn">
        지역
        <span class="caret">▾</span>
      </button>
      <button class="pill-btn" id="levelBtn">
        난이도
        <span class="caret">▾</span>
      </button>
    </div>

    <div class="sort-wrap">
      <button class="sort-btn" id="sortBtn">
        <span id="sortLabel">최신순</span>
        <span class="caret">▾</span>
      </button>
      <div class="sort-dropdown" id="sortDropdown">
        <button data-value="최신순">최신순</button>
        <button data-value="오래된 순">오래된 순</button>
        <button data-value="조회수 많은 순">조회수많은순</button>
        <button data-value="조회수 적은 순">조회수 적은순</button>
        <button data-value="별점 높은 순">별점 높은순</button>
        <button data-value="별점 낮은 순">별점 낮은 순</button>
      </div>
    </div>
  </div>

  <!-- 거리 / 지역 / 난이도 옵션 박스들 -->
  <div class="filter-options">
    <!-- 거리 -->
    <div class="distance-control" id="distanceControl">
      <div class="distance-top">
        <span>거리 설정 (km)</span>
        <span id="distanceValue">1 km ~ 10 km</span>
      </div>
      <div class="distance-slider-row range-slider-group">
        <div class="slider-range-fill" id="sliderRangeFill"></div>

        <input v-model="searchInfo.minRange" type="range" id="minDistanceRange" min="1" max="50" value="1" step="1"
          class="range-min" />
        <input v-model="searchInfo.maxRange" type="range" id="maxDistanceRange" min="1" max="50" value="50" step="1"
          class="range-max" />
      </div>
      <div class="distance-slider-buttons" style="display: flex; justify-content: flex-end; gap: 8px; margin-top: 8px;">
        <button class="distance-btn" id="resetDistanceBtn">초기화</button>
      </div>
    </div>

    <div class="region-control" id="regionControl">
      <div style="margin-bottom: 4px; color: var(--text-sub)">
        도시 / 지역구 선택
      </div>
      <div class="region-selects">
        <select ref="city" placeholder="도시"></select>
        <select v-model="searchInfo.courseRegion" ref="district" placeholder="지역구"></select>
      </div>
    </div>

    <!-- 난이도 -->
    <div class="difficulty-control" id="levelControl">
      <div style="margin-bottom: 4px; color: var(--text-sub)">
        난이도 선택
      </div>
      <div class="difficulty-chips">
        <button class="difficulty-chip" data-level="런린이">런린이</button>
        <button class="difficulty-chip" data-level="러너">러너</button>
        <button class="difficulty-chip" data-level="런고수">런고수</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useCourseStore } from '@/stores/course'
import { useMapStore } from '@/stores/map';
import { storeToRefs } from 'pinia'
import { ref, onMounted, watch } from 'vue'

const emit = defineEmits(['move-map']);
//코스
const store = useCourseStore()
const mapStore = useMapStore()
const { searchInfo } = storeToRefs(store);

const searchCourseList = () => {
  store.searchCourseList()
}

watch(
  searchInfo,
  () => {
    searchCourseList()
  },
  { deep: true }
)

/////////////////////////////////////////////////////////////////////////

const regionDB = {
  "도시": [],
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


/**
 * 전국 시/도 및 시/군/구별 중심 좌표 데이터 (줌 레벨 최적화 버전)
 * - 광역 자치단체(시/도): zoom 10 ~ 11
 * - 기초 자치단체(시/군/구): zoom 13 (면적이 넓은 군 지역은 11~12)
 */
const regionCoordinates = {
  // ------------------------------------
  // 1. 시/도 광역 자치단체 (Zoom 10-11 통일)
  // ------------------------------------
  "서울특별시": { lat: 37.566535, lng: 126.9779692, zoom: 11 },
  "부산광역시": { lat: 35.1797865, lng: 129.0750194, zoom: 11 },
  "대구광역시": { lat: 35.8714354, lng: 128.6017, zoom: 11 },
  "인천광역시": { lat: 37.4562557, lng: 126.7052062, zoom: 11 },
  "광주광역시": { lat: 35.160028, lng: 126.851451, zoom: 11 },
  "대전광역시": { lat: 36.3504119, lng: 127.3845475, zoom: 11 },
  "울산광역시": { lat: 35.5395632, lng: 129.3113596, zoom: 11 },
  "세종특별자치시": { lat: 36.504269, lng: 127.260846, zoom: 11 },
  "경기도": { lat: 37.2752179, lng: 127.0090141, zoom: 10 },
  "강원도": { lat: 37.8853765, lng: 127.7349767, zoom: 10 },
  "충청북도": { lat: 36.635967, lng: 127.491375, zoom: 10 },
  "충청남도": { lat: 36.6587642, lng: 126.672809, zoom: 10 },
  "전라북도": { lat: 35.8202511, lng: 127.1087222, zoom: 10 },
  "전남": { lat: 34.8166549, lng: 126.463255, zoom: 10 },
  "경북": { lat: 36.5760851, lng: 128.5050854, zoom: 10 },
  "경남": { lat: 35.2384739, lng: 128.6923483, zoom: 10 },
  "제주특별자치도": { lat: 33.4996213, lng: 126.5311884, zoom: 10 },

  // ------------------------------------
  // 2. 서울특별시 (Zoom 13 통일)
  // ------------------------------------
  "서울특별시 강남구": { lat: 37.5172282, lng: 127.0473772, zoom: 13 },
  "서울특별시 강동구": { lat: 37.5273673, lng: 127.1278144, zoom: 13 },
  "서울특별시 강북구": { lat: 37.6469954, lng: 127.0277194, zoom: 13 },
  "서울특별시 강서구": { lat: 37.5509425, lng: 126.8495066, zoom: 13 },
  "서울특별시 관악구": { lat: 37.4782806, lng: 126.9515671, zoom: 13 },
  "서울특별시 광진구": { lat: 37.5385655, lng: 127.082264, zoom: 13 },
  "서울특별시 구로구": { lat: 37.4954708, lng: 126.8581206, zoom: 13 },
  "서울특별시 금천구": { lat: 37.4568608, lng: 126.8953594, zoom: 13 },
  "서울특별시 노원구": { lat: 37.6541611, lng: 127.0560706, zoom: 13 },
  "서울특별시 도봉구": { lat: 37.6688538, lng: 127.0471018, zoom: 13 },
  "서울특별시 동대문구": { lat: 37.5743603, lng: 127.0395462, zoom: 13 },
  "서울특별시 동작구": { lat: 37.5123961, lng: 126.9398543, zoom: 13 },
  "서울특별시 마포구": { lat: 37.5661908, lng: 126.9015091, zoom: 13 },
  "서울특별시 서대문구": { lat: 37.5794269, lng: 126.9366436, zoom: 13 },
  "서울특별시 서초구": { lat: 37.4838637, lng: 127.0325608, zoom: 13 },
  "서울특별시 성동구": { lat: 37.563421, lng: 127.037, zoom: 13 },
  "서울특별시 성북구": { lat: 37.5894178, lng: 127.0167664, zoom: 13 },
  "서울특별시 송파구": { lat: 37.514488, lng: 127.1065203, zoom: 13 },
  "서울특별시 양천구": { lat: 37.5253896, lng: 126.8559091, zoom: 13 },
  "서울특별시 영등포구": { lat: 37.5263659, lng: 126.8962077, zoom: 13 },
  "서울특별시 용산구": { lat: 37.5323985, lng: 126.9902675, zoom: 13 },
  "서울특별시 은평구": { lat: 37.6019567, lng: 126.9292305, zoom: 13 },
  "서울특별시 종로구": { lat: 37.5735749, lng: 126.979061, zoom: 13 },
  "서울특별시 중구": { lat: 37.5635079, lng: 126.9922247, zoom: 13 },
  "서울특별시 중랑구": { lat: 37.6063686, lng: 127.0926591, zoom: 13 },

  // ------------------------------------
  // 3. 부산광역시 (Zoom 13 통일)
  // ------------------------------------
  "부산광역시 강서구": { lat: 35.2155694, lng: 128.9669534, zoom: 12 },
  "부산광역시 금정구": { lat: 35.244302, lng: 129.0833299, zoom: 13 },
  "부산광역시 기장군": { lat: 35.2415783, lng: 129.222387, zoom: 12 },
  "부산광역시 남구": { lat: 35.137851, lng: 129.088656, zoom: 13 },
  "부산광역시 동래구": { lat: 35.2045958, lng: 129.0817343, zoom: 13 },
  "부산광역시 부산진구": { lat: 35.1668853, lng: 129.0664989, zoom: 13 },
  "부산광역시 북구": { lat: 35.2198129, lng: 129.0118501, zoom: 13 },
  "부산광역시 사상구": { lat: 35.1633527, lng: 128.9814421, zoom: 13 },
  "부산광역시 사하구": { lat: 35.0945892, lng: 128.9839498, zoom: 13 },
  "부산광역시 서구": { lat: 35.0967262, lng: 129.0229447, zoom: 13 },
  "부산광역시 수영구": { lat: 35.1661646, lng: 129.1130638, zoom: 13 },
  "부산광역시 연제구": { lat: 35.1764663, lng: 129.0766107, zoom: 13 },
  "부산광역시 영도구": { lat: 35.0970535, lng: 129.0620888, zoom: 13 },
  "부산광역시 중구": { lat: 35.1061919, lng: 129.0345037, zoom: 13 },
  "부산광역시 해운대구": { lat: 35.1631245, lng: 129.1633519, zoom: 13 },

  // ------------------------------------
  // 4. 대구광역시 (Zoom 13 통일)
  // ------------------------------------
  "대구광역시 군위군": { lat: 36.3142718, lng: 128.5706249, zoom: 11 },
  "대구광역시 남구": { lat: 35.847146, lng: 128.5833221, zoom: 13 },
  "대구광역시 달서구": { lat: 35.8510862, lng: 128.5301389, zoom: 13 },
  "대구광역시 달성군": { lat: 35.8368814, lng: 128.4357064, zoom: 11 },
  "대구광역시 동구": { lat: 35.8824133, lng: 128.6277271, zoom: 12 },
  "대구광역시 북구": { lat: 35.8893967, lng: 128.5910166, zoom: 13 },
  "대구광역시 서구": { lat: 35.871928, lng: 128.5583737, zoom: 13 },
  "대구광역시 수성구": { lat: 35.8458739, lng: 128.619013, zoom: 13 },
  "대구광역시 중구": { lat: 35.8679075, lng: 128.5901173, zoom: 13 },

  // ------------------------------------
  // 5. 인천광역시 (Zoom 13 통일)
  // ------------------------------------
  "인천광역시 강화군": { lat: 37.7490072, lng: 126.4172572, zoom: 11 },
  "인천광역시 계양구": { lat: 37.5612142, lng: 126.7329241, zoom: 13 },
  "인천광역시 남동구": { lat: 37.447551, lng: 126.732734, zoom: 13 },
  "인천광역시 동구": { lat: 37.4727409, lng: 126.6346985, zoom: 13 },
  "인천광역시 미추홀구": { lat: 37.4678822, lng: 126.6669865, zoom: 13 },
  "인천광역시 부평구": { lat: 37.5097449, lng: 126.7226684, zoom: 13 },
  "인천광역시 서구": { lat: 37.5393086, lng: 126.6710777, zoom: 13 },
  "인천광역시 연수구": { lat: 37.4098905, lng: 126.6687313, zoom: 13 },
  "인천광역시 옹진군": { lat: 37.3820986, lng: 126.1557008, zoom: 10 },
  "인천광역시 중구": { lat: 37.4571991, lng: 126.611187, zoom: 13 },

  // ------------------------------------
  // 6. 경기도 (Zoom 13 통일)
  // ------------------------------------
  "경기도 가평군": { lat: 37.8306002, lng: 127.5101666, zoom: 11 },
  "경기도 고양시 덕양구": { lat: 37.6256242, lng: 126.8378772, zoom: 13 },
  "경기도 고양시 일산동구": { lat: 37.6657662, lng: 126.7885448, zoom: 13 },
  "경기도 고양시 일산서구": { lat: 37.697682, lng: 126.757041, zoom: 13 },
  "경기도 과천시": { lat: 37.4290747, lng: 127.000305, zoom: 13 },
  "경기도 광명시": { lat: 37.478676, lng: 126.866504, zoom: 13 },
  "경기도 광주시": { lat: 37.4087961, lng: 127.2587528, zoom: 12 },
  "경기도 구리시": { lat: 37.6046187, lng: 127.106602, zoom: 13 },
  "경기도 군포시": { lat: 37.3615437, lng: 126.935105, zoom: 13 },
  "경기도 김포시": { lat: 37.6166164, lng: 126.7148566, zoom: 12 },
  "경기도 남양주시": { lat: 37.6366055, lng: 127.2185566, zoom: 12 },
  "경기도 동두천시": { lat: 37.9048386, lng: 127.060197, zoom: 13 },
  "경기도 부천시 소사구": { lat: 37.4878272, lng: 126.7865913, zoom: 13 },
  "경기도 부천시 오정구": { lat: 37.5459392, lng: 126.788734, zoom: 13 },
  "경기도 부천시 원미구": { lat: 37.5029415, lng: 126.7645051, zoom: 13 },
  "경기도 성남시 분당구": { lat: 37.3616613, lng: 127.1118671, zoom: 13 },
  "경기도 성남시 수정구": { lat: 37.4443916, lng: 127.1382402, zoom: 13 },
  "경기도 성남시 중원구": { lat: 37.4168019, lng: 127.1783857, zoom: 13 },
  "경기도 수원시 권선구": { lat: 37.2652254, lng: 126.9723049, zoom: 13 },
  "경기도 수원시 영통구": { lat: 37.2694775, lng: 127.0494496, zoom: 13 },
  "경기도 수원시 장안구": { lat: 37.3006323, lng: 127.0094723, zoom: 13 },
  "경기도 수원시 팔달구": { lat: 37.284213, lng: 127.012586, zoom: 13 },
  "경기도 시흥시": { lat: 37.3785461, lng: 126.804153, zoom: 12 },
  "경기도 안산시 단원구": { lat: 37.3117469, lng: 126.7323871, zoom: 13 },
  "경기도 안산시 상록구": { lat: 37.3090757, lng: 126.8624479, zoom: 13 },
  "경기도 안성시": { lat: 37.0033107, lng: 127.2798679, zoom: 12 },
  "경기도 안양시 동안구": { lat: 37.382695, lng: 126.956667, zoom: 13 },
  "경기도 안양시 만안구": { lat: 37.3820257, lng: 126.9208753, zoom: 13 },
  "경기도 양주시": { lat: 37.781846, lng: 127.0427958, zoom: 12 },
  "경기도 양평군": { lat: 37.493822, lng: 127.485122, zoom: 11 },
  "경기도 여주시": { lat: 37.2985161, lng: 127.6360614, zoom: 12 },
  "경기도 연천군": { lat: 38.0934079, lng: 127.0858992, zoom: 11 },
  "경기도 오산시": { lat: 37.147715, lng: 127.077271, zoom: 13 },
  "경기도 용인시 기흥구": { lat: 37.2917719, lng: 127.108257, zoom: 13 },
  "경기도 용인시 수지구": { lat: 37.3228919, lng: 127.0945532, zoom: 13 },
  "경기도 용인시 처인구": { lat: 37.2341655, lng: 127.1868352, zoom: 12 },
  "경기도 의왕시": { lat: 37.3458316, lng: 126.9660341, zoom: 13 },
  "경기도 의정부시": { lat: 37.7388703, lng: 127.0378036, zoom: 13 },
  "경기도 이천시": { lat: 37.2872323, lng: 127.4414609, zoom: 12 },
  "경기도 파주시": { lat: 37.7594974, lng: 126.7770851, zoom: 12 },
  "경기도 평택시": { lat: 36.990666, lng: 127.110298, zoom: 12 },
  "경기도 포천시": { lat: 37.8927495, lng: 127.2001557, zoom: 11 },
  "경기도 하남시": { lat: 37.5398246, lng: 127.2131972, zoom: 13 },
  "경기도 화성시": { lat: 37.1983056, lng: 126.831518, zoom: 11 },

  // ------------------------------------
  // 7. 강원도 (Zoom 11~13)
  // ------------------------------------
  "강원도 강릉시": { lat: 37.7518536, lng: 128.8770281, zoom: 13 },
  "강원도 고성군": { lat: 38.3773173, lng: 128.468782, zoom: 11 },
  "강원도 동해시": { lat: 37.5255018, lng: 129.114777, zoom: 13 },
  "강원도 삼척시": { lat: 37.447738, lng: 129.167882, zoom: 12 },
  "강원도 속초시": { lat: 38.2033013, lng: 128.5916428, zoom: 13 },
  "강원도 양구군": { lat: 38.106517, lng: 127.994646, zoom: 11 },
  "강원도 양양군": { lat: 38.077271, lng: 128.621458, zoom: 11 },
  "강원도 영월군": { lat: 37.184319, lng: 128.468307, zoom: 11 },
  "강원도 원주시": { lat: 37.342371, lng: 127.937767, zoom: 13 },
  "강원도 인제군": { lat: 38.077651, lng: 128.170668, zoom: 11 },
  "강원도 정선군": { lat: 37.382229, lng: 128.66539, zoom: 11 },
  "강원도 철원군": { lat: 38.146522, lng: 127.319717, zoom: 11 },
  "강원도 춘천시": { lat: 37.8853765, lng: 127.7349767, zoom: 13 },
  "강원도 태백시": { lat: 37.166661, lng: 128.98595, zoom: 13 },
  "강원도 평창군": { lat: 37.382898, lng: 128.396561, zoom: 11 },
  "강원도 홍천군": { lat: 37.885145, lng: 127.882192, zoom: 11 },
  "강원도 화천군": { lat: 38.105193, lng: 127.701193, zoom: 11 },

  // ------------------------------------
  // 8. 충청북도
  // ------------------------------------
  "충청북도 괴산군": { lat: 36.806297, lng: 127.794689, zoom: 11 },
  "충청북도 단양군": { lat: 36.983756, lng: 128.373273, zoom: 11 },
  "충청북도 보은군": { lat: 36.486255, lng: 127.728873, zoom: 11 },
  "충청북도 영동군": { lat: 36.17387, lng: 127.785239, zoom: 11 },
  "충청북도 옥천군": { lat: 36.307527, lng: 127.575232, zoom: 11 },
  "충청북도 음성군": { lat: 36.936006, lng: 127.674919, zoom: 12 },
  "충청북도 제천시": { lat: 37.135248, lng: 128.204595, zoom: 13 },
  "충청북도 증평군": { lat: 36.78657, lng: 127.58021, zoom: 13 },
  "충청북도 진천군": { lat: 36.858712, lng: 127.452695, zoom: 12 },
  "충청북도 청주시 상당구": { lat: 36.635876, lng: 127.491375, zoom: 13 },
  "충청북도 청주시 서원구": { lat: 36.626787, lng: 127.460773, zoom: 13 },
  "충청북도 청주시 청원구": { lat: 36.666986, lng: 127.485122, zoom: 13 },
  "충청북도 청주시 흥덕구": { lat: 36.64366, lng: 127.41165, zoom: 13 },
  "충청북도 충주시": { lat: 36.993074, lng: 127.92349, zoom: 13 },

  // ------------------------------------
  // 9. 충청남도
  // ------------------------------------
  "충청남도 계룡시": { lat: 36.236967, lng: 127.241512, zoom: 13 },
  "충청남도 공주시": { lat: 36.452183, lng: 127.11895, zoom: 12 },
  "충청남도 금산군": { lat: 36.103975, lng: 127.502931, zoom: 11 },
  "충청남도 논산시": { lat: 36.195305, lng: 127.098863, zoom: 13 },
  "충청남도 당진시": { lat: 36.883711, lng: 126.631835, zoom: 12 },
  "충청남도 보령시": { lat: 36.38602, lng: 126.615822, zoom: 12 },
  "충청남도 부여군": { lat: 36.280173, lng: 126.918991, zoom: 11 },
  "충청남도 서산시": { lat: 36.777977, lng: 126.45187, zoom: 13 },
  "충청남도 아산시": { lat: 36.788478, lng: 127.002047, zoom: 13 },
  "충청남도 예산군": { lat: 36.673898, lng: 126.833501, zoom: 11 },
  "충청남도 천안시 동남구": { lat: 36.8058226, lng: 127.1691236, zoom: 13 },
  "충청남도 천안시 서북구": { lat: 36.814032, lng: 127.10664, zoom: 13 },
  "충청남도 청양군": { lat: 36.440263, lng: 126.809325, zoom: 11 },
  "충청남도 태안군": { lat: 36.730248, lng: 126.305672, zoom: 11 },
  "충청남도 홍성군": { lat: 36.60253, lng: 126.66692, zoom: 12 },

  // ------------------------------------
  // 10. 전라북도 (전북특별자치도)
  // ------------------------------------
  "전라북도 고창군": { lat: 35.438595, lng: 126.697071, zoom: 11 },
  "전라북도 군산시": { lat: 35.979066, lng: 126.702005, zoom: 13 },
  "전라북도 김제시": { lat: 35.795856, lng: 126.892015, zoom: 12 },
  "전라북도 남원시": { lat: 35.405788, lng: 127.382898, zoom: 13 },
  "전라북도 무주군": { lat: 36.035313, lng: 127.818821, zoom: 11 },
  "전라북도 부안군": { lat: 35.727546, lng: 126.732952, zoom: 11 },
  "전라북도 순창군": { lat: 35.378941, lng: 127.138402, zoom: 11 },
  "전라북도 완주군": { lat: 35.864789, lng: 127.185672, zoom: 11 },
  "전라북도 익산시": { lat: 35.946342, lng: 126.947219, zoom: 13 },
  "전라북도 임실군": { lat: 35.617158, lng: 127.279646, zoom: 11 },
  "전라북도 장수군": { lat: 35.60309, lng: 127.533261, zoom: 11 },
  "전라북도 전주시 덕진구": { lat: 35.8450148, lng: 127.1278151, zoom: 13 },
  "전라북도 전주시 완산구": { lat: 35.8157795, lng: 127.11467, zoom: 13 },
  "전라북도 정읍시": { lat: 35.570535, lng: 126.855325, zoom: 13 },
  "전라북도 진안군": { lat: 35.846506, lng: 127.428751, zoom: 11 },

  // ------------------------------------
  // 11. 전남 (전라남도)
  // ------------------------------------
  "전남 강진군": { lat: 34.542283, lng: 126.7645051, zoom: 11 },
  "전남 고흥군": { lat: 34.604719, lng: 127.2798679, zoom: 10 },
  "전남 곡성군": { lat: 35.302302, lng: 127.300557, zoom: 11 },
  "전남 광양시": { lat: 34.94522, lng: 127.69766, zoom: 13 },
  "전남 구례군": { lat: 35.20164, lng: 127.472145, zoom: 11 },
  "전남 나주시": { lat: 35.029671, lng: 126.719601, zoom: 13 },
  "전남 담양군": { lat: 35.399589, lng: 126.974954, zoom: 11 },
  "전남 목포시": { lat: 34.808035, lng: 126.392476, zoom: 13 },
  "전남 무안군": { lat: 34.992644, lng: 126.471696, zoom: 12 },
  "전남 보성군": { lat: 34.767352, lng: 127.1087222, zoom: 11 },
  "전남 순천시": { lat: 34.95761, lng: 127.502931, zoom: 13 },
  "전남 신안군": { lat: 34.78913, lng: 126.1130638, zoom: 9 },
  "전남 여수시": { lat: 34.760775, lng: 127.662243, zoom: 12 },
  "전남 영광군": { lat: 35.27515, lng: 126.5050854, zoom: 11 },
  "전남 영암군": { lat: 34.836171, lng: 126.685375, zoom: 11 },
  "전남 완도군": { lat: 34.300588, lng: 126.757041, zoom: 10 },
  "전남 장성군": { lat: 35.30907, lng: 126.779782, zoom: 11 },
  "전남 장흥군": { lat: 34.685652, lng: 127.0090141, zoom: 11 },
  "전남 진도군": { lat: 34.484196, lng: 126.275306, zoom: 11 },
  "전남 함평군": { lat: 35.19799, lng: 126.518776, zoom: 11 },
  "전남 해남군": { lat: 34.568461, lng: 126.586147, zoom: 11 },
  "전남 화순군": { lat: 35.059491, lng: 127.050302, zoom: 11 },

  // ------------------------------------
  // 12. 경북 (경상북도)
  // ------------------------------------
  "경북 경산시": { lat: 35.82276, lng: 128.748366, zoom: 13 },
  "경북 경주시": { lat: 35.856277, lng: 129.222387, zoom: 11 },
  "경북 고령군": { lat: 35.84577, lng: 128.261899, zoom: 11 },
  "경북 구미시": { lat: 36.110546, lng: 128.396561, zoom: 13 },
  "경북 김천시": { lat: 36.113695, lng: 128.113824, zoom: 13 },
  "경북 문경시": { lat: 36.621419, lng: 128.109038, zoom: 13 },
  "경북 봉화군": { lat: 36.903844, lng: 128.948275, zoom: 11 },
  "경북 상주시": { lat: 36.402128, lng: 128.156543, zoom: 12 },
  "경북 성주군": { lat: 35.918995, lng: 128.283196, zoom: 11 },
  "경북 안동시": { lat: 36.568395, lng: 128.729124, zoom: 13 },
  "경북 영덕군": { lat: 36.417208, lng: 129.362226, zoom: 11 },
  "경북 영양군": { lat: 36.669865, lng: 129.117181, zoom: 11 },
  "경북 영주시": { lat: 36.806297, lng: 128.6277271, zoom: 13 },
  "경북 영천시": { lat: 35.968037, lng: 128.937222, zoom: 13 },
  "경북 예천군": { lat: 36.5760851, lng: 128.5050854, zoom: 11 },
  "경북 울릉군": { lat: 37.5255018, lng: 130.866503, zoom: 10 },
  "경북 울진군": { lat: 36.993074, lng: 129.400133, zoom: 11 },
  "경북 의성군": { lat: 36.377627, lng: 128.6923483, zoom: 11 },
  "경북 청도군": { lat: 35.6443111, lng: 128.7362, zoom: 11 },
  "경북 청송군": { lat: 36.433919, lng: 129.222387, zoom: 11 },
  "경북 칠곡군": { lat: 36.116669, lng: 128.375677, zoom: 12 },
  "경북 포항시 남구": { lat: 36.0371689, lng: 129.3512398, zoom: 13 },
  "경북 포항시 북구": { lat: 36.0689439, lng: 129.365315, zoom: 13 },

  // ------------------------------------
  // 13. 경남 (경상남도)
  // ------------------------------------
  "경남 거제시": { lat: 34.887622, lng: 128.620023, zoom: 12 },
  "경남 거창군": { lat: 35.688846, lng: 127.904259, zoom: 11 },
  "경남 고성군": { lat: 34.978253, lng: 128.318816, zoom: 11 },
  "경남 김해시": { lat: 35.22915, lng: 128.874136, zoom: 13 },
  "경남 남해군": { lat: 34.836171, lng: 127.97022, zoom: 11 },
  "경남 밀양시": { lat: 35.5097449, lng: 128.74005, zoom: 13 },
  "경남 사천시": { lat: 35.09315, lng: 128.084511, zoom: 13 },
  "경남 산청군": { lat: 35.433919, lng: 127.876722, zoom: 11 },
  "경남 양산시": { lat: 35.336441, lng: 129.043321, zoom: 13 },
  "경남 의령군": { lat: 35.459708, lng: 128.349603, zoom: 11 },
  "경남 진주시": { lat: 35.180295, lng: 128.108257, zoom: 13 },
  "경남 창녕군": { lat: 35.545801, lng: 128.506548, zoom: 11 },
  "경남 창원시 마산합포구": { lat: 35.19799, lng: 128.5833221, zoom: 13 },
  "경남 창원시 마산회원구": { lat: 35.22177, lng: 128.56708, zoom: 13 },
  "경남 창원시 성산구": { lat: 35.22678, lng: 128.66539, zoom: 13 },
  "경남 창원시 의창구": { lat: 35.25049, lng: 128.6687313, zoom: 13 },
  "경남 창원시 진해구": { lat: 35.137851, lng: 128.643265, zoom: 13 },
  "경남 통영시": { lat: 34.846506, lng: 128.420808, zoom: 13 },
  "경남 하동군": { lat: 35.039607, lng: 127.756209, zoom: 11 },
  "경남 합천군": { lat: 35.56456, lng: 128.1691236, zoom: 11 },

  // ------------------------------------
  // 14. 제주특별자치도
  // ------------------------------------
  "제주특별자치도": { lat: 33.4996213, lng: 126.5311884, zoom: 10 },
  "제주특별자치도 제주시": { lat: 33.4996213, lng: 126.5311884, zoom: 13 },
  "제주특별자치도 서귀포시": { lat: 33.2509176, lng: 126.5658092, zoom: 13 },

  // ------------------------------------
  // 15. 광역시별 구 (기본 zoom 13)
  // ------------------------------------
  "광주광역시 광산구": { lat: 35.166661, lng: 126.851451, zoom: 13 },
  "광주광역시 남구": { lat: 35.140889, lng: 126.90565, zoom: 13 },
  "광주광역시 동구": { lat: 35.14815, lng: 126.921319, zoom: 13 },
  "광주광역시 북구": { lat: 35.1764663, lng: 126.918991, zoom: 13 },
  "광주광역시 서구": { lat: 35.159671, lng: 126.8530325, zoom: 13 },
  "대전광역시 대덕구": { lat: 36.39828, lng: 127.42095, zoom: 13 },
  "대전광역시 동구": { lat: 36.326299, lng: 127.433557, zoom: 13 },
  "대전광역시 서구": { lat: 36.355153, lng: 127.385317, zoom: 13 },
  "대전광역시 유성구": { lat: 36.375294, lng: 127.371261, zoom: 13 },
  "대전광역시 중구": { lat: 36.32759, lng: 127.41165, zoom: 13 },
  "울산광역시 남구": { lat: 35.5395632, lng: 129.3113596, zoom: 13 },
  "울산광역시 동구": { lat: 35.510862, lng: 129.41406, zoom: 13 },
  "울산광역시 북구": { lat: 35.589895, lng: 129.375677, zoom: 13 },
  "울산광역시 울주군": { lat: 35.5395632, lng: 129.3113596, zoom: 11 },
  "울산광역시 중구": { lat: 35.550186, lng: 129.33618, zoom: 13 },
};



const city = ref(null)
const district = ref(null)


onMounted(() => {
  // =========================
  // 1) 지역 셀렉트 박스 세팅
  // =========================
  Object.keys(regionDB).forEach(c => {
    city.value.innerHTML += `<option>${c}</option>`
  })

  const loadDistricts = () => {
    district.value.innerHTML = ""
    district.value.innerHTML += `<option value="">지역구 전체</option>`

    const selectedCity = city.value.value;
    if (regionDB[selectedCity]) {
      regionDB[selectedCity].forEach(gu => {
        district.value.innerHTML += `<option>${gu}</option>`
      })
    }

    // 도시 변경 시 해당 도시 중심으로 이동
    const coords = regionCoordinates[selectedCity];
    if (coords) {
      mapStore.setMapCenter({ lat: coords.lat, lng: coords.lng, zoom: coords.zoom });
    }

    searchInfo.value.courseCity = selectedCity === '도시' ? '' : selectedCity;
    searchInfo.value.courseDistrict = ""; // 도시 변경 시 지역구 초기화
  }

  district.value.addEventListener("change", () => {
    const selectedCity = city.value.value;
    const selectedDistrict = district.value.value;

    searchInfo.value.courseCity = selectedCity === '도시' ? '' : selectedCity;
    searchInfo.value.courseDistrict = selectedDistrict;

    // ⭐ 유니크 키 생성: "서울특별시 중구" 형태
    const uniqueRegionKey = `${selectedCity} ${selectedDistrict}`.trim();

    // 1. 구체적인 지역구 좌표가 있는지 먼저 확인
    let coords = regionCoordinates[uniqueRegionKey];

    // 2. 만약 지역구 전체를 선택했다면 도시 좌표로 이동
    if (!selectedDistrict || !coords) {
      coords = regionCoordinates[selectedCity];
    }

    if (coords) {
      mapStore.setMapCenter({ lat: coords.lat, lng: coords.lng, zoom: coords.zoom });
    }
  });

  city.value.addEventListener("change", loadDistricts)
  loadDistricts()

  // =========================
  // 2) 정렬 드롭다운
  // =========================
  const sortBtn = document.getElementById("sortBtn")
  const sortDropdown = document.getElementById("sortDropdown")
  const sortLabel = document.getElementById("sortLabel")

  if (sortBtn && sortDropdown && sortLabel) {
    sortBtn.addEventListener("click", (e) => {
      e.stopPropagation()
      sortDropdown.classList.toggle("open")
    })

    sortDropdown.querySelectorAll("button").forEach((btn) => {
      btn.addEventListener("click", () => {
        sortLabel.textContent = btn.dataset.value
        sortDropdown.classList.remove("open")

        //searchInfo.orderBy/Dir 값 업데이트
        const value = btn.dataset.value;
        switch (value) {
          case '최신순':
            searchInfo.value.orderBy = 'createdAt';
            searchInfo.value.orderByDir = 'desc';
            break;
          case '오래된 순':
            searchInfo.value.orderBy = 'createdAt';
            searchInfo.value.orderByDir = 'asc';
            break;
          case '조회수 많은 순':
            searchInfo.value.orderBy = 'viewCnt';
            searchInfo.value.orderByDir = 'desc';
            break;
          case '조회수 적은 순':
            searchInfo.value.orderBy = 'viewCnt';
            searchInfo.value.orderByDir = 'asc';
            break;
          case '별점 높은 순':
            searchInfo.value.orderBy = 'avgRating';
            searchInfo.value.orderByDir = 'desc';
            break;
          case '별점 낮은 순':
            searchInfo.value.orderBy = 'avgRating';
            searchInfo.value.orderByDir = 'asc';
            break;
        }


      })
    })

    document.addEventListener("click", () => {
      sortDropdown.classList.remove("open")
    })
  }

  // =========================
  // 3) 필터 버튼 토글
  // =========================
  const distanceBtn = document.getElementById("distanceBtn")
  const regionBtn = document.getElementById("regionBtn")
  const levelBtn = document.getElementById("levelBtn")

  const distanceControl = document.getElementById("distanceControl")
  const regionControl = document.getElementById("regionControl")
  const levelControl = document.getElementById("levelControl")

  if (distanceBtn && regionBtn && levelBtn &&
    distanceControl && regionControl && levelControl) {

    const resetOthers = (except) => {
      if (except !== "distance") {
        distanceBtn.classList.remove("active")
        distanceControl.style.display = "none"
      }
      if (except !== "region") {
        regionBtn.classList.remove("active")
        regionControl.style.display = "none"
      }
      if (except !== "level") {
        levelBtn.classList.remove("active")
        levelControl.style.display = "none"
      }
    }

    distanceBtn.addEventListener("click", () => {
      const willActive = !distanceBtn.classList.contains("active")
      resetOthers("distance")
      distanceBtn.classList.toggle("active", willActive)
      distanceControl.style.display = willActive ? "block" : "none"

      // ⭐ 거리 필터가 닫힐 때 (willActive가 false일 때) 거리 조건을 초기화
      if (!willActive) {
        // minRange/maxRange가 null이면 백엔드에서 무시되도록 처리하는 것이 이상적
        // 여기서는 초기값(1, 50)으로 되돌리되, 백엔드에서 이 범위는 전체 검색으로 간주해야 합니다.
        searchInfo.value.minRange = 1;
        searchInfo.value.maxRange = 50;

        // 또는 만약 백엔드가 0, 0을 무시한다면 0으로 설정
        // searchInfo.value.minRange = 0;
        // searchInfo.value.maxRange = 0; 

        // UI 초기화 로직도 실행
        if (minRange && maxRange && resetBtn) {
          minRange.value = minRange.min;
          maxRange.value = maxRange.max;
          updateValue(); // UI 및 Store 초기값 업데이트
        }
      }
    });

    regionBtn.addEventListener("click", () => {
      const willActive = !regionBtn.classList.contains("active")
      resetOthers("region")
      regionBtn.classList.toggle("active", willActive)
      regionControl.style.display = willActive ? "block" : "none"

      // 지역 필터가 닫힐 때 (willActive가 false일 때) 값을 초기화
      if (!willActive) {
        // 지역 필터를 취소할 경우, Store의 지역 값을 비움
        searchInfo.value.courseRegion = '';
      } else {
        if (district.value) {
          searchInfo.value.courseRegion = district.value.value;
        }
      }
    })

    levelBtn.addEventListener("click", () => {
      const willActive = !levelBtn.classList.contains("active")
      resetOthers("level")
      levelBtn.classList.toggle("active", willActive)
      levelControl.style.display = willActive ? "block" : "none"
    })
  }

  // =========================
  // 4) 거리 슬라이더
  // =========================
  const minRange = document.getElementById("minDistanceRange")
  const maxRange = document.getElementById("maxDistanceRange")
  const sliderRangeFill = document.getElementById("sliderRangeFill")
  const valueLabel = document.getElementById("distanceValue")
  const resetBtn = document.getElementById("resetDistanceBtn")

  if (minRange && maxRange && sliderRangeFill && valueLabel && resetBtn) {
    const min = parseFloat(minRange.min)
    const max = parseFloat(minRange.max)
    const range = max - min

    const updateValue = () => {
      let minVal = parseFloat(minRange.value)
      let maxVal = parseFloat(maxRange.value)

      if (minVal > maxVal) {
        minVal = maxVal
        minRange.value = minVal
      }
      if (maxVal < minVal) {
        maxVal = minVal
        maxRange.value = maxVal
      }

      //store 값 변경
      searchInfo.value.minRange = minVal;
      searchInfo.value.maxRange = maxVal;

      valueLabel.textContent = `${minVal} km ~ ${maxVal} km`

      const minPercent = ((minVal - min) / range) * 100
      const maxPercent = ((maxVal - min) / range) * 100

      sliderRangeFill.style.left = `${minPercent}%`
      sliderRangeFill.style.width = `${maxPercent - minPercent}%`
    }

    minRange.addEventListener("input", updateValue)
    maxRange.addEventListener("input", updateValue)
    resetBtn.addEventListener("click", () => {
      minRange.value = min
      maxRange.value = max
      updateValue()
    })

    updateValue()
  }

  // =========================
  // 5) 난이도 칩
  // =========================
  const chips = document.querySelectorAll(".difficulty-chip")
  chips.forEach((chip) => {
    chip.addEventListener("click", () => {
      const currentLevel = chip.getAttribute('data-level');

      if (chip.classList.contains("active")) {
        // ⭐ 1. 이미 활성화된 칩을 다시 누르면 비활성화 (취소)
        chip.classList.remove("active");
        searchInfo.value.difficulty = ''; // ⭐ Store 값 비우기
      } else {
        // 2. 새로운 칩 활성화
        chips.forEach((c) => c.classList.remove("active"))
        chip.classList.add("active")

        searchInfo.value.difficulty = currentLevel; // ⭐ Store 값 설정
      }
    })
  })
})
</script>

<style scoped></style>