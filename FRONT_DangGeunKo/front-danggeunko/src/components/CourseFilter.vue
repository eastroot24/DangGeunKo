<template>
    <div class="filter-row">
          <div class="filter-buttons">
            <button class="pill-btn active" id="distanceBtn">
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
    
    <input
        type="range"
        id="minDistanceRange"
        min="1"
        max="10"
        value="1" 
        step="1"
        class="range-min"
    />
    <input
        type="range"
        id="maxDistanceRange"
        min="1"
        max="10"
        value="10"
        step="1"
        class="range-max"
    />
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
        <select ref="city"></select>
        <select ref="district"></select>
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
import {ref, onMounted } from 'vue'
const regionDB = {
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
    regionDB[city.value.value].forEach(gu => {
      district.value.innerHTML += `<option>${gu}</option>`
    })
  }

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
    })

    regionBtn.addEventListener("click", () => {
      const willActive = !regionBtn.classList.contains("active")
      resetOthers("region")
      regionBtn.classList.toggle("active", willActive)
      regionControl.style.display = willActive ? "block" : "none"
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
      chips.forEach((c) => c.classList.remove("active"))
      chip.classList.add("active")
    })
  })
})
</script>

<style scoped>

</style>