import { defineStore } from 'pinia'
import axios from 'axios'
const API = 'http://localhost:8080/cart/1'
export const useCartStore = defineStore('cart', { state: () => ({ items: [], loading: false }), getters: { total: s => s.items.reduce((sum, i) => sum + Number(i.price) * i.quantity, 0), itemCount: s => s.items.reduce((sum, i) => sum + i.quantity, 0) }, actions: {
  async fetchCart() { this.loading = true; try { this.items = (await axios.get(API)).data.items || [] } finally { this.loading = false } },
  async addToCart(p) { await axios.post(`${API}/items`, { productId: String(p.id), productName: p.name, price: p.price, quantity: 1, emoji: p.emoji }); await this.fetchCart() },
  async updateQuantity(id, quantity) { await axios.put(`${API}/items/${id}`, { quantity }); await this.fetchCart() },
  async removeItem(id) { await axios.delete(`${API}/items/${id}`); await this.fetchCart() }
} })
