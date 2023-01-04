import { createStore } from 'vuex'

export default createStore({
  state: {
    isLogin: false,
    account: {
      account_seq: 0,
      user_email: 'guest',
      user_pw: 'guest',
      user_name: 'guest',
      bookcase_order: []
    }
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
  }
})
