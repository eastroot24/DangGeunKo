import './assets/style.css'
import '@flaticon/flaticon-uicons/css/all/all.css'
// import { createApp } from 'vue'
// import { createPinia } from 'pinia'

// import App from './App.vue'
// import router from './router'

// const app = createApp(App)

// app.use(createPinia())
// app.use(router)

// app.mount('#app')

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { useUserStore } from '@/stores/user'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)

const userStore = useUserStore()
userStore.initAuth()   // ⭐⭐⭐ 이거 중요

app.use(router)
app.mount('#app')

