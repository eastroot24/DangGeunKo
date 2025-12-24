<template>
  <div id="staticmap-wrapper"><div id="staticMap"></div></div>
  
</template>

<script setup>
import { onMounted, watch } from 'vue'
import dgkMarkerImage from '@/assets/img/dgk_marker.png'

const props = defineProps({
  points: {
    type: Array,
    default: () => []   // 🔥 null 방지
  }
})

let map = null
let polyline = null
let markers = []

// 시작 마커
const startIcon = {
  url: dgkMarkerImage,
  size: new naver.maps.Size(40, 40),
  scaledSize: new naver.maps.Size(40, 40),
  anchor: new naver.maps.Point(20, 40)
}

// 일반 마커
const normalIcon = {
  content: '<div style="background:#ffffff;color:white;padding:6px 6px;border-radius:50%;border:3px solid #ff8a24"></div>'
  ,
  anchor: new naver.maps.Point(5, 5)
}

function clearMap() {
  markers.forEach(m => m.setMap(null))
  markers = []
  if (polyline) polyline.setMap(null)
}

function drawMap() {
  if (!props.points.length) return

  clearMap()

  // sequence 기준 정렬
  const sorted = [...props.points].sort((a, b) => a.sequence - b.sequence)

  const latLngs = sorted.map(
    p => new naver.maps.LatLng(p.latitude, p.longitude)
  )

  // 지도 생성 (인터랙션 허용)
  map = new naver.maps.Map('staticMap', {
    center: latLngs[0],
    zoom: 14,
    draggable: true,
    scrollWheel: true,
    pinchZoom: true,
  })

  // 폴리라인
  polyline = new naver.maps.Polyline({
    map,
    path: latLngs,
    strokeColor: '#FF7A00',
    strokeWeight: 4
  })

  // 마커
  latLngs.forEach((pos, idx) => {
    const marker = new naver.maps.Marker({
      position: pos,
      map,
      icon: idx === 0 ? startIcon : normalIcon
    })
    markers.push(marker)
  })

  // bounds 자동 맞춤
  const bounds = latLngs.reduce(
    (b, coord) => b.extend(coord),
    new naver.maps.LatLngBounds(latLngs[0], latLngs[0])
  )
  map.fitBounds(bounds);

  
}

onMounted(drawMap)

// 🔥 비동기 데이터 대응
watch(
  () => props.points,
  (v) => {
    if (v && v.length) drawMap()
  },
  { deep: true }
)
</script>

<style>
  #staticMap{
     width:100%; 
     aspect-ratio: 5 / 4;
     border-radius: 1rem;
  }
</style>
