<template>
  <vue-final-modal v-model="showModal"
                   classes="modal-container"
                   content-class="modal-content elevation-3"
                   :lock-scroll="true"
                   :click-to-close="true"
                   :esc-to-close="true"
                   @closed="close">

    <v-icon
      icon="mdi-close"
      class="modal__close"
      color="#808080"
      @click="showModal = false"/>

    <span class="modal__title">
      <img src="@/assets/images/title_black.png" alt="deneb">
    </span>
    <div class="modal__content">

      <v-responsive
        class="mx-auto"
        max-width="192">

        <v-text-field
          variant="underlined"
          label="이메일"
          v-model="user_email"
          :class="{invalid: user_email === ''}"
          :rules="[rules.required]"
          @keyup.enter="login"
          maxlength="64"/>

        <v-text-field
          variant="underlined"
          label="비밀번호"
          type="password"
          v-model="user_pw"
          :class="{invalid: user_pw === ''}"
          :rules="[rules.required]"
          @keyup.enter="login"
          maxlength="20"/>
      </v-responsive>

      <div>
        <loading-button
          :text="text"
          :loading="loading"
          @click="login"/>
      </div>
      <div>
        <span style="font-size: small">
          아직 회원이 아니신가요?
          <a
            href="javascript:void(0)"
            @click="this.$emit('switch')">
            회원가입
          </a>
        </span>
      </div>
    </div>
  </vue-final-modal>
</template>

<script>

import { VueFinalModal } from 'vue-final-modal'
import LoadingButton from '@/components/common/LoadingButton'

export default {
  components: {
    VueFinalModal, LoadingButton
  },
  data () {
    return {
      showModal: true,
      user_email: '',
      user_pw: '',
      text: '로그인',
      loading: false,
      rules: {
        required: value => !!value || ''
      }
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
        this.close()
        this.$store.commit('setLogin', { account: res.data.account, library: res.data.library })
        console.log(this.$store.state.bookStore.library)
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
  height: 25rem;
  min-width: 20rem;
  min-height: 25rem;
  text-align: center;
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

button {
  margin: 1rem 0 0.5rem 0;
  width: 12rem;
}

span {
  color: #404040;
}

</style>
