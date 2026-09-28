<template>
  <div class="products-page">
    <div class="hero">
      <h1>Welcome to Shopwebsite</h1>
      <p>Browse our available products</p>
    </div>

    <div v-if="loading" class="status">Loading products...</div>
    <div v-else-if="error" class="status error">{{ error }}</div>
    <div v-else>
      <h2>Available products are</h2>
      <div class="product-grid">
        <ProductCard
          v-for="product in products"
          :key="product.id"
          :product="product"
          :is-admin="isAdmin"
          @add-to-cart="handleAddToCart"
          @delete-product="deleteProduct"
        />
      </div>
    </div>

    <div v-if="successMsg" class="toast">{{ successMsg }}</div>
  </div>
</template>

<script>
import axios from 'axios'
import ProductCard from '../components/ProductCard.vue'
import { useCartStore } from '../stores/cartStore.js'
import { useAuthStore } from '../stores/authStore.js'

export default {
  name: 'Products',
  components: { ProductCard },
  data() {
    return {
      products: [],
      loading: false,
      error: null,
      successMsg: null
    }
  },
  props: {
    isAdmin: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    isAdmin() { return useAuthStore().isAdmin }
  },
  async created() {
    this.loading = true
    try {
      const res = await axios.get('http://localhost:8080/products')
      this.products = res.data
    } catch (e) {
      this.error = 'Failed to load products. Make sure backend services are running.'
    } finally {
      this.loading = false
    }
  },
  methods: {
    async handleAddToCart(product) {
      if (!useAuthStore().isAuthenticated) { this.$router.push('/login'); return }
      const cartStore = useCartStore()
      await cartStore.addToCart(product)
      this.successMsg = `${product.name} added to cart!`
      setTimeout(() => { this.successMsg = null }, 2000)
    },
    async deleteProduct(product) {
      if (!window.confirm(`Delete ${product.name}?`)) return

      try {
        await axios.delete(`http://localhost:8080/api/products/admin/${product.id}`)
        this.products = this.products.filter(item => item.id !== product.id)
        this.successMsg = `${product.name} deleted.`
        setTimeout(() => { this.successMsg = null }, 2000)
      } catch (e) {
        this.error = 'Failed to delete product.'
      }
    }
  }
}
</script>

<style scoped>
.products-page {
  max-width: 1100px;
  margin: 0 auto;
}

.hero {
  text-align: center;
  margin-bottom: 32px;
}

.hero h1 {
  font-size: 1.8rem;
  margin-bottom: 8px;
}

.hero p {
  color: #666;
}

h2 {
  font-size: 1.1rem;
  margin-bottom: 16px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.status {
  text-align: center;
  padding: 32px;
  color: #555;
}

.status.error {
  color: #c0392b;
}

.toast {
  position: fixed;
  bottom: 24px;
  right: 24px;
  background: #27ae60;
  color: white;
  padding: 12px 20px;
  border-radius: 6px;
  font-size: 0.9rem;
}
</style>
