<template>
  <div class="cart-page">
    <h1>Shopping Cart</h1>

    <div v-if="cartStore.loading" class="status">Loading cart...</div>

    <div v-else-if="cartStore.items.length === 0" class="empty">
      <p>Your cart is empty.</p>
      <router-link to="/products" class="btn-shop">Browse Products</router-link>
    </div>

    <div v-else>
      <table class="cart-table">
        <thead>
          <tr>
            <th>Product</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Subtotal</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in cartStore.items" :key="item.productId">
            <td>
              <span class="item-emoji">{{ item.emoji }}</span>
              {{ item.productName }}
            </td>
            <td>Rs.{{ item.price }}</td>
            <td>
              <div class="qty-controls">
                <button @click="decrease(item)">−</button>
                <span>{{ item.quantity }}</span>
                <button @click="increase(item)">+</button>
              </div>
            </td>
            <td>Rs.{{ item.price * item.quantity }}</td>
            <td>
              <button class="btn-remove" @click="remove(item.productId)">Remove</button>
            </td>
          </tr>
        </tbody>
      </table>

      <div class="cart-summary">
        <p class="total">Total: <strong>Rs.{{ cartStore.total }}</strong></p>
        <button class="btn-order" @click="placeOrder">Place Order</button>
      </div>
    </div>

    <div v-if="orderMsg" class="toast">{{ orderMsg }}</div>
  </div>
</template>

<script>
import axios from 'axios'
import { useCartStore } from '../stores/cartStore.js'

export default {
  name: 'Cart',
  data() {
    return {
      orderMsg: null
    }
  },
  computed: {
    cartStore() {
      return useCartStore()
    }
  },
  created() {
    this.cartStore.fetchCart()
  },
  methods: {
    increase(item) {
      this.cartStore.updateQuantity(item.productId, item.quantity + 1)
    },
    decrease(item) {
      if (item.quantity > 1) {
        this.cartStore.updateQuantity(item.productId, item.quantity - 1)
      } else {
        this.remove(item.productId)
      }
    },
    remove(productId) {
      this.cartStore.removeItem(productId)
    },
    async placeOrder() {
      try {
        await axios.post('http://localhost:8080/orders', {
          cartId: '1',
          items: this.cartStore.items,
          total: this.cartStore.total
        })
        this.orderMsg = 'Order placed successfully!'
        setTimeout(async () => {
          this.orderMsg = null
          // Clear cart after order
          for (const item of [...this.cartStore.items]) {
            await this.cartStore.removeItem(item.productId)
          }
        }, 2000)
      } catch (e) {
        this.orderMsg = 'Failed to place order.'
        setTimeout(() => { this.orderMsg = null }, 2000)
      }
    }
  }
}
</script>

<style scoped>
.cart-page {
  max-width: 900px;
  margin: 0 auto;
}

h1 {
  font-size: 1.6rem;
  margin-bottom: 24px;
}

.status, .empty {
  text-align: center;
  padding: 48px;
  color: #555;
}

.btn-shop {
  display: inline-block;
  margin-top: 12px;
  background: #4a00e0;
  color: white;
  padding: 10px 20px;
  border-radius: 4px;
  text-decoration: none;
}

.cart-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
}

.cart-table th,
.cart-table td {
  padding: 12px 16px;
  border-bottom: 1px solid #eee;
  text-align: left;
}

.cart-table th {
  background: #f5f5f5;
  font-weight: bold;
}

.item-emoji {
  font-size: 1.2rem;
  margin-right: 8px;
}

.qty-controls {
  display: flex;
  align-items: center;
  gap: 10px;
}

.qty-controls button {
  background: #eee;
  border: none;
  width: 28px;
  height: 28px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
}

.qty-controls button:hover {
  background: #ddd;
}

.btn-remove {
  background: #c0392b;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.85rem;
}

.btn-remove:hover {
  background: #a93226;
}

.cart-summary {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 24px;
}

.total {
  font-size: 1.2rem;
}

.btn-order {
  background: #4a00e0;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
}

.btn-order:hover {
  background: #3a00b0;
}

.toast {
  position: fixed;
  bottom: 24px;
  right: 24px;
  background: #27ae60;
  color: white;
  padding: 12px 20px;
  border-radius: 6px;
}
</style>
