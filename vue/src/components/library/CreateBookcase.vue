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

      <v-responsive
        class="mx-auto"
        max-width="256">
        <v-text-field
          variant="underlined"
          label="이름"
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
      text: '생성',
      loading: false,
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
      const url = '/bookcase/create'
      const params = {
        user_email: this.$store.state.accountStore.account.user_email,
        user_pw: this.$store.state.accountStore.account.user_pw,
        bookcase_name: this.bookcase_name
      }
      const res = await this.$axios.post(url, null, { params })
      if (!res.data) {
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
  padding: 1rem;
  border: #404040;
  border-radius: 0.25rem;
  background: #fff;
  width: 20rem;
  height: 20rem;
  min-width: 20rem;
  min-height: 20rem;
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
