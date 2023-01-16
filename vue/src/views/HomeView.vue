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
        <div
          class="py-5 d-flex">
          <div style="width:8%" class="text-center px-1">
            순위
          </div>
          <div style="width:24%" class="px-1">
            작성자
          </div>
          <div style="width:24%" class="px-1">
            제목
          </div>
          <div style="width:20%">
            <span class="pl-3">태그</span>
          </div>
          <div style="width:8%" class="text-center px-1">
            좋아요
          </div>
          <div style="width:8%" class="text-center px-1">
            공유
          </div>
          <div style="width:8%" class="text-center px-1">
            댓글
          </div>
        </div>
        <hr>

        <div
          v-for="(item, index) in postList"
          :key="index">
          <div
            class="py-4 d-flex post"
            @click="openDetail(item)">
            <div style="width:8%" class="text-center px-1">
              {{ index + 1 }}
            </div>
            <div style="width:24%" class="text-truncate px-1">
              {{ item.user_name }}
            </div>
            <div style="width:24%" class="text-truncate px-1">
              {{ item.bookcase_name }}
            </div>
            <div style="width:20%">
              <v-slide-group show-arrows>
                <v-slide-group-item
                  v-for="tag in JSON.parse(item.share_tag)"
                  :key="tag">
                  <v-chip
                    class="mx-1 tag">
                    {{ tag }}
                  </v-chip>
                </v-slide-group-item>
              </v-slide-group>
            </div>
            <div style="width:8%" class="text-center px-1">
              <v-icon
                class="mr-1"
                icon="mdi-heart"
                size="x-small"
                color="red"/>
              {{ item.like_count }}
            </div>
            <div style="width:8%" class="text-center px-1">
              <v-icon
                class="mr-1"
                icon="mdi-share-variant"
                size="x-small"/>
              {{ item.share_count }}
            </div>
            <div style="width:8%" class="text-center px-1">
              <v-icon
                class="mr-1"
                icon="mdi-message-reply-text"
                size="x-small"/>
              {{ item.reply_count }}
            </div>
          </div>
          <hr>
        </div>

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
      loadComplete: false
    }
  },
  setup () {
  },
  created () {
    AOS.init()
    this.getChartList()
  },
  mounted () {
  },
  unmounted () {
  },
  methods: {
    async getChartList () {
      const url = '/share/chart'
      const params = {
        account_seq: this.$store.state.accountStore.account.account_seq
      }
      const res = await this.$axios.get(url, { params })
      this.postList = res.data
      this.loadComplete = true
    },
    shareCount (data) {
      const index = this.postList
        .findIndex(item => item.bookcase_seq === data.post.bookcase_seq)
      this.postList[index].share_count = this.postList[index].share_count + data.increase
      this.postList.splice(0, 0)
    },
    likeCount (data) {
      const index = this.postList
        .findIndex(item => item.bookcase_seq === data.post.bookcase_seq)
      this.postList[index].like_count = this.postList[index].like_count + data.increase
      this.postList.splice(0, 0)
    },
    replyCount (data) {
      const index = this.postList
        .findIndex(item => item.bookcase_seq === data.post.bookcase_seq)
      this.postList[index].reply_count = data.count
      this.postList.splice(0, 0)
    },
    openDetail (item) {
      const targetClass = event.target.className
      if (targetClass.includes('chevron')) {
        return false
      }
      this.post = item
      this.post_detail = true
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
</style>
