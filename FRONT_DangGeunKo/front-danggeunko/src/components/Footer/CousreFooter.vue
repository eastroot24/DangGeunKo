<template>
    <div> 
        <div v-if="loginUserId">
            <CoachButton></CoachButton>
        </div>
        <CreateCourseBtn/>
        <NavBar/>
    </div>
</template>

<script setup>
import CreateCourseBtn from './CreateCourseBtn.vue';
import NavBar from './CourseNavBar.vue';
import CoachButton from './CoachButton.vue';
import { useUserStore } from '@/stores/user';
import { storeToRefs } from 'pinia';

const userStore = useUserStore()
const {loginUserId} = storeToRefs(userStore)

</script>

<style scoped>

</style>