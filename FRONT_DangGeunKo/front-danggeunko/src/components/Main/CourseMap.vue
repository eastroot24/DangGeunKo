<template>
    <div>
        <h3>지도화면</h3>
        <div id="map" style="width:100%;height:400px;">
            
        </div>
        <CourseSearchBar/>
    </div>
</template>
<script setup >
import { onMounted } from 'vue'; // onMounted 훅을 import
import CourseSearchBar from './CourseSearchBar.vue';

// 지도를 초기화하는 함수
const initMap = () => {
    // Naver Map API가 로드된 후 (window.naver 객체가 존재할 때) 실행
    if (window.naver) {
        var mapOptions = {
            center: new naver.maps.LatLng(37.3595704, 127.105399),
            zoom: 10
        };

        // 'map' ID를 가진 DOM 요소에 지도를 생성
        var map = new naver.maps.Map('map', mapOptions);
    } else {
        console.error('Naver 지도 API가 로드되지 않았습니다.');
    }
}

// 컴포넌트가 DOM에 마운트된 후 initMap 함수를 실행
onMounted(() => {
    initMap();
})

</script>

<style scoped >

</style>