import { defineStore } from 'pinia'
import axios from 'axios'
const API = 'http://localhost:8080/api/auth'
export const useAuthStore = defineStore('auth', {
  state: () => ({ token: localStorage.getItem('shop_token'), user: JSON.parse(localStorage.getItem('shop_user') || 'null') }),
  getters: { isAuthenticated: s => Boolean(s.token), isAdmin: s => s.user?.role === 'ADMIN' },
  actions: {
    async login(credentials) { const { data } = await axios.post(`${API}/login`, credentials); this.setSession(data) },
    async register(credentials) { const { data } = await axios.post(`${API}/register`, credentials); this.setSession(data) },
    setSession(data) { this.token = data.token; this.user = data.user; localStorage.setItem('shop_token', data.token); localStorage.setItem('shop_user', JSON.stringify(data.user)) },
    logout() { this.token = null; this.user = null; localStorage.removeItem('shop_token'); localStorage.removeItem('shop_user') }
  }
})
