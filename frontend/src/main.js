import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router/index.js'
import axios from 'axios'
import { useAuthStore } from './stores/authStore.js'

const app = createApp(App)
const pinia = createPinia()
app.use(pinia)
axios.interceptors.request.use(config => { const auth = useAuthStore(pinia); if (auth.token) { config.headers.Authorization = `Bearer ${auth.token}`; config.headers['X-Role'] = auth.user?.role || ''; config.headers['X-User-Id'] = auth.user?.id || '' } return config })
app.use(router)
app.mount('#app')
