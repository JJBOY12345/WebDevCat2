<template>
  <div class="products-page">
    <div class="hero">
      <h1>Welcome to Shopwebsite</h1>
      <p>Browse our available products</p>
    </div>

    <section v-if="isAdmin" class="admin-panel">
      <div class="admin-panel-heading">
        <div>
          <h2>Admin product management</h2>
          <p>Add a product directly to the catalog.</p>
        </div>
        <button class="toggle-form" type="button" @click="showAddForm = !showAddForm">
          {{ showAddForm ? 'Close form' : 'Add product' }}
        </button>
      </div>

      <form v-if="showAddForm" class="product-form" @submit.prevent="createProduct">
        <label>Product name<input v-model.trim="newProduct.name" required /></label>
        <label>Price<input v-model.number="newProduct.price" type="number" min="0.01" step="0.01" required /></label>
        <label>Stock<input v-model.number="newProduct.stock" type="number" min="0" required /></label>
        <label>Emoji / logo<input v-model.trim="newProduct.emoji" maxlength="8" placeholder="🛍️" /></label>
        <label class="description-field">Description<textarea v-model.trim="newProduct.description" required rows="2"></textarea></label>
        <p v-if="formError" class="form-error">{{ formError }}</p>
        <button class="btn-submit" type="submit" :disabled="saving">{{ saving ? 'Saving...' : 'Save product' }}</button>
      </form>
    </section>

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
      successMsg: null,
      showAddForm: false,
      saving: false,
      formError: null,
      newProduct: { name: '', price: null, stock: 0, description: '', emoji: '🛍️' }
    }
  },
  computed: {
    isAdmin() { return useAuthStore().isAdmin }
  },
  async created() {
    this.loading = true
    try {
      const res = await axios.get('http://localhost:8081/api/products')
      this.products = res.data
    } catch (e) {
      this.error = 'Failed to load products. Make sure backend services are running.'
    } finally {
      this.loading = false
    }
  },
  methods: {
    async createProduct() {
      this.saving = true
      this.formError = null
      try {
        const response = await axios.post('http://localhost:8081/api/products', {
          name: this.newProduct.name,
          price: this.newProduct.price,
          stock: this.newProduct.stock,
          description: this.newProduct.description,
          emoji: this.newProduct.emoji || '🛍️'
        })
        this.products.unshift(response.data)
        this.newProduct = { name: '', price: null, stock: 0, description: '', emoji: '🛍️' }
        this.showAddForm = false
        this.successMsg = `${response.data.name} added to the catalog.`
        setTimeout(() => { this.successMsg = null }, 2500)
      } catch (e) {
        this.formError = e.response?.data?.message || 'Failed to add product.'
      } finally {
        this.saving = false
      }
    },
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
        await axios.delete(`http://localhost:8081/api/products/${product.id}`)
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

.admin-panel {
  background: white;
  border: 2px solid #4a00e0;
  border-radius: 6px;
  padding: 18px;
  margin-bottom: 28px;
}

.admin-panel-heading {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
}

.admin-panel-heading h2 { margin-bottom: 4px; }
.admin-panel-heading p { color: #666; font-size: .9rem; }

.toggle-form, .btn-submit {
  background: #4a00e0;
  color: white;
  border: none;
  padding: 10px 16px;
  border-radius: 4px;
}

.product-form {
  margin-top: 18px;
  padding-top: 18px;
  border-top: 1px solid #eee;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px 16px;
}

.product-form label { display: grid; gap: 6px; font-size: .9rem; font-weight: bold; }
.product-form input, .product-form textarea { width: 100%; padding: 9px; border: 1px solid #ccc; border-radius: 4px; font: inherit; font-weight: normal; }
.description-field { grid-column: 1 / -1; }
.btn-submit { justify-self: start; cursor: pointer; }
.btn-submit:disabled { opacity: .65; cursor: wait; }
.form-error { grid-column: 1 / -1; color: #c0392b; font-size: .9rem; }

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
