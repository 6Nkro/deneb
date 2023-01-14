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
      <v-icon
        icon="mdi-folder-move"
        size="x-large"
        :color="this.book.book_color"/>
      <div class="book-name">{{ this.book.book_name }}</div>
    </span>

    <div class="modal__content">

      <v-responsive
        class="mx-auto"
        max-width="256">

        <v-text-field
          class="mb-7"
          variant="filled"
          label="from"
          :model-value="this.bookcase.bookcase_name"
          readonly
          hide-details/>

        <v-icon
          icon="mdi-arrow-down-bold"
          size="large"
          style="margin:0 0 1rem"/>

        <v-select
          class="destination mb-7"
          variant="filled"
          label="to"
          v-model="select"
          :items="items"
          item-title="bookcase.bookcase_name"
          return-object
          hide-details/>
      </v-responsive>

      <loading-button
        :text="text"
        :loading="loading"
        @click="move"/>

    </div>

  </vue-final-modal>

</template>

<script>
import { VueFinalModal } from 'vue-final-modal'
import LoadingButton from '@/components/common/LoadingButton'

export default {
  props: {
    book: Object,
    bookcase: Object,
    bookcaseIndex: Number
  },
  components: {
    VueFinalModal, LoadingButton
  },
  data () {
    return {
      showModal: true,
      text: '이동',
      loading: false,
      book_name: this.book.book_name,
      select: null,
      items: null
    }
  },
  setup () {
  },
  created () {
    const list = []
    for (let i = 0; i < this.$store.state.bookStore.library.length; i++) {
      if (i !== this.bookcaseIndex) {
        list.push(this.$store.state.bookStore.library[i])
      }
    }
    this.select = list[0]
    this.items = list
  },
  mounted () {
  },
  unmounted () {
  },
  methods: {
    close () {
      this.showModal = true
      this.$emit('close', 'moveBook')
    },
    async move () {
      this.text = ''
      this.loading = true
      const url = '/book/move'
      const params = {
        account_seq: this.$store.state.accountStore.account.account_seq,
        book_seq: this.book.book_seq,
        origin_seq: this.bookcase.bookcase_seq,
        destination_seq: this.select.bookcase.bookcase_seq
      }
      const res = await this.$axios.post(url, null, { params })
      if (!res.data) {
        return false
      }
      this.$store.commit('moveBook', res.data)
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
  min-width: 20rem;
  text-align: center;
  font-size: large;
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

.book-name {
  overflow: hidden;
  white-space: nowrap;
}

.destination {
  margin: 0 0 1rem;
}

button {
  width: 16rem
}

</style>
