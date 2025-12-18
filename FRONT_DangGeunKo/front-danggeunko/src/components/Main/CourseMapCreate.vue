<template>
  <div class="map-wrapper">
    <div id="createmap" style="width:100%;height:400px;">
      <div class="map-btn-group">
        <button @click="moveToCurrentLocation" class="map-btn">
          현위치
        </button>
        <button @click="undoLast">되돌리기</button>
        <button @click="resetAll">초기화</button>
      </div>

    </div>

    <div style="margin-top:10px;">
      <div style="margin-top:6px;font-size:12px;">
        현재 거리: {{ distanceKm }} km
      </div>

    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import dgkMarkerImage from '@/assets/img/dgk_marker.png'

const emit = defineEmits([
  'update:points',
  'update:distance',
  'update:startAddress',
  'update:endAddress'
])


let map
let path = []
let markers = []
let polyline = null

const distanceKm = ref(0)
let totalDistance = 0

const startIcon = {
  url: dgkMarkerImage,
  size: new naver.maps.Size(40, 40),
  scaledSize: new naver.maps.Size(40, 40),
  anchor: new naver.maps.Point(20, 40)
}

const normalIcon = {
  content: '<div style="background:#3498db;color:white;padding:6px 10px;border-radius:50%;"></div>',
  anchor: new naver.maps.Point(10, 10)
}
const mapInstance = ref(null);
// =========================================================
// ⭐ 현위치 가져오기 및 지도 이동 함수
// =========================================================
const curLocationMarker = ref(null); // 현위치 마커를 저장할 변수

const moveToCurrentLocation = () => {
  if (!mapInstance.value) return;

  if (navigator.geolocation) {

    navigator.geolocation.getCurrentPosition(
      (position) => {
        const lat = position.coords.latitude;
        const lng = position.coords.longitude;
        const currentLatLng = new naver.maps.LatLng(lat, lng);

        mapInstance.value.setCenter(currentLatLng);
        mapInstance.value.setZoom(16); // 정확도를 높였으므로 줌 레벨을 더 높여도 좋습니다.

        if (curLocationMarker.value) {
          curLocationMarker.value.setPosition(currentLatLng);
          curLocationMarker.value.setMap(mapInstance.value);
        } else {
          curLocationMarker.value = new naver.maps.Marker({
            position: currentLatLng,
            map: mapInstance.value,
            icon: {
              content: '<div style="width:15px;height:15px;background:rgba(0,123,255,0.8);border:2px solid white;border-radius:50%;box-shadow:0 0 8px rgba(0,0,0,0.5);"></div>',
              anchor: new naver.maps.Point(7, 7)
            }
          });
        }
      },
      (err) => {
        console.error("Geolocation 오류:", err.message);
        // 옵션 사용 시 타임아웃 오류가 발생할 수 있으므로 사용자에게 안내
        if (err.code === 3) alert("위치 정보를 가져오는 데 시간이 초과되었습니다.");
        else alert("위치 권한을 확인해주세요.");
      },
    );
  } else {
    alert("이 브라우저에서는 위치 서비스를 지원하지 않습니다.");
  }
};

// 거리 계산
function calcDistance(p1, p2) {
  const R = 6371
  const dLat = (p2.lat() - p1.lat()) * Math.PI / 180
  const dLng = (p2.lng() - p1.lng()) * Math.PI / 180

  const a =
    Math.sin(dLat / 2) ** 2 +
    Math.cos(p1.lat() * Math.PI / 180) *
    Math.cos(p2.lat() * Math.PI / 180) *
    Math.sin(dLng / 2) ** 2

  return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
}

// 좌표 emit
function emitPoints() {
  const points = path.map((p, idx) => ({
    sequence: idx + 1,
    latitude: p.lat(),
    longitude: p.lng()
  }))
  emit('update:points', points)
}

// 지도 클릭
function addPoint(latlng) {
  if (path.length > 0) {
    totalDistance += calcDistance(path[path.length - 1], latlng)
    distanceKm.value = Number(totalDistance.toFixed(2))
    emit('update:distance', distanceKm.value)
  }

  if (path.length === 0) {
    getAddress(latlng, 'update:startAddress')
  } else {
    getAddress(latlng, 'update:endAddress')
  }

  const icon = path.length === 0 ? startIcon : normalIcon

  const marker = new naver.maps.Marker({
    position: latlng,
    map,
    icon
  })

  markers.push(marker)
  path.push(latlng)
  polyline.setPath(path)

  emitPoints()
}

// Undo
function undoLast() {
  if (path.length === 0) return

  const lastMarker = markers.pop()
  lastMarker.setMap(null)

  path.pop()
  polyline.setPath(path)

  totalDistance = 0
  for (let i = 1; i < path.length; i++) {
    totalDistance += calcDistance(path[i - 1], path[i])
  }
  distanceKm.value = Number(totalDistance.toFixed(2))
  emit('update:distance', distanceKm.value)
  emitPoints()
}

// Reset
function resetAll() {
  markers.forEach(m => m.setMap(null))
  markers = []
  path = []

  totalDistance = 0
  distanceKm.value = 0

  if (polyline) polyline.setMap(null)

  polyline = new naver.maps.Polyline({
    map,
    path,
    strokeColor: '#FF8A24',
    strokeWeight: 4
  })

  emit('update:distance', 0)
  emitPoints()
}

function getAddress(latlng, type) {
  naver.maps.Service.reverseGeocode(
    {
      coords: latlng,
      orders: naver.maps.Service.OrderType.ADDR
    },
    (status, response) => {
      if (status !== naver.maps.Service.Status.OK) return

      const address = response.v2.address.jibunAddress
      emit(type, address)
    }
  )
}


onMounted(() => {
  map = new naver.maps.Map('createmap', {
    center: new naver.maps.LatLng(37.5665, 126.9780),
    zoom: 14
  })

  mapInstance.value = map

  polyline = new naver.maps.Polyline({
    map,
    path,
    strokeColor: '#FF8A24',
    strokeWeight: 4
  })

  naver.maps.Event.addListener(map, 'click', e => {
    addPoint(e.coord)
  })
})
</script>

<style>
.map-wrapper {
  position: relative;
  width: 100%;
  height: 400px;
}

#createmap {
  width: 100%;
  height: 100%;
}

.map-btn-group {
  position: absolute;
  top: 12px;
  right: 12px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  z-index: 100;
}

.map-btn {
  padding: 8px 10px;
  font-size: 12px;
  background: white;
  border: 1px solid #ccc;
  border-radius: 6px;
  cursor: pointer;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
}

.map-btn:hover {
  background: #f5f5f5;
}
</style>