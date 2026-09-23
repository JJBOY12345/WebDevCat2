import { defineStore } from 'pinia'
import axios from 'axios'

const API = 'http://localhost:8080/cart'

export const useCartStore = defineStore('cart', {
  state: () => ({
    items: [],
    loading: false,
    error: null
  }),

  getters: {
    total: (state) =>
      state.items.reduce((sum, item) => sum + item.price * item.quantity, 0),
    itemCount: (state) =>
      state.items.reduce((sum, item) => sum + item.quantity, 0)
  },

  actions: {
    async fetchCart() {
      this.loading = true
      try {
        const res = await axios.get(`${API}/1`)
        this.items = res.data.items || []
      } catch (e) {
        this.error = 'Failed to load cart'
      } finally {
        this.loading = false
      }
    },

    async addToCart(product) {
      try {
        await axios.post(`${API}/1/items`, {
          productId: product.id,
          productName: product.name,
          price: product.price,
          quantity: 1,
          emoji: product.emoji
        })
        await this.fetchCart()
      } catch (e) {
        this.error = 'Failed to add item'
      }
    },

    async updateQuantity(productId, quantity) {
      try {
        await axios.put(`${API}/1/items/${productId}`, { quantity })
        await this.fetchCart()
      } catch (e) {
        this.error = 'Failed to update quantity'
      }
    },

    async removeItem(productId) {
      try {
        await axios.delete(`${API}/1/items/${productId}`)
        await this.fetchCart()
      } catch (e) {
        this.error = 'Failed to remove item'
      }
    }
  }
})
