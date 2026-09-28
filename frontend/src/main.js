import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router/index.js'
import axios from 'axios'
import { useAuthStore } from './stores/authStore.js'

const app = createApp(App)
const pinia = createPinia()
app.use(pinia)
axios.interceptors.request.use(config => { const token = useAuthStore(pinia).token; if (token) config.headers.Authorization = `Bearer ${token}`; return config })
app.use(router)
app.mount('#app')
