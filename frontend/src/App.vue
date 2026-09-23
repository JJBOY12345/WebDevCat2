<template>
  <div id="app">
    <header class="navbar">
      <span class="brand">ShopWebsite</span>
      <nav>
        <router-link to="/products">Product</router-link>
        <router-link to="/cart">Cart</router-link>
        <button class="admin-link" @click="openAdminDialog">
          {{ isAdmin ? 'Admin ✓' : 'Admin' }}
        </button>
      </nav>
    </header>

    <main>
      <router-view v-slot="{ Component }">
        <component :is="Component" :is-admin="isAdmin" />
      </router-view>
    </main>

    <div v-if="showAdminDialog" class="modal-backdrop" @click.self="closeAdminDialog">
      <form class="admin-modal" @submit.prevent="verifyAdmin">
        <h2>Admin access</h2>
        <p>Enter the admin password to manage products.</p>
        <input v-model="password" type="password" placeholder="Password" autocomplete="off" autofocus />
        <p v-if="loginError" class="login-error">{{ loginError }}</p>
        <div class="modal-actions">
          <button type="button" class="btn-cancel" @click="closeAdminDialog">Cancel</button>
          <button type="submit" class="btn-confirm">Continue</button>
        </div>
      </form>
    </div>

    <footer class="footer">
      <p>ShopWebsite &copy; by ssn</p>
    </footer>
  </div>
</template>

<script>
const ADMIN_PASSWORD = 'admin123'

export default {
  name: 'App',
  data() {
    return { isAdmin: false, showAdminDialog: false, password: '', loginError: null }
  },
  methods: {
    openAdminDialog() {
      if (this.isAdmin) {
        this.isAdmin = false
        return
      }
      this.password = ''
      this.loginError = null
      this.showAdminDialog = true
    },
    closeAdminDialog() {
      this.showAdminDialog = false
      this.password = ''
      this.loginError = null
    },
    verifyAdmin() {
      if (this.password === ADMIN_PASSWORD) {
        this.isAdmin = true
        this.closeAdminDialog()
      } else {
        this.loginError = 'Incorrect password.'
      }
    }
  }
}
</script>

<style>
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

body {
  font-family: Arial, sans-serif;
  background: #f0f0f0;
}

#app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.navbar {
  background: #4a00e0;
  color: white;
  padding: 16px 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.navbar .brand {
  font-size: 1.3rem;
  font-weight: bold;
}

.navbar nav a {
  color: white;
  text-decoration: none;
  margin-left: 20px;
  font-size: 0.95rem;
}

.navbar nav a:hover,
.navbar nav a.router-link-active {
  text-decoration: underline;
}

.admin-link {
  margin-left: 20px;
  background: transparent;
  border: 1px solid rgba(255, 255, 255, 0.7);
  color: white;
  padding: 6px 10px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
}

.admin-link:hover { background: rgba(255, 255, 255, 0.15); }

main {
  flex: 1;
  padding: 32px;
}

.footer {
  background: #222;
  color: #ccc;
  text-align: center;
  padding: 16px;
  font-size: 0.85rem;
}

.modal-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10;
}

.admin-modal {
  width: min(90%, 360px);
  background: white;
  border-radius: 6px;
  padding: 24px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.25);
}

.admin-modal h2 { color: #4a00e0; margin-bottom: 8px; }
.admin-modal p { color: #666; font-size: 0.9rem; margin-bottom: 16px; }
.admin-modal input { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; font-size: 0.95rem; }
.login-error { color: #c0392b !important; margin: 8px 0 0 !important; }
.modal-actions { display: flex; justify-content: flex-end; gap: 10px; margin-top: 20px; }
.btn-cancel, .btn-confirm { border: none; padding: 9px 14px; border-radius: 4px; cursor: pointer; }
.btn-cancel { background: #eee; color: #333; }
.btn-confirm { background: #4a00e0; color: white; }
</style>
