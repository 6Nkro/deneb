<template>
  <vue-final-modal
    v-if="loadComplete"
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
                  icon="mdi-heart"
                  :color="like_already ? 'red' : 'grey'"
                  style="font-size: 2rem; transition: 0.3s;"/>
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

      <div class="d-inline-flex justify-center w-100 reply-input">
        <v-textarea
          variant="outlined"
          label="댓글"
          v-model="reply_contents"
          :class="{invalid: reply_contents === ''}"
          no-resize
          rows="3"
          counter
          maxlength="200"
          @keydown.enter="sendReply"/>
        <v-btn
          variant="outlined"
          size="large"
          color="grey"
          style="border-radius: 0; height:104px"
          @click="sendReply">
          <v-icon
            icon="mdi-send"
            color="blue"/>
        </v-btn>
      </div>

      <div class="justify-center">

        <hr class="mt-3">

        <div v-if="replyList.length === 0">
          <div class=py-3>
            아직 등록된 댓글이 없어요.
          </div>
          <hr>
        </div>

        <div v-if="replyList.length > 0">
          <div
            v-for="(item, index) in replyList"
            :key="index">
            <div class="py-4 d-flex align-center">
              <div style="width:15%" class="reply-name text-truncate text-start px-1">
                {{ item.user_name }}
              </div>
              <div style="width:70%" class="reply-contents text-start px-1">
                {{ item.reply_contents }}
              </div>
              <div style="width:15%" class="text-end px-1">
                <span class="reply-date">
                  {{ getPostDateFormat(item.write_date) }}
                </span>
                <v-icon
                  class="pointer"
                  v-if="this.$store.state.accountStore.account.account_seq === item.account_seq"
                  icon="mdi-close"
                  size="x-small"
                  color="red-lighten-3"
                  @click="deleteReply(item.reply_seq)"/>
              </div>
            </div>
            <hr>
          </div>
        </div>

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
      bookList: [],
      replyList: [],
      like_already: null,
      loadComplete: false
    }
  },
  setup () {
  },
  async created () {
    await this.getBookList(this.post.bookcase_seq)
    await this.getReplyList(this.post.bookcase_seq)
    this.loadComplete = true
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
        account_seq: this.$store.state.accountStore.account.account_seq,
        bookcase_seq: seq
      }
      const res = await this.$axios.get(url, { params })
      if (!res.data) {
        return false
      }
      this.bookList = res.data.bookList
      this.like_already = res.data.like_already
    },
    async getReplyList (seq) {
      const url = '/reply/list'
      const params = {
        parent_bookcase_seq: seq
      }
      const res = await this.$axios.get(url, { params })
      if (!res.data) {
        return false
      }
      this.replyList = res.data
      this.showModal = true
    },
    getPostDateFormat (date) {
      const today = new Date()
      const shareDate = new Date(new Date(date).getTime() + 32400000)
      const timeGap = today.getTime() - (shareDate.getTime())
      return timeGap > 86400000 ? this.$getDateFormat(shareDate).slice(0, 10) : this.$getDateFormat(shareDate).slice(11, 16)
    },
    async likePost () {
      if (!this.$store.state.accountStore.isLogin) {
        this.$emit('openLogin')
        return false
      }
      const url = '/share/like'
      const params = {
        account_seq: this.$store.state.accountStore.account.account_seq,
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
      if (!this.$store.state.accountStore.isLogin) {
        this.$emit('openLogin')
        return false
      }
      const data = {
        html: `
        <strong>${this.post.bookcase_name}</strong>
        <br>페이지를 라이브러리에 추가합니다.
        <br><br>
        <input
        type="text"
        value="${this.post.share_code}"
        style="
        text-align: center;
        height: 2rem;
        font-weight: bold;
        font-size: large;
        border: 0;
        background-color: #E8E8E8;
        outline: none;
        cursor: pointer;"
        onclick="this.select()
        document.execCommand('Copy')
        this.setSelectionRange(0, 0)"
        readonly>`,
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
              account_seq: this.$store.state.accountStore.account.account_seq,
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
              html: '<a href="/library?recent">라이브러리</a>에 성공적으로 저장되었어요.'
            }
            this.$swal.fire(data)
          }
        })
    },
    async sendReply () {
      if (!this.$store.state.accountStore.isLogin) {
        this.$emit('openLogin')
        return false
      }
      const items = document.querySelectorAll('.invalid')
      if (this.loading || !this.$isValid(items)) {
        return false
      }
      const url = '/reply/send'
      const params = {
        account_seq: this.$store.state.accountStore.account.account_seq,
        parent_bookcase_seq: this.post.bookcase_seq,
        reply_contents: this.reply_contents
      }
      const res = await this.$axios.post(url, null, { params })
      this.reply_contents = ''
      this.replyList = res.data
      this.replyList.splice(0, 0)
      this.$emit('replyCount', {
        post: this.post,
        count: res.data.length
      })
    },
    async deleteReply (seq) {
      const url = '/reply/delete'
      const params = {
        parent_bookcase_seq: this.post.bookcase_seq,
        reply_seq: seq
      }
      const res = await this.$axios.post(url, null, { params })
      this.replyList = res.data
      this.replyList.splice(0, 0)
      this.$emit('replyCount', {
        post: this.post,
        count: res.data.length
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
  font-size: 1.5rem;
  font-weight: 700;
  padding: 1.5rem 4rem 0 4rem;
  align-items: center;
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
  margin-top: 1.5rem;
  padding: 0.5rem 1rem;
}

.contents {
  padding: 1.5rem 1rem;
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

.reply-input {
  margin: 2rem 1rem 0 0;
}

.reply-name {
  font-weight: bold;
}

.reply-date {
  color: #808080;
  font-size: small;
}

.pointer {
  cursor: pointer;
}
</style>
