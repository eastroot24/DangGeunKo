<template>
    <div>
         <div class="back" @click="retry">← 코스 다시 그리기</div>
         <!-- 입력 폼 -->
        <div class="form-wrap">
        <label style="font-weight: bold;">코스 이름</label>
        <input type="text" placeholder="코스 이름">

        <label style="font-weight: bold;">지역구</label>
        <div class="row-3">
            <select id="city"></select>
            <select id="district"></select>
        </div>
        <label style="font-weight: bold;">출발지 주소</label>
        <input type="text" placeholder="출발지 주소">
        <label style="font-weight: bold;">도착지 주소</label>
        <input type="text" placeholder="도착지 주소">

        <div class="row-3">
            <label style="font-weight: bold;">전체거리</label>
            <input type="number" placeholder="전체 거리 (km)">
            <label style="font-weight: bold;">소요 시간</label>
            <input type="number" placeholder="소요 시간 (분)">
            <label style="font-weight: bold;">평균 페이스</label>
            <input type="number" placeholder="평균 페이스 (/km)">
        </div>

        <div class="row-3">
            <label style="font-weight: bold;">러닝 유형</label>
            <select><option>런닝 유형</option></select>
            <label style="font-weight: bold;">코스 난이도</label>
            <select><option>코스 난이도</option></select>
        </div>
        <label style="font-weight: bold;">코스 설명 및 편의 시설</label>
        <textarea placeholder="코스의 특징, 주의 사항 등을 적어주세요."></textarea>

        <div class="row-3" style="margin-top:10px;">
            <label style="font-weight: bold;">횡단보도 유무</label>
            <select>
                <option>있음</option>
                <option>없음</option>
            </select>
             <label style="font-weight: bold;">공중화장실 유무</label>
            <select>
                <option>있음</option>
                <option>없음</option>
            </select>
        </div>
    </div>
   <button @click="addCourse" class="submit">등록하기</button>
    </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter()
const emit = defineEmits(["draw"])
const retry = () =>{
    emit("isDone", false)
}
const addCourse = () =>{
    alert('코스가 등록되었습니다.')
    // 본인이 올린 곳으로 가게 할 예정
    router.replace({path: "/"})

}

</script>

<style scoped>
 .back { font-size:10px;cursor:pointer;color: #ff7a00;
    }
 .form-wrap{padding:18px;}
    label{font-size:12px;color:#444;margin-bottom:4px;display:block;}
    input,select,textarea{
        width:100%;padding:10px;border-radius:10px;border:1px solid #ddd;font-size:12px;
        margin-bottom:14px;
    }
    textarea{height:100px;resize:none;}

    .row-3{display:flex;gap:6px;margin-bottom:12px;}
    .row-3 input,.row-3 select{flex:1;}
 .submit{
        width:85%;height:48px;border-radius:25px;background:#ff7a00;color:#fff;
        position:absolute;left:50%;transform:translateX(-50%);bottom:78px;
        border:none;font-size:15px;font-weight:600;cursor:pointer;
    }
</style>