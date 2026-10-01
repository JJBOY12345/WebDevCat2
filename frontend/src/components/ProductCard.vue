<template>
  <div class="product-card">
    <img v-if="product.imageUrl" class="product-image" :src="product.imageUrl" :alt="product.name" />
    <div v-else class="product-emoji">{{ product.emoji || '🛍️' }}</div>
    <h3>{{ product.name }}</h3>
    <p class="description">{{ product.description }}</p>
    <p class="price">Rs.{{ product.price }}</p>
    <p class="stock" :class="{ 'low-stock': product.stock > 0 && product.stock <= 5, 'out-stock': product.stock === 0 }">
      {{ product.stock > 0 ? `${product.stock} left in stock` : 'Out of stock' }}
    </p>
    <button class="btn-add" :disabled="product.stock === 0" @click="$emit('add-to-cart', product)">
      {{ product.stock === 0 ? 'Out of Stock' : 'Add to Cart' }}
    </button>
    <form v-if="isAdmin" class="stock-form" @submit.prevent="$emit('update-stock', product, stockValue)">
      <label>Stock <input v-model.number="stockValue" type="number" min="0" required /></label>
      <button class="btn-stock" type="submit">Update stock</button>
    </form>
    <button v-if="isAdmin" class="btn-edit" type="button" @click="editing = !editing">{{ editing ? 'Close editor' : 'Edit product' }}</button>
    <form v-if="isAdmin && editing" class="edit-form" @submit.prevent="$emit('edit-product', product, editProduct)">
      <label>Name <input v-model.trim="editProduct.name" required /></label>
      <label>Price <input v-model.number="editProduct.price" type="number" min="0.01" step="0.01" required /></label>
      <label>Emoji / logo <input v-model.trim="editProduct.emoji" maxlength="8" /></label>
      <label>Image URL <input v-model.trim="editProduct.imageUrl" type="url" /></label>
      <label>Description <textarea v-model.trim="editProduct.description" required rows="2"></textarea></label>
      <button class="btn-stock" type="submit">Save product</button>
    </form>
    <button v-if="isAdmin" class="btn-delete" @click="$emit('delete-product', product)">
      Delete Product
    </button>
  </div>
</template>

<script>
export default {
  name: 'ProductCard',
  props: {
    product: {
      type: Object,
      required: true
    },
    isAdmin: {
      type: Boolean,
      default: false
    }
  },
  emits: ['add-to-cart', 'delete-product', 'update-stock', 'edit-product'],
  data() {
    return { stockValue: this.product.stock, editing: false, editProduct: { ...this.product } }
  }
}
</script>

<style scoped>
.product-card {
  background: white;
  border: 2px solid #4a00e0;
  border-radius: 6px;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.product-emoji {
  font-size: 2rem;
}

.product-image {
  width: 100%;
  height: 120px;
  object-fit: contain;
  border-radius: 4px;
  background: #f5f5f5;
}

h3 {
  font-size: 1rem;
  font-weight: bold;
}

.description {
  font-size: 0.85rem;
  color: #555;
  flex: 1;
}

.price {
  font-weight: bold;
  font-size: 0.95rem;
}

.stock { color: #27ae60; font-size: .85rem; font-weight: bold; }
.low-stock { color: #d68910; }
.out-stock { color: #c0392b; }

.btn-add {
  background: #c0392b;
  color: white;
  border: none;
  padding: 10px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
  width: 100%;
}

.btn-add:hover {
  background: #a93226;
}

.btn-add:disabled { background: #999; cursor: not-allowed; }

.stock-form { display: grid; gap: 6px; margin-top: 4px; }
.stock-form label { font-size: .82rem; font-weight: bold; }
.stock-form input { width: 100%; box-sizing: border-box; padding: 7px; border: 1px solid #ccc; border-radius: 4px; margin-top: 4px; }
.btn-stock { background: #4a00e0; color: white; border: none; padding: 8px; border-radius: 4px; cursor: pointer; }
.btn-edit { background: white; color: #4a00e0; border: 1px solid #4a00e0; padding: 8px; border-radius: 4px; cursor: pointer; }
.edit-form { display: grid; gap: 7px; border-top: 1px solid #eee; padding-top: 10px; }
.edit-form label { font-size: .82rem; font-weight: bold; }
.edit-form input, .edit-form textarea { width: 100%; box-sizing: border-box; padding: 7px; border: 1px solid #ccc; border-radius: 4px; margin-top: 4px; font: inherit; }

.btn-delete {
  background: #666;
  color: white;
  border: none;
  padding: 8px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.85rem;
  width: 100%;
}

.btn-delete:hover {
  background: #444;
}
</style>
