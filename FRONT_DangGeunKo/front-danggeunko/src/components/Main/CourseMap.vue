<template>
    <div class="map-wrapper">
        <CourseSearchBar />
        <div id="map" style="width:100%;height:100vh;">
            <button @click="moveToCurrentLocation" class="curr-loc-btn">
                내 위치로 이동
            </button>
        </div>
    </div>
</template>
<script setup>
import { onMounted, ref, watch } from 'vue'; // onMounted 훅을 import
import CourseSearchBar from './CourseSearchBar.vue';
import dgkMarkerImage from '@/assets/img/dgk_marker.png'
import { useRouter } from 'vue-router';
import { useMapStore } from '@/stores/map';
import { storeToRefs } from 'pinia';
const router = useRouter()
const mapStore = useMapStore()
// const searchAddress = ref('정자동 178-1'); // 초기 검색 주소 설정

// Naver Map 객체를 저장할 반응형 변수 (지도 인스턴스에 접근하기 위해)
const mapInstance = ref(null);
const infoWindowInstance = ref(null);
const markerList = ref([]);


// =========================================================
// ⭐ 마커 생성 및 관리 함수
// =========================================================
const updateMarkers = (newMarkerData) => {
    // 1. 데이터 유효성 검사 (undefined 방지)
    const safeData = Array.isArray(newMarkerData) ? newMarkerData : [];

    if (!mapInstance.value) {
        console.warn("⚠️ 아직 지도 객체가 없습니다. 생성을 기다립니다.");
        return;
    }

    // 기존 마커 제거
    markerList.value.forEach(m => m.setMap(null));
    markerList.value = [];

    // 마커 생성 로직
    const markers = safeData.map((data, index) => {
        if (!data.lat || !data.lng) return null;

        const marker = new naver.maps.Marker({
            position: new naver.maps.LatLng(data.lat, data.lng),
            map: mapInstance.value,
            title: data.name,
            icon: {
                url: dgkMarkerImage,
                size: new naver.maps.Size(40, 40),
                scaledSize: new naver.maps.Size(40, 40),
                anchor: new naver.maps.Point(20, 40)
            }
        });

        naver.maps.Event.addListener(marker, 'click', () => {
            router.push({ name: 'courseDetail', params: { id: data.id } });
        });
        return marker;
    }).filter(m => m !== null);

    markerList.value = markers;
};

// =========================================================
// ⭐ 1. 지도/주소 검색 관련 함수 정의 (initMap 밖으로 이동)
// =========================================================

// 1-1. 좌표로 주소 검색 (Reverse Geocoding)
const searchCoordinateToAddress = (latlng) => {
    // mapInstance.value가 아직 정의되지 않았을 수도 있으므로 체크
    if (!mapInstance.value || !infoWindowInstance.value) return;

    infoWindowInstance.value.close();

    naver.maps.Service.reverseGeocode({
        coords: latlng,
        orders: [
            naver.maps.Service.OrderType.ADDR,
            naver.maps.Service.OrderType.ROAD_ADDR
        ].join(',')
    }, function (status, response) {
        if (status === naver.maps.Service.Status.ERROR) {
            return alert('Something Wrong!');
        }

        var items = response.v2.results,
            address = '',
            htmlAddresses = [];

        for (var i = 0, ii = items.length, item, addrType; i < ii; i++) {
            item = items[i];
            address = makeAddress(item) || '';
            addrType = item.name === 'roadaddr' ? '[도로명 주소]' : '[지번 주소]';

            htmlAddresses.push((i + 1) + '. ' + addrType + ' ' + address);
        }

        infoWindowInstance.value.setContent([
            '<div style="padding:10px;min-width:200px;line-height:150%;">',
            '<h4 style="margin-top:5px;">검색 좌표</h4><br />',
            htmlAddresses.join('<br />'),
            '</div>'
        ].join('\n'));

        infoWindowInstance.value.open(mapInstance.value, latlng);
    });
}

// 1-2. 주소로 좌표 검색 (Geocoding)
const searchAddressToCoordinate = (address) => {
    if (!mapInstance.value) return;

    naver.maps.Service.geocode({
        query: address
    }, function (status, response) {
        if (status === naver.maps.Service.Status.ERROR) {
            return alert('Something Wrong!');
        }

        if (response.v2.meta.totalCount === 0) {
            return alert('totalCount' + response.v2.meta.totalCount);
        }

        var htmlAddresses = [];
        const item = response.v2.addresses[0];
        const point = new naver.maps.Point(item.x, item.y);

        if (item.roadAddress) {
            htmlAddresses.push('[도로명 주소] ' + item.roadAddress);
        }

        if (item.jibunAddress) {
            htmlAddresses.push('[지번 주소] ' + item.jibunAddress);
        }

        if (item.englishAddress) {
            htmlAddresses.push('[영문명 주소] ' + item.englishAddress);
        }

        infoWindowInstance.value.setContent([
            '<div style="padding:10px;min-width:200px;line-height:150%;">',
            '<h4 style="margin-top:5px;">검색 주소 : ' + address + '</h4><br />',
            htmlAddresses.join('<br />'),
            '</div>'
        ].join('\n'));

        mapInstance.value.setCenter(point);
        infoWindowInstance.value.open(mapInstance.value, point);
    });
}

