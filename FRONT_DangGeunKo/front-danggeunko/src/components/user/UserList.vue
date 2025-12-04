<template>
    <div>
        <h4>유저 목록</h4>
        <hr>
        <table>
            <thead>
                <tr>
                    <th>번호</th>
                    <th>이름</th>
                    <th>닉네임</th>
                    <th>이메일</th>
                    <th>비번</th>
                    <th>성별</th>
                    <th>나이</th>
                    <th>지역</th>
                    <th>가입일시</th>
                    <th>선호거리</th>
                    <th>수준</th>
                    <th>수정일시</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="user in store.userList" :key="user.id">
                    <td>{{ user.userId }}</td>
                    <td>{{ user.userName }}</td>
                    <td>{{ user.nickname }}</td>
                    <td>{{ user.userEmail }}</td>
                    <td>{{ user.userPassword }}</td>
                    <td>{{ user.gender }}</td>
                    <td>{{ user.age }}</td>
                    <td>{{ user.region }}</td>
                    <td>{{ user.createdAt }}</td>
                    <td>{{ user.prefDistance }}</td>
                    <td>{{ user.prefDifficulty }}</td>
                    <td>{{ user.updateAt  }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script setup>
import { useUserStore } from '@/stores/user';
import { onMounted } from 'vue';

const store = useUserStore()
onMounted(() => {
    store.getUserList()
})

</script>

<style scoped>
    table{
        border: 1px, solid, black;
    }
    td, th{
        border: 1px, solid, black;
    
    }
</style>