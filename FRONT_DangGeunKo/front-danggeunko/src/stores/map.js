import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useMapStore = defineStore('map', () => {
  const mapMoveCommand = ref(null) // { lat: number, lng: number, zoom: number }
  const markers = ref([])
  const setMapCenter = (command) => {
    mapMoveCommand.value = command
  }
// 마커 데이터를 업데이트하는 함수
  const setMarkers = (newMarkers) => {
    markers.value = newMarkers
  }
  // 명령을 실행한 후에는 초기화하여 무한 루프를 방지합니다.
  const clearMapMoveCommand = () => {
    mapMoveCommand.value = null
  }

  return { markers, mapMoveCommand, setMapCenter, clearMapMoveCommand, setMarkers,  }
})