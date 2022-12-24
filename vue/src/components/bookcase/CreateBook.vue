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
       <input
         type="color"
         class="color-custom"
         v-model="color">
      <v-icon
        icon="mdi-book-open-page-variant"
        size="x-large"
        :color="color"/>
    </span>

    <div class="modal__content">

      <v-responsive
        class="mx-auto"
        max-width="256">
        <v-text-field
          variant="underlined"
          label="이름"
          v-model="book_name"
          :class="{invalid: book_name === ''}"
          :rules="[rules.required]"
          counter
          maxlength="12"/>
      </v-responsive>

      <div class="color-group">
        <span
          class="color-filter"
          v-for="item in colors"
          :key="item">
          <div
            class="color-pick"
            :style="{'background-color': item}"
            @click="color=item"/>
        </span>
      </div>

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
  props: {
    bookcase: Object,
    bookcaseIndex: Number
  },
  components: {
    VueFinalModal, LoadingButton
  },
  data () {
    return {
      showModal: true,
      text: '생성',
      loading: false,
      book_name: '',
      color: '#9896A4',
      colors: [
        '#9896A4',
        '#F7C9C9',
        '#F7776A',
        '#DD4132',
        '#F9E03D',
        '#78C752',
        '#97DDDD',
        '#91A8D1',
        '#034F83',
        '#B18F6A'
      ],
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
      this.$emit('close', 'createBook')
    },
    async create () {
      const items = document.querySelectorAll('.invalid')
      if (this.loading || !this.$isValid(items)) {
        return false
      }
      this.text = ''
      this.loading = true
      const url = '/book/create'
      const params = {
        parent_bookcase_seq: this.bookcase.bookcase_seq,
        book_name: this.book_name,
        book_color: this.color
      }
      const res = await this.$axios.post(url, null, { params })
      if (!res.data) {
        return false
      }
      this.$store.commit('addBook', {
        book: res.data,
        bookcaseIndex: this.bookcaseIndex
      })
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
  width: 24rem;
  height: 22rem;
  min-width: 24rem;
  min-height: 22rem;
  text-align: center;
}

.modal__title {
  position: relative;
  font-size: 1.5rem;
  font-weight: 700;
  margin: 2.5rem 0;
  align-items: center;
}

.modal__close {
  position: absolute;
  top: 1rem;
  right: 1rem;
  cursor: pointer;
  font-size: 1.5rem;
}

input:focus {
  outline: none;
}

.color-group {
  white-space: nowrap;
  margin: 1rem 0 1.5rem;
}

.color-pick {
  width: inherit;
  height: inherit;
  border: 1px solid #808080;
}

.color-filter {
  display: inline-block;
  position: relative;
  width: 1.5rem;
  height: 1.5rem;
  margin: 0 4px;
  transition: 0.3s;
  cursor: pointer;
  background-color: white;
  text-align: center;
}

.color-filter:hover {
  opacity: 0.7;
}

.color-filter:active {
  opacity: 1;
}

.color-custom {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 3rem;
  height: 3rem;
  z-index: 3;
  cursor: pointer;
  opacity: 0;
}

button {
  width: 16rem;
}

</style>
