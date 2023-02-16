<template>
  <div id="home" v-if="loadComplete">
    <div class="container">
      <div class="intro">

        <v-carousel
          data-aos="zoom-out"
          data-aos-duration="1000"
          cycle
          interval="5000"
          hide-delimiters
          :show-arrows="false">
          <v-carousel-item cover>
            <div>북마크를 손쉽게 관리해보세요.</div>
            <div class="mt-10">
              <img
                src="@/assets/images/intro1.png"
                alt="intro"
                style="height: 350px">
            </div>
          </v-carousel-item>

          <v-carousel-item cover>
            <div>나만의 북마크 페이지를 꾸며보세요.</div>
            <div class="mt-10">
              <img
                src="@/assets/images/intro2.png"
                alt="intro"
                style="height: 350px">
            </div>
          </v-carousel-item>

          <v-carousel-item cover>
            <div>내 북마크를 다른 사용자와 공유해보세요.</div>
            <div class="mt-10">
              <img
                src="@/assets/images/intro3.png"
                alt="intro"
                style="height: 350px">
            </div>
          </v-carousel-item>

        </v-carousel>

      </div>

      <div
        class="text-start chart"
        data-aos="zoom-out"
        data-aos-duration="1000">
        <div class="chart-title mb-3">인기 Top10</div>
        <hr>
        <div class="d-flex py-3">
          <div style="flex: 0 0 8%;" class="text-center">
            순위
          </div>
          <div style="flex: 0 0 24%;">
            작성자
          </div>
          <div style="flex: 0 0 24%;">
            제목
          </div>
          <div style="flex: 0 0 20%;">
            <span class="pl-3">태그</span>
          </div>
          <div style="flex: 0 0 8%;" class="text-center">
            좋아요
          </div>
          <div style="flex: 0 0 8%;" class="text-center">
            공유
          </div>
          <div style="flex: 0 0 8%;" class="text-center">
            댓글
          </div>
        </div>
        <hr>

        <div v-for="(item, index) in postList" :key="index">
          <div class="py-4 d-flex post" @click="openDetail(item)">
            <div style="flex: 0 0 8%;" class="text-center">{{ index + 1 }}</div>
            <div style="flex: 0 0 24%;" class="text-truncate px-1">{{ item.user_name }}</div>
            <div style="flex: 0 0 24%;" class="text-truncate px-1">{{ item.bookcase_name }}</div>
            <div style="flex: 0 0 20%;">
              <v-slide-group show-arrows>
                <v-slide-group-item v-for="tag in JSON.parse(item.share_tag)" :key="tag">
                  <v-chip class="mx-1 tag">{{ tag }}</v-chip>
                </v-slide-group-item>
              </v-slide-group>
            </div>
            <div style="flex: 0 0 8%;" class="text-center">
              <v-icon class="mr-1" icon="mdi-heart" size="x-small" color="red" />
              {{ item.like_count }}
            </div>
            <div style="flex: 0 0 8%;" class="text-center">
              <v-icon class="mr-1" icon="mdi-share-variant" size="x-small" />
              {{ item.share_count }}
            </div>
            <div style="flex: 0 0 8%;" class="text-center">
              <v-icon class="mr-1" icon="mdi-message-reply-text" size="x-small" />
              {{ item.reply_count }}
            </div>
          </div>
          <hr>
        </div>

      </div>

      <div class="text-start comment">

        <div class="chart-title mb-3">Comment</div>
        <div class="d-inline-flex justify-center w-100 comment-input">
          <v-textarea
            variant="outlined"
            placeholder="하고 싶은 말이나 건의 사항등을 적어주세요."
            v-model="comment_contents"
            :class="{invalid_comment: comment_contents === ''}"
            no-resize
            rows="3"
            counter
            maxlength="200"
            @keydown.prevent.enter="sendComment"/>
          <v-btn
            variant="outlined"
            size="large"
            color="grey"
            style="border-radius: 0; height:104px"
            @click="sendComment">
            <v-icon
              icon="mdi-send"
              color="blue"/>
          </v-btn>
        </div>

        <div class="justify-center">

          <hr class="mt-3">

          <div v-if="commentList.length === 0">
            <div class="py-3 text-center">
              아직 등록된 코멘트가 없어요.
            </div>
            <hr>
          </div>

          <div v-if="commentList.length > 0">
            <div
              v-for="(item, index) in commentList"
              :key="index">
              <div class="py-4 d-flex align-center">
                <div style="width:15%" class="comment-name text-truncate text-start px-1">
                  {{ item.user_name }}
                </div>
                <div style="width:70%" class="comment-contents text-start px-1">
                  {{ item.reply_contents }}
                </div>
                <div style="width:15%" class="text-end px-1">
                <span class="comment-date">
                  {{ getPostDateFormat(item.write_date) }}
                </span>
                  <v-icon
                    class="pointer"
                    v-if="this.$store.state.accountStore.account.account_seq === item.account_seq"
                    icon="mdi-close"
                    size="x-small"
                    color="red-lighten-3"
                    @click="deleteComment(item.reply_seq)"/>
                </div>
              </div>
              <hr>
            </div>
          </div>

        </div>

        <v-pagination
          class="pagination mt-5 mb-10"
          v-model="currentPage"
          :length="pageSize"
          :total-visible="8"
          rounded="circle"/>

      </div>

    </div>

    <share-post-detail
      v-if="post_detail"
      :post="post"
      @openLogin="this.$emit('setLoginModal', true)"
      @shareCount="shareCount"
      @likeCount="likeCount"
      @replyCount="replyCount"
      @close="post_detail = false"/>

  </div>

