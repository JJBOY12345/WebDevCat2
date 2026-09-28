<template><div id="app"><header class="navbar"><router-link class="brand" to="/products">ShopWebsite</router-link><nav><router-link to="/products">Shop</router-link><router-link v-if="auth.isAuthenticated" to="/cart">Cart</router-link><router-link v-if="auth.isAuthenticated" to="/orders">Orders</router-link><router-link v-if="auth.isAdmin" to="/admin/orders">Manage orders</router-link><span v-if="auth.user" class="welcome">Hi, {{ auth.user.name }}</span><router-link v-if="!auth.isAuthenticated" to="/login">Login</router-link><button v-else @click="logout">Logout</button></nav></header><main><router-view /></main><footer class="footer">ShopWebsite · a modular shopping platform</footer></div></template>
<script>
import { useAuthStore } from './stores/authStore.js'
export default { computed: { auth() { return useAuthStore() } }, methods: { logout() { this.auth.logout(); this.$router.push('/login') } } }
</script>
<style>
* { box-sizing: border-box; margin: 0; padding: 0; }
body { font-family: Arial, sans-serif; background: #f0f0f0; }
#app { display: flex; flex-direction: column; min-height: 100vh; }
.navbar { background: #4a00e0; color: white; padding: 16px 32px; display: flex; justify-content: space-between; align-items: center; }
.navbar .brand { font-size: 1.3rem; font-weight: bold; color: white; text-decoration: none; }
.navbar nav { display: flex; align-items: center; gap: 0; }
.navbar nav a { color: white; text-decoration: none; margin-left: 20px; font-size: .95rem; }
.navbar nav a:hover, .navbar nav a.router-link-active { text-decoration: underline; }
.navbar nav button { margin-left: 20px; background: transparent; border: 1px solid rgba(255,255,255,.7); color: white; padding: 6px 10px; border-radius: 4px; cursor: pointer; font-size: .9rem; }
.navbar nav button:hover { background: rgba(255,255,255,.15); }
.welcome { color: white; margin-left: 20px; font-size: .9rem; }
main { flex: 1; padding: 32px; }
.footer { background: #222; color: #ccc; text-align: center; padding: 16px; font-size: .85rem; }
button, .btn { cursor: pointer; }
</style>
