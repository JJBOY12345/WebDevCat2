import { createRouter, createWebHashHistory } from 'vue-router'
import Products from '../views/Products.vue'
import Cart from '../views/Cart.vue'

const routes = [
  { path: '/', redirect: '/products' },
  { path: '/products', component: Products },
  { path: '/cart', component: Cart }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
