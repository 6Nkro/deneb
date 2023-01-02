<template>
  <vue-final-modal
    v-model="showModal"
    classes="modal-container"
    content-class="modal-content elevation-3"
    :lock-scroll="true"
    :click-to-close="true"
    :esc-to-close="true"
    @closed="close">

    <div class="close-wrap">
      <v-icon
        icon="mdi-close"
        class="modal__close"
        color="#808080"
        @click="showModal = false"/>
    </div>

    <span class="modal__title">

      <div class="title d-flex align-center elevation-3">
         {{ post.bookcase_name }}
        <v-btn
          class="contents-fold ml-auto"
          variant="plain"
          :icon="showContents ? 'mdi-chevron-up' : 'mdi-chevron-down'"
          @click="showContents = !showContents"/>
      </div>

    </span>

    <div class="modal__content">

      <v-expand-transition>

        <div
          class="contents elevation-3"
          v-show="showContents">

          <share-book-list
            :bookList="bookList"/>

          <div class="contents-text text-start">
            {{ post.share_contents }}
          </div>

          <div class="button-group">
            <v-card
              class="button-square d-inline-block mx-3"
              @click="likePost">
              <div>
                <v-icon
                  class="py-1"
                  :icon="like_already ? 'mdi-heart-off' : 'mdi-heart'"
                  color="red"
                  style="font-size: 2rem"/>
                <div class="mt-1">{{ post.like_count }}</div>
              </div>
            </v-card>

            <v-card
              class="button-square d-inline-block mx-3"
              @click="sharePost">
              <div>
                <v-icon
                  icon="mdi-share-variant"
                  color="black"
                  style="font-size: 2rem"/>
                <div class="mt-1">{{ post.share_count }}</div>
              </div>
            </v-card>
          </div>

        </div>

      </v-expand-transition>

      <div class="d-inline-flex justify-center w-100 my-5">
        <v-textarea
          variant="outlined"
          label="댓글"
          v-model="reply_contents"
          no-resize
          rows="3"
          counter
          maxlength="200"/>
        <v-btn
          variant="outlined"
          size="large"
          color="grey"
          style="border-radius: 0; height:104px">
          <v-icon
            icon="mdi-send"
            color="blue"/>
        </v-btn>
      </div>

      <div class="justify-center">

        <hr class="my-3">

        <v-row>

          <v-col cols="2" class="text-start">
            <span class="reply-name"></span>
          </v-col>
          <v-col cols="8" class="text-start">
            <span class="reply-contents"></span>
          </v-col>
          <v-col cols="2" class="text-end">
            <span class="reply-date"></span>
            <v-icon
              icon="mdi-close"
              size="x-small"
              color="red-lighten-3"/>
          </v-col>
          <v-col cols="12">
            <hr>
          </v-col>

        </v-row>

      </div>

    </div>

  </vue-final-modal>

</template>

<script>
import { VueFinalModal } from 'vue-final-modal'
import ShareBookList from '@/components/share/post/ShareBookList'

export default {
  props: {
    post: Object
  },
  components: {
    VueFinalModal, ShareBookList
  },
  data () {
    return {
      showModal: false,
      showContents: true,
      reply_contents: '',
      bookList: null,
      like_already: null
    }
  },
  setup () {
  },
  created () {
    this.getBookList(this.post.bookcase_seq)
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
    async getBookList (seq) {
      const url = '/share/detail'
      const params = {
        user_email: this.$store.state.accountStore.account.user_email,
        user_pw: this.$store.state.accountStore.account.user_pw,
        bookcase_seq: seq
      }
      const res = await this.$axios.get(url, { params })
      if (!res.data) {
        return false
      }
      this.bookList = res.data.bookList
      this.like_already = res.data.like_already
      this.showModal = true
    },
    async likePost () {
      const url = '/share/like'
      const params = {
        user_email: this.$store.state.accountStore.account.user_email,
        user_pw: this.$store.state.accountStore.account.user_pw,
        bookcase_seq: this.post.bookcase_seq,
        like_already: this.like_already
      }
      const res = await this.$axios.post(url, null, { params })
      this.$emit('likeCount', {
        post: this.post,
        increase: this.like_already ? -1 : 1
      })
      this.$store.commit('addBookcase', { bookcase: res.data })
      this.like_already = !this.like_already
    },
    async sharePost () {
      const data = {
        html: '<strong>\'' + this.post.bookcase_name + '\'</strong><br>페이지를 라이브러리에 추가합니다.',
        icon: 'info',
        showCancelButton: true,
        confirmButtonText: '예',
        cancelButtonText: '아니오'
      }
      this.$swal.fire(data)
        .then(async (result) => {
          if (result.isConfirmed) {
            const url = '/share/import'
            const params = {
              user_email: this.$store.state.accountStore.account.user_email,
              user_pw: this.$store.state.accountStore.account.user_pw,
              share_code: this.post.share_code
            }
            const res = await this.$axios.post(url, null, { params })
            this.$emit('shareCount', {
              post: this.post,
              increase: res.data.share_count === 0 ? 1 : 0
            })
            this.$store.commit('addBookcase', { bookcase: res.data })
            const data = {
              icon: 'success',
              html: '<a href="/library">라이브러리</a>에 성공적으로 저장되었어요.'
            }
            this.$swal.fire(data)
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
  padding: 0 0 2rem;
  border: #404040;
  border-radius: 0.25rem;
  background: #fff;
  min-width: 60rem;
  width: 60rem;
  max-height: 80vh;
  overflow-y: scroll;
  text-align: center;
}

::v-deep(.modal-content)::-webkit-scrollbar {
  display: none;
}

.modal__title {
  position: sticky;
  top: 0;
  background-color: white;
  font-size: 1.5rem;
  font-weight: 700;
  padding: 1.5rem 4rem 0 4rem;
  align-items: center;
  z-index: 7;
}

.modal__close {
  position: absolute;
  top: 1rem;
  right: 1rem;
  cursor: pointer;
  font-size: 1.5rem;
}

.close-wrap {
  position: sticky;
  top: 0;
  z-index: 10
}

.modal__content {
  padding: 0 4rem 1rem 4rem;
}

.title {
  padding: 0.5rem 1rem;
}

.contents {
  position: sticky;
  top: 88px;
  background-color: white;
  padding: 1.5rem 1rem;
  z-index: 5;
}

.contents-text {
  padding: 1rem;
}

.button-group {
  margin: 1.5rem 0;
}

.button-square {
  position: relative;
  width: 6rem;
  height: 6rem;
  border: 1px solid #E8E8E8;
}

.button-square > div {
  position: relative;
  top: 50%;
  transform: translate(0, -50%);
}

.reply-name {
  font-weight: bold;
}

.reply-date {
  color: #808080;
  font-size: small;
}
</style>
