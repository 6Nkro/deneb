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
        icon="mdi-bookmark-multiple"
        size="x-large"/>
    </span>

    <div class="modal__content">
      <v-responsive
        class="mx-auto"
        max-width="256">
        <v-text-field
          variant="underlined"
          label="이름"
          v-model="bookmark_name"
          :class="{invalid: bookmark_name === ''}"
          :rules="[rules.required]"
          counter
          maxlength="12"/>

        <v-text-field
          variant="underlined"
          label="URL"
          v-model="bookmark_url"
          :class="{invalid: bookmark_url === ''}"
          :rules="[rules.required]"
          maxlength="1000"/>

        <v-textarea
          variant="outlined"
          label="메모"
          v-model="bookmark_memo"
          no-resize
          rows="3"
          counter
          maxlength="60" />
      </v-responsive>
      <loading-button
        :text="text"
        :loading="loading"
        @click="edit"/>
    </div>
  </vue-final-modal>

</template>

<script>
import { VueFinalModal } from 'vue-final-modal'
import LoadingButton from '@/components/common/LoadingButton'

export default {
  props: {
    bookmark: Object,
    bookcaseIndex: Number,
    bookIndex: Number,
    bookmarkIndex: Number
  },
  components: {
    VueFinalModal, LoadingButton
  },
  data () {
    return {
      showModal: true,
      text: '변경',
      loading: false,
      bookmark_seq: this.bookmark.bookmark_seq,
      parent_book_seq: this.bookmark.parent_book_seq,
      bookmark_name: this.bookmark.bookmark_name,
      bookmark_url: this.bookmark.bookmark_url,
      bookmark_memo: this.bookmark.bookmark_memo,
      bookmark_icon: '',
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
      this.$emit('close', 'editBookmark')
    },
    async edit () {
      const items = document.querySelectorAll('.invalid')
      if (this.loading || !this.$isValid(items)) {
        return false
      }
      this.text = ''
      this.loading = true
      const url = '/bookmark/edit'
      const params = {
        bookmark_seq: this.bookmark_seq,
        parent_book_seq: this.parent_book_seq,
        bookmark_name: this.bookmark_name,
        bookmark_url: this.bookmark_url,
        bookmark_memo: this.bookmark_memo,
        bookmark_icon: this.bookmark_icon
      }
      const res = await this.$axios.post(url, null, { params })
      if (!res.data) {
        return false
      }
      this.$store.commit('editBookmark', {
        bookmark: res.data,
        bookcaseIndex: this.bookcaseIndex,
        bookIndex: this.bookIndex,
        bookmarkIndex: this.bookmarkIndex
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
  width: 20rem;
  height: 30rem;
  min-width: 20rem;
  min-height: 30rem;
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
}

</style>
