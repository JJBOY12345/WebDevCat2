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
  emits: ['add-to-cart', 'delete-product']
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
