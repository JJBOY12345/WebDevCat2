import { createRouter, createWebHashHistory } from 'vue-router'
import Products from '../views/Products.vue'
import Cart from '../views/Cart.vue'
import Login from '../views/Login.vue'
import Checkout from '../views/Checkout.vue'
import Orders from '../views/Orders.vue'
import AdminOrders from '../views/AdminOrders.vue'
import AdminDashboard from '../views/AdminDashboard.vue'

const routes = [
  { path: '/', redirect: '/products' },
  { path: '/login', component: Login },
  { path: '/products', component: Products },
  { path: '/cart', component: Cart, meta: { auth: true } },
  { path: '/checkout', component: Checkout, meta: { auth: true } },
  { path: '/orders', component: Orders, meta: { auth: true } }
  ,{ path: '/admin/orders', component: AdminOrders, meta: { auth: true, admin: true } }
  ,{ path: '/admin/dashboard', component: AdminDashboard, meta: { auth: true, admin: true } }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

router.beforeEach((to) => { if (to.meta.auth && !localStorage.getItem('shop_token')) return '/login'; if (to.meta.admin && JSON.parse(localStorage.getItem('shop_user') || '{}').role !== 'ADMIN') return '/products' })

export default router
