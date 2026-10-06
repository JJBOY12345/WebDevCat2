import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    allowedHosts: ['lacie-unnosed-uncontumaciously.ngrok-free.dev'],
    proxy: {
      '/api/auth': 'http://localhost:8080',
      '/api/users': 'http://localhost:8080',
      '/api/products': 'http://localhost:8081',
      '/api/carts': 'http://localhost:8082',
      '/api/orders': 'http://localhost:8083'
    }
  }
})
