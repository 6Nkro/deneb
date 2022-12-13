<template>
  <vue-final-modal v-model="showModal" classes="modal-container" content-class="modal-content" name="login"
                   @closed="close" :lock-scroll="true" :click-to-close="true" :esc-to-close="true">

    <font-awesome-icon icon="fa-solid fa-xmark" class="modal__close" @click="showModal = false"/>

    <span class="modal__title"><img src="@/assets/images/title_black.png" alt="deneb"></span>
    <div class="modal__content">
      <div>
        <input type="text" :class="{ invalid: user_email === '' }" v-model="user_email" @keyup.enter="login"
               placeholder="이메일" maxlength="64">
      </div>
      <div>
        <input type="password" :class="{ invalid: user_pw === '' }" v-model="user_pw" @keyup.enter="login"
               placeholder="비밀번호" maxlength="20">
      </div>
      <div>
        <custom-button :text="text" :loading="loading" @click="login"></custom-button>
      </div>
      <div>
        <span style="font-size: small">아직 회원이 아니신가요?
          <a href="javascript:void(0)" @click="this.$emit('switch')">회원가입</a></span>
      </div>
    </div>
  </vue-final-modal>
</template>

<script>

import { VueFinalModal } from 'vue-final-modal'
import CustomButton from '@/components/CustomButton'

export default {
  components: {
    VueFinalModal,
    CustomButton
  },
  data () {
    return {
      showModal: true,
      text: '로그인',
      loading: false,
      user_email: '',
      user_pw: ''
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
    close () {
      this.showModal = true
      this.$emit('close')
    },
    async login () {
      const items = document.querySelectorAll('.invalid')
      if (this.loading || !this.$isValid(items)) {
        return false
      }
      this.text = ''
      this.loading = true
      const url = '/account/login'
      const params = {
        user_email: this.user_email,
        user_pw: this.user_pw
      }
      const res = await this.$axios.post(url, null, { params })
      console.log(res)
      console.log(res.data)
      if (res.data) {
        close()
        this.$setLogin(res.data)
        this.$router.go()
      } else {
        const data = {
          title: 'Error',
          icon: 'error',
          text: 'ID가 등록되지 않았거나 비밀번호가 올바르지 않습니다.'
        }
        this.$swal.fire(data)
          .then(result => {
            if (result.isConfirmed || result.isDismissed) {
              this.text = '로그인'
              this.loading = false
            }
          })
      }
    }
  }
}
</script>

<style scoped>
::v-deep(.modal-container) {
  display: flex;
  justify-content: center;
  align-items: center;
}

::v-deep(.modal-content) {
  position: relative;
  display: flex;
  flex-direction: column;
  margin: 0 1rem;
  padding: 1rem;
  border: #404040;
  border-radius: 0.25rem;
  background: #fff;
  width: 20rem;
  height: 24rem;
  min-width: 20rem;
  min-height: 24rem;
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  box-shadow: 1px 1px 5px 1px #404040;
}

.modal__title {
  font-size: 1.5rem;
  font-weight: 700;
  margin: 2.5rem 0 2.5rem 0;
  align-items: center;
}

.modal__close {
  position: absolute;
  top: 1rem;
  right: 1rem;
  cursor: pointer;
  font-size: 1.5rem;
}

img {
  width: 10rem;
}

input {
  padding-left: 0.3rem;
  padding-right: 0.3rem;
  border: 0;
  border-bottom: 1px solid #808080;
  margin: 0 0 2rem 0;
  width: 12rem;
}

input:focus {
  outline: none;
}

button {
  margin: 0 0 0.5rem 0;
  width: 12rem;
}

span {
  color: #404040;
}

</style>
