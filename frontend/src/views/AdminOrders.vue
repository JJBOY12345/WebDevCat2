<template>
  <section class="admin">
    <h1>Order management</h1>
    <p class="muted">Review and update customer orders.</p>

    <article v-for="order in orders" :key="order.id" class="order">
      <div class="order-heading">
        <strong>#{{ order.id.slice(0, 8) }}</strong>
        <span>Rs.{{ order.total }}</span>
      </div>
      <p class="customer">
        Customer: <strong>{{ order.customer?.name || 'Unknown customer' }}</strong>
        <span v-if="order.customer?.email">({{ order.customer.email }})</span>
      </p>
      <p class="order-user-id">User ID: {{ order.userId }}</p>
      <p>{{ order.items.map(item => `${item.productName} × ${item.quantity}`).join(', ') }}</p>
      <select :value="order.status" @change="update(order, $event.target.value)">
        <option v-for="status in statuses" :key="status">{{ status }}</option>
      </select>
    </article>

    <p v-if="!orders.length">No orders found.</p>
  </section>
</template>

<script>
import axios from 'axios'

export default {
  data: () => ({
    orders: [],
    statuses: ['PLACED', 'PACKED', 'SHIPPED', 'DELIVERED', 'CANCELLED']
  }),

  async created() {
    const orders = (await axios.get('http://localhost:8083/api/orders')).data

    this.orders = await Promise.all(orders.map(async order => {
      try {
        const response = await axios.get(`http://localhost:8080/api/users/${order.userId}`)
        return { ...order, customer: response.data }
      } catch {
        return { ...order, customer: null }
      }
    }))
  },

  methods: {
    async update(order, status) {
      const { data } = await axios.patch(
        `http://localhost:8083/api/orders/${order.id}/status`,
        { status }
      )
      Object.assign(order, data)
    }
  }
}
</script>

<style scoped>
.admin { max-width: 900px; margin: auto; }
.muted { color: #666; margin: 8px 0 25px; }
.order { background: #fff; padding: 18px; margin: 12px 0; border-radius: 10px; }
.order-heading { display: flex; justify-content: space-between; gap: 15px; }
.customer { margin: 12px 0 4px; }
.customer span, .order-user-id { color: #666; }
.order-user-id { font-size: .8rem; margin-bottom: 10px; }
.order p { color: #666; margin: 12px 0; }
.order select { padding: 8px; border: 1px solid #d5dbea; border-radius: 6px; }
</style>
