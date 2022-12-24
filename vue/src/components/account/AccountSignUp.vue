<template>
  <vue-final-modal
    v-model="showModal"
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

      <div>
        <input
          type="text"
          placeholder="이메일"
          id="user_email"
          v-model="user_email.value"
          :class="{ invalid: !user_email.valid }"
          @change="setMessage(user_email)"
          maxlength="64">
      </div>
      <div class="text-start">
        <label
          for="user_email"
          :class="{ invalid: !user_email.valid }">{{ user_email.msg }}
        </label>
      </div>

      <div>
        <input
          type="password"
          placeholder="비밀번호(8~20자)"
          id="user_pw"
          v-model="user_pw.value"
          :class="{ invalid: !user_pw.valid }"
          @input="setMessage(user_pw); setMessage(chk_pw)"
          maxlength="20">
      </div>
      <div class="text-start">
        <label
          for="user_pw"
          :class="{ invalid: !user_pw.valid }">{{ user_pw.msg }}
        </label>
      </div>

      <div>

        <input
          type="password"
          placeholder="비밀번호 확인"
          id="chk_pw"
          v-model="chk_pw.value"
          :class="{ invalid: !chk_pw.valid }"
          @input="setMessage(chk_pw)"
          maxlength="20">
      </div>
      <div class="text-start">
        <label
          for="chk_pw"
          :class="{ invalid: !chk_pw.valid }">{{ chk_pw.msg }}
        </label>
      </div>

      <div>
        <input
          type="text"
          placeholder="닉네임(2~12자)"
          id="user_name"
          v-model="user_name.value"
          :class="{ invalid: !user_name.valid }"
          @change="setMessage(user_name)"
          maxlength="12">
      </div>
      <div class="text-start">
        <label
          for="user_name"
          :class="{ invalid: !user_name.valid }">{{ user_name.msg }}
        </label>
      </div>

      <div style="margin-top: 1.2rem"></div>

      <loading-button
        :text="text"
        :loading="loading"
        @click="join"/>

      <div>
        <span style="font-size: small">
          이미 계정이 있으신가요?
          <a
            href="javascript:void(0)"
            @click="this.$emit('switch')">
            로그인
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
      text: '회원가입',
      loading: false,
      user_email: {
        value: '',
        valid: false,
        msg: '',
        validMsg: '사용 가능한 이메일 입니다.',
        key: 'user_email',
        checkUrl: '/account/chkEmail'
      },
      user_pw: {
        value: '',
        valid: false,
        msg: '',
        validMsg: '사용 가능한 비밀번호 입니다.'
      },
      chk_pw: {
        value: '',
        valid: false,
        msg: '',
        validMsg: '비밀번호가 일치합니다.',
        key: 'chk_pw'
      },
      user_name: {
        value: '',
        valid: false,
        msg: '',
        validMsg: '사용 가능한 닉네임 입니다.',
        key: 'user_name',
        checkUrl: '/account/chkName'
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
    setMessage (item) {
      item.valid = this.isValidRegex(item)
      if (item.value === '') {
        item.msg = ''
        return false
      }
      if (item.key === 'user_email' || item.key === 'user_name') {
        if (item.valid) {
          this.duplCheckMessage(item)
        } else {
          item.msg = this.getInvalidMessage(item)
        }
      } else if (item.key === 'chk_pw') {
        item.msg = this.user_pw.valid ? item.valid ? item.validMsg : this.getInvalidMessage(item) : ''
      } else {
        item.msg = item.valid ? item.validMsg : this.getInvalidMessage(item)
      }
    },
    getInvalidMessage (item) {
      switch (item) {
        case this.user_email:
          return '올바른 이메일 형식이 아닙니다.'
        case this.user_pw:
          return this.user_pw.value.length >= 8 ? '숫자와 문자를 포함해야 합니다.' : '8~20자 사이로 구성해야 합니다.'
        case this.chk_pw:
          return '비밀번호가 일치하지 않습니다.'
        case this.user_name:
          return this.user_name.value.length >= 2 ? '올바른 닉네임 형식이 아닙니다.' : ''
      }
    },
    isValidRegex (item) {
      switch (item) {
        case this.user_email:
          return /^([\w-.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$/.test(this.user_email.value)
        case this.user_pw:
          return /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d~!@#$%^&*()+|=]{8,20}$/.test(this.user_pw.value)
        case this.chk_pw:
          return this.user_pw.value === this.chk_pw.value && this.chk_pw.value.length >= 1
        case this.user_name:
          return /^[가-힣a-zA-Z\d_-]{2,12}$/.test(this.user_name.value)
      }
    },

    async duplCheckMessage (item) {
      const params = { [item.key]: item.value }
      const res = await this.$axios.get(item.checkUrl, { params })
      item.valid = !res.data
      const text = item.key === 'user_email' ? '이메일' : '닉네임'
      item.msg = res.data ? '이미 등록된 ' + text + ' 입니다' : item.validMsg
    },

    async join () {
      const items = document.querySelectorAll('.invalid')
      if (this.loading || !this.$isValid(items)) {
        return false
      }
      this.text = ''
      this.loading = true

      const url = '/account/join'
      const params = {
        user_email: this.user_email.value,
        user_pw: this.user_pw.value,
        user_name: this.user_name.value
      }
      const res = await this.$axios.post(url, null, { params })
      if (!res.data) {
        return false
      }
      this.$store.commit('setLogin', { account: res.data.account, library: res.data.library })
      this.close()

      const data = {
        title: 'Welcome!',
        icon: 'success',
        html: '회원 가입이 성공적으로 완료 되었습니다.'
      }
      this.$swal.fire(data)
        .then(result => {
          if (result.isConfirmed || result.isDismissed) {
            this.$router.go()
          }
        })
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
  height: 32.5rem;
  min-width: 20rem;
  min-height: 32.5rem;
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

input {
  padding-left: 0.3rem;
  padding-right: 0.3rem;
  border: 0;
  border-bottom: 1px solid #808080;
  width: 12rem;
}

input:focus {
  outline: none;
}

button {
  width: 12rem;
}

span {
  color: #404040;
}

div[class=text-start] {
  margin-bottom: 1rem;
  height: 1.5rem;
}

label {
  position: relative;
  font-size: small;
  left:3rem;
}

label[class=invalid] {
  color: red;
}

label:not([class=invalid]) {
  color: green;
}

</style>
