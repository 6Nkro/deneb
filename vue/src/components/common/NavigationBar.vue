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
          @click="this.$emit('setLoginModal', true); showSignUp=false;">
          LOGIN
        </a>

        <a
          v-if="!isLogin"
          href="javascript:void(0)"
          @click="this.$emit('setLoginModal', false); showSignUp=true;">
          SIGNUP
        </a>

        <v-menu
          location="bottom"
          v-if="isLogin">
          <template v-slot:activator="{ props }">
            <a
              v-bind="props"
              href="javascript:void(0)">
              ACCOUNT
            </a>
          </template>

          <v-list>
            <v-list-item
              v-for="item in accountMenu"
              :key="item.link">
              <router-link
                class="text-center"
                role="menu"
                :to="item.link">
                <v-icon
                  class="mx-1"
                  icon="mdi-package-variant"
                />
                {{ item.title }}
              </router-link>
            </v-list-item>
          </v-list>
        </v-menu>

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
    @close="this.$emit('setLoginModal', false)"
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
  props: {
    showLogin: Boolean
  },
  components: { AccountLogin, AccountSignUp },
  data () {
    return {
      showSignUp: false,
      isLogin: this.$store.state.accountStore.isLogin,
      active: 'Home',
      accountMenu: [
        { title: '공유함', link: '/account/myshare' }
      ]
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
      this.$emit('setLoginModal', false)
      this.showSignUp = false
    },
    switch () {
      this.$emit('setLoginModal', !this.showLogin)
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

.router-link-active[role="menu"] {
  color: #404040;
}

.router-link-active[role="menu"]:hover {
  color: #808080;
}

a > i {
  font-size: large;
}

</style>
