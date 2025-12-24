<template>
    <div class="filter-row">
          <div class="filter-buttons">
            <button class="pill-btn" id="regionBtn">
              지역
              <span class="caret">▾</span>
            </button>
          </div>
    </div>

    <!-- 거리 / 지역 / 난이도 옵션 박스들 -->
    <div class="filter-options">
      <div class="region-control" id="regionControl">
        <div style="margin-bottom: 0.4rem; color: var(--text-sub)">
                도시 / 지역구 선택
        </div>
        <div class="region-selects">
          <select ref="city" placeholder="도시"></select>
          <select ref="district" placeholder="지역구"></select>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useCourseStore } from '@/stores/course'
import { storeToRefs } from 'pinia'
import {ref, onMounted, watch } from 'vue'

//코스
const store = useCourseStore()
const { searchInfo } = storeToRefs(store);

const getRankingList = () => {
  store.getWeeklyRanking()
}

watch(
  searchInfo,
  () => {
    getRankingList()
  },
  {deep: true}
)

/////////////////////////////////////////////////////////////////////////

const regionDB = {
    "시/도 선택" : [],
    "서울특별시": ["강남구","강동구","강북구","강서구","관악구","광진구","구로구","금천구","노원구","도봉구","동대문구","동작구",
    "마포구","서대문구","서초구","성동구","성북구","송파구","양천구","영등포구","용산구","은평구","종로구","중구","중랑구"],

    "부산광역시":[ "강서구","금정구","기장군","남구","동래구","부산진구","북구","사상구","사하구","서구","수영구","연제구","영도구","중구","해운대구"],
    "대구광역시":[ "군위군","남구","달서구","달성군","동구","북구","서구","수성구","중구"],
    "인천광역시":[ "강화군","계양구","남동구","동구","미추홀구","부평구","서구","연수구","옹진군","중구"],

    "경기도":[
        "가평군","고양시 덕양구","고양시 일산동구","고양시 일산서구",
        "과천시","광명시","광주시","구리시","군포시","김포시",
        "남양주시","동두천시","부천시 소사구","부천시 오정구","부천시 원미구",
        "성남시 분당구","성남시 수정구","성남시 중원구",
        "수원시 권선구","수원시 영통구","수원시 장안구","수원시 팔달구",
        "시흥시","안산시 단원구","안산시 상록구","안성시","안양시 동안구","안양시 만안구",
        "양주시","양평군","여주시","연천군","오산시",
        "용인시 기흥구","용인시 수지구","용인시 처인구","의왕시","의정부시",
        "이천시","파주시","평택시","포천시","하남시","화성시"
    ],

    "강원도":[ "강릉시","고성군","동해시","삼척시","속초시","양구군","양양군","영월군","원주시","인제군","정선군","철원군","춘천시","태백시","평창군","홍천군","화천군" ],
    "충청북도":[ "괴산군","단양군","보은군","영동군","옥천군","음성군","제천시","증평군","진천군","청주시 상당구","청주시 서원구","청주시 청원구","청주시 흥덕구","충주시" ],
    "충청남도":[ "계룡시","공주시","금산군","논산시","당진시","보령시","부여군","서산시","아산시","예산군","천안시 동남구","천안시 서북구","청양군","태안군","홍성군" ],
    "전라북도":[ "고창군","군산시","김제시","남원시","무주군","부안군","순창군","완주군","익산시","임실군","장수군","전주시 덕진구","전주시 완산구","정읍시","진안군" ],
    "전남":[ "강진군","고흥군","곡성군","광양시","구례군","나주시","담양군","목포시","무안군","보성군","순천시","신안군","여수시","영광군","영암군","완도군","장성군","장흥군","진도군","함평군","해남군","화순군" ],
    "경북":[ "경산시","경주시","고령군","구미시","김천시","문경시","봉화군","상주시","성주군","안동시","영덕군","영양군","영주시","영천시","예천군","울릉군","울진군","의성군","청도군","청송군","칠곡군","포항시 남구","포항시 북구" ],
    "경남":[ "거제시","거창군","고성군","김해시","남해군","밀양시","사천시","산청군","양산시","의령군","진주시","진해구","창녕군","창원시 마산합포구","창원시 마산회원구","창원시 성산구","창원시 의창구","창원시 진해구","통영시","하동군","합천군" ],
    "제주특별자치도":[ "제주시","서귀포시" ]
}

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
    district.value.innerHTML += `<option value="">시/군/구 선택</option>`
    regionDB[city.value.value].forEach(gu => {
        district.value.innerHTML += `<option>${gu}</option>`
    })
        
     
     searchInfo.value.courseCity = city.value.value === '시/도 선택'? '' : city.value.value;
     searchInfo.value.courseDistrict = district.value.value
  }

  district.value.addEventListener("change", () => {
      searchInfo.value.courseCity = city.value.value;
      searchInfo.value.courseDistrict = district.value.value;
    });

  city.value.addEventListener("change", loadDistricts)
  loadDistricts()

  
  // =========================
  // 3) 필터 버튼 토글
  // =========================
  const regionBtn = document.getElementById("regionBtn")

  const regionControl = document.getElementById("regionControl")

  if (regionBtn && regionControl) {

    const resetOthers = (except) => {

      if (except !== "region") {
        regionBtn.classList.remove("active")
        regionControl.style.display = "none"
      }

    }



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


  }

  
})
</script>

<style scoped>

</style>