</template>

<script>
import AOS from 'aos'
import SharePostDetail from '@/components/share/post/SharePostDetail'

export default {

  components: { SharePostDetail },
  data () {
    return {
      postList: [],
      post: null,
      post_detail: false,
      loadComplete: false,
      comment_contents: '',
      commentList: [],
      pageSize: 0,
      currentPage: 0
    }
  },
  setup () {
  },
  async created () {
    if (this.$route.query.test !== undefined) {
      const url = '/account/login'
      const params = {
        user_email: 'test@deneb.run',
        user_pw: 'deneb123'
      }
      const res = await this.$axios.post(url, null, { params })
      this.$store.commit('setLogin', { account: res.data.account, library: res.data.library })
      location.href = '/'
    }

    AOS.init()
    await this.getChartList()
    await this.getCommentList(-1)
    this.currentPage = this.pageSize
  },
  mounted () {
  },
  unmounted () {
  },
  watch: {
    currentPage: function () {
      this.getCommentList(this.currentPage)
    }
  },
  methods: {
    async getChartList () {
      const url = '/share/chart'
      const res = await this.$axios.get(url)
      this.postList = res.data
      this.loadComplete = true
    },
    shareCount (data) {
      const post = this.postList.find((item) => item.bookcase_seq === data.post.bookcase_seq)
      if (post) post.share_count += data.increase
    },
    likeCount (data) {
      const post = this.postList.find((item) => item.bookcase_seq === data.post.bookcase_seq)
      if (post) post.like_count += data.increase
    },
    replyCount (data) {
      const post = this.postList.find((item) => item.bookcase_seq === data.post.bookcase_seq)
      if (post) post.reply_count = data.count
    },
    openDetail (item) {
      const targetClass = event.target.className
      if (targetClass.includes('chevron')) {
        return false
      }
      this.post = item
      this.post_detail = true
    },
    async getCommentList (page) {
      const url = '/reply/comment'
      const params = { page }
      const res = await this.$axios.get(url, { params })
      if (!res.data) {
        return false
      }
      this.pageSize = Math.ceil(res.data.count / 10)
      this.commentList = res.data.commentList
    },
    async sendComment () {
      if (!this.$store.state.accountStore.isLogin) {
        this.$emit('setLoginModal', true)
        return false
      }
      const items = document.querySelectorAll('.invalid_comment')
      if (this.loading || !this.$isValid(items)) {
        return false
      }
      const url = '/reply/send'
      const params = {
        account_seq: this.$store.state.accountStore.account.account_seq,
        parent_bookcase_seq: -1,
        reply_contents: this.comment_contents
      }
      this.comment_contents = ''
      await this.$axios.post(url, null, { params })
      await this.getCommentList(-1)
      this.currentPage = this.pageSize
    },
    async deleteComment (seq) {
      const url = '/reply/delete'
      const params = {
        parent_bookcase_seq: -1,
        reply_seq: seq
      }
      await this.$axios.post(url, null, { params })
      if (this.commentList.length === 1) {
        await this.getCommentList(this.currentPage - 1)
        this.currentPage -= 1
      } else {
        await this.getCommentList(this.currentPage)
      }
    },
    getPostDateFormat (date) {
      const today = new Date().setHours(0, 0, 0, 0)
      const shareDate = new Date(date).setHours(0, 0, 0, 0)
      const formattedDate = this.$getDateFormat(shareDate)
      return today - shareDate >= 86400000 ? formattedDate.slice(0, 10) : formattedDate.slice(11, 16)
    }
  }
}
</script>

<style scoped>

#home {
  position: relative;
  min-width: inherit;
  min-height: 100vh;
}

.container {
  margin: auto;
  width: 1280px;
  text-align: center;
  padding-top: 64px;
}

.intro {
  margin: 5rem 0;
  font-size: x-large;
  font-weight: bold;
  color: #808080;
}

.chart {
  margin: 5rem 0;
  color: #404040
}

.chart-title {
  font-size: large;
  font-weight: bold;
}

hr {
  border-top: 1px solid #E8E8E8;
}

.post {
  cursor: pointer;
}

.post:hover {
  background-color: #E8E8E8;
}

.comment {
  margin: 5rem 0;
}

.comment-input {
  max-height: 126px;
}

.comment-name {
  font-weight: bold;
}

.comment-date {
  color: #808080;
  font-size: small;
}
</style>
