import { createStore } from 'vuex'
import createPersistedState from 'vuex-persistedstate'

import accountStore from '@/store/modules/accountStore.js'

export default createStore({
  mutations: {
    setLogin: function (state, payload) {
      state.accountStore.isLogin = true
      state.accountStore.account = payload
    },
    setLogout: function (state) {
      state.accountStore.isLogin = false
      state.accountStore.account = null
    }
  },
  modules: {
    accountStore: accountStore
  },
  plugins: [createPersistedState({
    paths: ['accountStore']
  })]
})
