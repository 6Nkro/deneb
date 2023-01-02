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
         v-model="book_color">
      <v-icon
        icon="mdi-book-open-page-variant"
        size="x-large"
        :color="book_color"/>
    </span>

    <div class="modal__content">

      <div>
        <fieldset>
          <legend>Book color
            <input
              style="position: absolute;"
              type="color"
              v-model="book_color">
          </legend>
          <span
            class="color-filter"
            v-for="item in colors"
            :key="item">
          <div
            class="color-pick"
            :style="{'background-color': item}"
            @click="book_color=item"/>
          </span>
        </fieldset>
      </div>

      <div>
        <fieldset>
          <legend>Icon
            <input
              style="position: absolute;"
              type="color"
              v-model="icon_color">
          </legend>
          <v-icon
            class="icon"
            :class="{selected: icon === this.icon}"
            v-for="icon in icons"
            :key="icon"
            :icon="icon"
            :color="icon_color"
            @click="this.icon=icon"/>
          <div>
            <span
              class="color-filter my-3"
              v-for="item in colors"
              :key="item">
          <div
            class="color-pick"
            :style="{'background-color': item}"
            @click="icon_color=item"/>
            </span>
          </div>
        </fieldset>
      </div>

      <v-responsive
        class="mx-auto my-3"
        max-width="320">
        <v-text-field
          variant="underlined"
          label="이름"
          v-model="book_name"
          :class="{invalid: book_name === ''}"
          :rules="[rules.required]"
          counter
          maxlength="12"/>

        <v-text-field
          v-if="book_type==='Video'"
          class="text-start"
          variant="underlined"
          label="URL"
          v-model="video_url"
          :class="{invalid: book_type === 'Video' && (video_url === '' || !video_valid)}"
          :rules="[rules.required]"
          :loading="video_loading"
          :hint="hint"
          persistent-hint
          @change="getVideoData"/>
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
    bookcaseIndex: Number,
    bookIndex: Number
  },
  components: {
    VueFinalModal, LoadingButton
  },
  data () {
    return {
      showModal: true,
      text: '변경',
      loading: false,
      book_type: this.book.book_type,
      book_name: this.book.book_name,
      video_url: 'https://youtu.be/' + this.book.video_id,
      video_loading: false,
      hint: '',
      video_valid: true,
      book_color: this.book.book_color,
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
      icon: this.book.book_icon,
      icon_color: this.book.book_icon_color,
      icons: [
        'mdi-book-multiple',
        'mdi-star',
        'mdi-heart',
        'mdi-youtube',
        'mdi-forum',
        'mdi-cart',
        'mdi-controller',
        'mdi-silverware-fork-knife',
        'mdi-information',
        'mdi-briefcase'
      ],
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
      this.$emit('close', 'editBook')
    },
    async edit () {
      const items = document.querySelectorAll('.invalid')
      if (this.loading || !this.$isValid(items)) {
        return false
      }
      this.text = ''
      this.loading = true
      const url = '/book/edit'
      const params = {
        book_seq: this.book.book_seq,
        book_name: this.book_name,
        book_color: this.book_color,
        book_icon: this.icon,
        book_icon_color: this.icon_color,
        video_id: this.getId(this.video_url)
      }
      const res = await this.$axios.post(url, null, { params })
      if (!res.data) {
        return false
      }
      this.$store.commit('editBook', {
        book: res.data.book,
        bookcaseIndex: this.bookcaseIndex,
        bookIndex: this.bookIndex
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
      this.hint = title.length < 25 ? '* ' + title : '* ' + title.slice(0, 24) + '...'
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
  min-width: 24rem;
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

fieldset {
  display: inline-block;
  white-space: nowrap;
  width: 20rem;
  margin: 0 0 0.5rem;
  padding: 0.5rem;
  border: 1px solid #C8C8C8;
  border-radius: 0.5rem;
}

legend {
  position: relative;
  color: #808080;
  padding: 0.5rem;
}

legend > input {
  position: absolute;
  top: 0.5rem;
  left: 0;
  width: 100%;
  height: 75%;
  opacity: 0;
  cursor: pointer;
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

.icon:hover {
  opacity: 0.7;
}

.icon:active {
  opacity: 1;
}

.selected {
  opacity: 0.5;
  transform: translate(0, -4px);
}

.icon {
  width: 1.5rem;
  height: 1.5rem;
  margin: 0 4px;
  transition: 0.3s;
  cursor: pointer;
}

button {
  width: 20rem;
  margin-top: 1rem;
}

</style>
