<template>
  <vue-final-modal
    v-model="showModal"
    classes="modal-container"
    content-class="modal-content elevation-3"
    :lock-scroll="true"
    :click-to-close="true"
    :esc-to-close="true"
    @closed="close" >

    <v-icon
      icon="mdi-close"
      class="modal__close"
      color="#808080"
      @click="showModal = false"/>

    <span class="modal__title">
      <v-icon
        icon="mdi-bookshelf"
        size="x-large"/>
    </span>

    <div class="modal__content">

      <v-radio-group
        class="mb-3"
        v-model="inputCode"
        inline
        hide-details>

        <v-row>
          <v-col cols="6" class="text-end">
            <v-radio
              label="직접 생성"
              :value="false"
              color="indigo"/>
          </v-col>

          <v-col cols="6" class="text-start">
            <v-radio
              label="코드 입력"
              :value="true"
              color="indigo"/>
          </v-col>
        </v-row>

      </v-radio-group>

      <v-responsive
        class="mx-auto"
        max-width="256">
        <v-text-field
          variant="underlined"
          :label="inputCode ? '공유 코드' : '이름'"
          v-model="bookcase_name"
          :class="{invalid: bookcase_name === ''}"
          :rules="[rules.required]"
          counter
          maxlength="12"/>
      </v-responsive>

      <loading-button
        :text="text"
        :loading="loading"
        @click="create"/>
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
      text: '확인',
      loading: false,
      inputCode: false,
      bookcase_name: '',
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
    async create () {
      const items = document.querySelectorAll('.invalid')
      if (this.loading || !this.$isValid(items)) {
        return false
      }
      this.text = ''
      this.loading = true
      const url = this.inputCode ? '/share/import' : '/bookcase/create'
      const params = {
        account_seq: this.$store.state.accountStore.account.account_seq,
        [this.inputCode ? 'share_code' : 'bookcase_name']: this.bookcase_name
      }
      const res = await this.$axios.post(url, null, { params })
      if (!res.data && this.inputCode) {
        const data = {
          icon: 'error',
          html: '<strong>공유 코드가 유효하지 않아요.</strong>'
        }
        this.$swal.fire(data)
          .then(result => {
            if (result.isConfirmed || result.isDismissed) {
              this.text = '확인'
              this.loading = false
            }
          })
        return false
      }
      this.$store.commit('addBookcase', { bookcase: res.data })
      this.$emit('select', this.$store.state.bookStore.library.length - 1)
      this.close()
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
  padding: 1rem 0 2rem;
  border: #404040;
  border-radius: 0.25rem;
  background: #fff;
  min-width: 22rem;
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

button {
  width: 16rem;
  margin: 1rem 0 0 0;
}

</style>
