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
          v-if="book_type==='Bookmark'"
          class="mb-3"
          variant="underlined"
          label="URL"
          v-model="bookmark_url"
          :class="{invalid: bookmark_url === ''}"
          :rules="[rules.required]"
          maxlength="1000"/>

        <v-text-field
          v-if="book_type==='Video'"
          class="text-start mb-3"
          variant="underlined"
          label="URL"
          v-model="video_url"
          :class="{invalid: video_url === '' || !video_valid}"
          :rules="[rules.required]"
          :loading="video_loading"
          :hint="hint"
          persistent-hint
          @change="getVideoData"/>

        <v-textarea
          variant="outlined"
          label="메모"
          v-model="bookmark_memo"
          no-resize
          rows="3"
          counter
          maxlength="60"/>
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
    book: Object,
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
      book_type: this.book.book_type,
      video_url: 'https://www.youtube.com/watch?v=' + this.bookmark.video_id,
      video_title: this.bookmark.video_title,
      video_channel: this.bookmark.video_channel,
      video_loading: false,
      hint: '',
      video_valid: true,
      rules: {
        required: value => !!value || ''
      }
    }
  },
  setup () {
  },
  created () {
    this.getVideoData(this.video_url)
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
        bookmark_url: this.book.book_type === 'Bookmark' ? this.bookmark_url : '-',
        bookmark_memo: this.bookmark_memo,
        bookmark_icon: this.bookmark_icon,
        video_id: this.book.book_type === 'Video' ? this.getId(this.video_url) : '-',
        video_title: this.book.book_type === 'Video' ? this.video_title : '-',
        video_channel: this.book.book_type === 'Video' ? this.video_channel : '-'
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
    },
    async getVideoData () {
      const videoId = this.getId(this.video_url)
      if (videoId === null) {
        this.hint = '* 올바른 형식의 URL을 입력하세요.'
        this.video_valid = false
        return false
      }
      this.video_loading = true
      const url = 'https://www.googleapis.com/youtube/v3/videos'
      const params = {
        part: 'snippet',
        id: videoId,
        key: 'AIzaSyDyq-JOF0s6xKnoBl1ZrQ1vadozAv7Xzss'
      }
      const res = await this.$axios.get(url, { params })
      this.video_loading = false
      if (res.data.items.length === 0) {
        this.hint = '* 동영상을 찾을 수 없어요.'
        this.video_valid = false
        return false
      }
      const title = res.data.items[0].snippet.title
      this.hint = title.length < 20 ? '* ' + title : '* ' + title.slice(0, 19) + '...'
      this.video_title = title
      this.video_channel = res.data.items[0].snippet.channelTitle
      this.video_valid = true
    },
    getId (url) {
      const regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|&v=)([^#&?]*).*/
      return url.match(regExp) === null ? null : url.match(regExp)[2]
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
