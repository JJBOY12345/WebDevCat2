<template>
  <div class="product-card">
    <img v-if="product.imageUrl" class="product-image" :src="product.imageUrl" :alt="product.name" />
    <div v-else class="product-emoji">{{ product.emoji || '🛍️' }}</div>
    <h3>{{ product.name }}</h3>
    <p class="description">{{ product.description }}</p>
    <p class="price">Rs.{{ product.price }}</p>
    <button class="btn-add" @click="$emit('add-to-cart', product)">Add to Cart</button>
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
