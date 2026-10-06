import { defineStore } from 'pinia'
import axios from 'axios'
import { useAuthStore } from './authStore.js'
const API = '/api/carts'
export const useCartStore = defineStore('cart', { state: () => ({ items: [], loading: false }), getters: { total: s => s.items.reduce((sum, i) => sum + Number(i.price) * i.quantity, 0), itemCount: s => s.items.reduce((sum, i) => sum + i.quantity, 0) }, actions: {
  userPath() { return `${API}/${useAuthStore().user.id}` },
  async fetchCart() { this.loading = true; try { this.items = (await axios.get(this.userPath())).data.items || [] } finally { this.loading = false } },
  async addToCart(p) { await axios.post(`${this.userPath()}/items`, { productId: String(p.id), quantity: 1 }); await this.fetchCart() },
  async updateQuantity(id, quantity) { await axios.put(`${this.userPath()}/items/${id}`, { quantity }); await this.fetchCart() },
  async removeItem(id) { await axios.delete(`${this.userPath()}/items/${id}`); await this.fetchCart() }
} })