// =========================================================
// ⭐ 2. 헬퍼 함수 정의 (const 화살표 함수로 통일)
// =========================================================

const makeAddress = (item) => {
    if (!item) return '';

    const { name, region, land } = item;
    const isRoadAddress = name === 'roadaddr';

    let sido = '', sigugun = '', dongmyun = '', ri = '', rest = '';

    if (hasArea(region.area1)) sido = region.area1.name;
    if (hasArea(region.area2)) sigugun = region.area2.name;
    if (hasArea(region.area3)) dongmyun = region.area3.name;
    if (hasArea(region.area4)) ri = region.area4.name;

    if (land) {
        if (hasData(land.number1)) {
            if (hasData(land.type) && land.type === '2') rest += '산';
            rest += land.number1;
            if (hasData(land.number2)) rest += ('-' + land.number2);
        }

        if (isRoadAddress) {
            if (checkLastString(dongmyun, '면')) {
                ri = land.name;
            } else {
                dongmyun = land.name;
                ri = '';
            }
            if (hasAddition(land.addition0)) rest += ' ' + land.addition0.value;
        }
    }

    return [sido, sigugun, dongmyun, ri, rest].filter(Boolean).join(' ');
};

const hasArea = (area) => !!(area && area.name && area.name !== '');

const hasData = (data) => !!(data && data !== '');

const checkLastString = (word, lastString) => new RegExp(lastString + '$').test(word);

const hasAddition = (addition) => !!(addition && addition.value);


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

// =========================================================
// ⭐ 3. 지도 초기화 함수 정의 (initMap)
// =========================================================

const initMap = () => {
    // Naver Map API가 로드된 후 (window.naver 객체가 존재할 때) 실행
    if (window.naver) {
        var position = new naver.maps.LatLng(37.3595704, 127.105399);
        var mapOptions = {
            center: position,
            zoom: 13,
        };

        // 'map' ID를 가진 DOM 요소에 지도를 생성
        var map = new naver.maps.Map('map', mapOptions);
        mapInstance.value = map

        infoWindowInstance.value = new naver.maps.InfoWindow({
            anchorSkew: true
        });
        map.setCursor('pointer');

        //  지도 클릭 리스너 연결 
        // map.addListener('click', (e) => {
        //     searchCoordinateToAddress(e.coord);
        // });

        // 초기 주소 검색 실행 
        // searchAddressToCoordinate(searchAddress.value);


        // 지도 이동(idle) 시 화면 밖에 있는 마커 최적화 (예제 로직 적용)
        naver.maps.Event.addListener(mapInstance.value, 'idle', () => {
            const mapBounds = mapInstance.value.getBounds();
            markerList.value.forEach(marker => {
                if (mapBounds.hasLatLng(marker.getPosition())) {
                    if (!marker.getMap()) marker.setMap(mapInstance.value);
                } else {
                    if (marker.getMap()) marker.setMap(null);
                }
            });
        });

        // ⭐ 중요: 초기 데이터가 이미 스토어에 있을 경우 즉시 실행
        if (mapStore.markers && mapStore.markers.length > 0) {
            updateMarkers(mapStore.markers);
        }

        // ⭐ 감시 2:지도 이동 명령을 감시하는 watch
        watch(() => mapStore.mapMoveCommand, async (command) => {
            if (command && mapInstance.value) {
                const { lat, lng, zoom } = command;
                const newCenter = new naver.maps.LatLng(lat, lng);


                mapInstance.value.setCenter(newCenter);

                // 3. 명령 실행 후 초기화 (다음 번 같은 지역 선택 시 반응을 위함)
                mapStore.clearMapMoveCommand();
            }
        }, { deep: true });
    } else {
        console.error('Naver 지도 API가 로드되지 않았습니다.');
    }
}

// 1. 데이터가 바뀔 때 마커 업데이트
watch(() => mapStore.markers, (newVal) => {
    updateMarkers(newVal);
}, { deep: true });

// 2. 지도가 바뀔 때 (null -> 객체 생성) 마커 업데이트
watch(mapInstance, (newMap) => {
    if (newMap && mapStore.markers > 0) {
        updateMarkers(mapStore.markers);
    }
});

// onMounted는 기존 코드 유지
onMounted(async () => {
    initMap();
    moveToCurrentLocation()
});
</script>

<style scoped>
.curr-loc-btn {
    position: absolute;
    z-index: 100;
    top: 100px;
    /* 검색바 아래로 */
    right: 16px;
    padding: 8px 12px;
    background: white;
    border: none;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
    cursor: pointer;
    border-radius: 8px;
    font-size: 12px;
    font-weight: bold;
}
</style>