const methods = {
  wobble (element) {
    const order = [0, -8, 8, -4, 4, -2, 2, -1, 1, 0]
    const seq = []
    order.forEach(y => {
      seq.push({ transform: 'translate(0,' + y + 'px)' })
    })
    element.animate(seq, { duration: 300 })
  },
  isValid (invalid) {
    if (invalid[0] === undefined) {
      return true
    } else {
      this.$wobble(invalid[0])
      invalid[0].focus()
      return false
    }
  },
  getState () {
    return this.$store.state.accountStore
  },

  setLogin (payload) {
    this.$store.commit('setLogin', payload)
  },

  setLogout () {
    this.$store.commit('setLogout')
  },
  getDateFormat (unformedDate) {
    const date = new Date(unformedDate)
    let month = date.getMonth() + 1
    let day = date.getDate()
    let hour = date.getHours()
    let minute = date.getMinutes()
    let second = date.getSeconds()

    month = month >= 10 ? month : '0' + month
    day = day >= 10 ? day : '0' + day
    hour = hour >= 10 ? hour : '0' + hour
    minute = minute >= 10 ? minute : '0' + minute
    second = second >= 10 ? second : '0' + second

    return date.getFullYear() + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second
  }
}

export default {
  install (Vue) {
    Vue.config.globalProperties.$wobble = methods.wobble
    Vue.config.globalProperties.$isValid = methods.isValid
    Vue.config.globalProperties.$getState = methods.getState
    Vue.config.globalProperties.$setLogin = methods.setLogin
    Vue.config.globalProperties.$setLogout = methods.setLogout
    Vue.config.globalProperties.$getDateFormat = methods.getDateFormat
  }
}
