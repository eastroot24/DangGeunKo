<template>
    <div class="profile-card">
        <img src="https://i.imgur.com/7b3cP6e.png" class="profile-img">
        <div class="profile-info">
            <b>{{ store.user.nickname }}</b>
            <p>{{ store.user.userEmail }}</p>
            <span @click="goFollow('following')">{{ store.followingList.length }} 팔로잉</span> · 
            <span @click="goFollow('follower')">{{ store.followerList.length }} 팔로워</span>
            <button class="profile-btn" @click="isOpen=true">프로필 수정</button>
        </div>

        <div class="popup-bg" v-if="isOpen">
            <div class="popup">
                <div class="popup-title">비밀번호 확인</div>

                <input 
                    type="password" 
                    v-model="pwInput"
                    placeholder="비밀번호를 입력하세요"
                    @keyup.enter="checkPassword"
                >

                <div class="error" v-if="pwError">
                비밀번호가 일치하지 않습니다.
                </div>

                <button class="confirm-btn" @click="checkPassword">확인</button>
                <button class="close-btn" @click="closePopup">취소</button>
            </div>
        </div>

    </div>
</template>

<script setup>
import { ref, onMounted } from "vue"
import { useRouter } from "vue-router"
import { useUserStore } from "@/stores/user"

const isOpen = ref(false)
const router = useRouter()
const store = useUserStore()
const pwInput = ref("")
const pwError = ref(false)

const closePopup = () => {
  isOpen.value = false
  pwInput.value = ""
  pwError.value = false
}

const checkPassword = () => {
  if (pwInput.value === store.user.userPassword) {
    sessionStorage.setItem('pwVerified', 'true') // 🔑 핵심
    closePopup()
    router.push({ name: 'editProfile' })
  } else {
    pwError.value = true
  }
}

const goFollow = (type) => {
    router.push({ 
        name: "follow", 
        query: { type }
    })
}

onMounted(async () => {
    const myId = store.loginUserId

    await store.getUserById(myId)
    await store.getFollowing(myId)
    await store.getFollower(myId)
})

</script>

<style scoped>
.popup-bg{
    position:fixed; top:0; left:0;
    width:100%; height:100%;
    background:rgba(0,0,0,.35);
    display:flex;
    justify-content:center;
    align-items:center;
    z-index:20;
}
.popup{
    width:280px;background:white;border-radius:14px;padding:20px;text-align:center;
    box-shadow:0 6px 20px rgba(0,0,0,.2);
}
.popup-title{font-weight:700;margin-bottom:10px;}
.popup input{
    width:100%;border:1px solid #ddd;border-radius:10px;
    padding:10px;font-size:12px;margin-bottom:12px;
}
.popup button{
    width:100%;height:36px;border-radius:10px;border:none;font-size:13px;
    font-weight:600;cursor:pointer;
}
.confirm-btn{background:#ff7a00;color:white;}
.close-btn{background:#ddd;margin-top:6px;}

.error {
  color: red;
  font-size: 12px;
  margin-bottom: 10px;
}


</style>