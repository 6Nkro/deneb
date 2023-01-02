<template>

  <v-layout>

    <v-app-bar
      class="nav-bar"
      color="#121212">
      <img src="@/assets/images/title.png"
           alt="deneb"
           @click="closeAll; this.$router.push('/');">

      <router-link
        to="/"
        @click="closeAll">
        <v-icon>mdi-home</v-icon>
        HOME
      </router-link>

      <router-link
        to="/library"
        @click="closeAll">
        <v-icon>mdi-bookmark-box-multiple</v-icon>
        LIBRARY
      </router-link>

      <router-link
        to="/share"
        @click="closeAll">
        <v-icon>mdi-comment-bookmark</v-icon>
        SHARE
      </router-link>

      <template v-slot:append>
        <a
          v-if="!isLogin"
          href="javascript:void(0)"
          @click="showLogin = true; showSignUp=false;">
          LOGIN
        </a>

        <a
          v-if="!isLogin"
          href="javascript:void(0)"
          @click="showLogin = false; showSignUp=true;">
          SIGNUP
        </a>

        <a
          v-if="isLogin"
          href="javascript:void(0)">
          ACCOUNT
        </a>

        <a
          v-if="isLogin"
          href="javascript:void(0)"
          @click="logout">
          LOGOUT
        </a>
      </template>
    </v-app-bar>

  </v-layout>

  <account-login
    v-if="showLogin"
    @close="showLogin = false"
    @switch="this.switch"/>

  <account-sign-up
    v-if="showSignUp"
    @close="showSignUp = false"
    @switch="this.switch"/>

</template>

<script>
import AccountLogin from '@/components/account/AccountLogin'
import AccountSignUp from '@/components/account/AccountSignUp'

export default {
  components: { AccountLogin, AccountSignUp },
  data () {
    return {
      showLogin: false,
      showSignUp: false,
      isLogin: this.$store.state.accountStore.isLogin,
      active: 'Home'
    }
  },
  setup () {
  },
  created () {
  },
  mounted () {
  },
  unmounted () {
  },
  methods: {
    closeAll () {
      this.showLogin = false
      this.showSignUp = false
    },
    switch () {
      this.showLogin = !this.showLogin
      this.showSignUp = !this.showSignUp
    },
    logout () {
      this.$router.push('/')
      this.$store.commit('setLogout')
      this.$router.go()
    }
  }
}
</script>

<style scoped>

@media (min-width: 768px) {
}

.nav-bar {
  min-width: 720px;
}

img {
  width: 8rem;
  margin-left: 2rem;
  margin-right: 2rem;
  cursor: pointer;
}

a {
  white-space: nowrap;
  overflow: hidden;
  text-decoration: none;
  color: #808080;
  font-family: Ubuntu, sans-serif;
  font-size: large;
  font-weight: bold;
  transition: 0.3s;
  padding: 0 1.5rem 0 0;
}

a:hover {
  color: #C8C8C8;
}

.router-link-active {
  color: white;
}

.router-link-active:hover {
  color: white;
}

a > i {
  font-size: large;
}

</style>
