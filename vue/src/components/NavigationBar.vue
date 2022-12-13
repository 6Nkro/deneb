<template>
  <b-navbar container="true" toggleable="md" dark="true" variant="dark" sticky="top">

    <img src="@/assets/images/title.png" alt="deneb" @click="closeAll(); this.$router.push('/');">

    <b-navbar-toggle target="nav-collapse">
    </b-navbar-toggle>

    <b-collapse id="nav-collapse" is-nav="true">
      <b-navbar-nav id="nav-item-start">
        <b-nav-item to="/" @click="closeAll">Home</b-nav-item>
        <b-nav-item href="javascript:void(0)" @click="closeAll">Collection</b-nav-item>
        <b-nav-item href="javascript:void(0)" @click="closeAll">Community</b-nav-item>
      </b-navbar-nav>

      <b-navbar-nav id="nav-item-end">
        <b-nav-item v-if="!isLogin" href="javascript:void(0)" @click="showSignUp=false; showLogin = true;">Login</b-nav-item>
        <b-nav-item v-if="!isLogin" href="javascript:void(0)" @click="showLogin=false; showSignUp = true;">SignUp</b-nav-item>
        <b-nav-item v-if="isLogin" href="javascript:void(0)">Account</b-nav-item>
        <b-nav-item v-if="isLogin" href="javascript:void(0)" @click="logout">Logout</b-nav-item>
      </b-navbar-nav>
    </b-collapse>
  </b-navbar>

  <account-login v-if="showLogin" @close="showLogin = false" @switch="this.switch"></account-login>
  <account-sign-up v-if="showSignUp" @close="showSignUp = false" @switch="this.switch"></account-sign-up>
</template>

<script>
import AccountLogin from '@/components/AccountLogin'
import AccountSignUp from '@/components/AccountSignUp'

export default {
  components: { AccountLogin, AccountSignUp },
  data () {
    return {
      showLogin: false,
      showSignUp: false,
      isLogin: this.$getState().isLogin
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
      this.$setLogout()
      this.$router.push('/')
      this.$router.go()
    }
  }
}
</script>

<style scoped>
nav {
  min-width: 280px;
  min-height: 66px;
}

@media (min-width: 768px) {
  #nav-item-end {
    position: absolute;
    right: 0;
    margin-right: 4rem;
  }

  ul > * {
    padding-top: 5px;
    padding-left: 10px;
  }
}

img {
  width: 8rem;
  margin-left: 2rem;
  margin-right: 2rem;
  cursor: pointer;
}

</style>
