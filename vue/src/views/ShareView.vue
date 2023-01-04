<template>
  <div id="share">
    <div
      class="container"
      v-if="loadComplete">

      <share-contents
        v-if="showContents"
        :postList="postList"
        :page="page"
        :pageSize="pageSize"
        @movePage="movePage"
        @openDetail="openDetail"/>

      <share-post-detail
        v-if="post_detail"
        :post="post"
        @openLogin="this.$emit('setLoginModal', true)"
        @shareCount="shareCount"
        @likeCount="likeCount"
        @replyCount="replyCount"
        @close="post_detail = false"/>

    </div>
  </div>
</template>

<script>

import ShareContents from '@/components/share/ShareContents'
import SharePostDetail from '@/components/share/post/SharePostDetail'

export default {
  components: { ShareContents, SharePostDetail },
  data () {
    return {
      showContents: false,
      postList: null,
      post: null,
      pageSize: null,
      post_detail: false,
      page: Number(this.$route.params.page),
      loadComplete: false
    }
  },
  setup () {
  },
  created () {
    this.getPostList({
      page: this.$route.params.page,
      search: false
    })
    this.loadComplete = true
  },
  mounted () {
  },
  unmounted () {
  },
  methods: {
    async getPostList (params) {
      const url = params.search ? '/share/search' : '/share/list'
      const res = await this.$axios.get(url, { params })
      this.pageSize = Math.floor(res.data.count / 12) + 1
      this.postList = res.data.postList
      this.showContents = true
    },
    movePage (params) {
      this.getPostList(params)
      this.page = Number(this.$route.params.page)
    },
    openDetail (post) {
      this.post = post
      this.post_detail = true
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
    }
  }
}
</script>

<style scoped>

#share {
  position: relative;
  min-width: inherit;
  min-height: 100vh;
}

.container {
  width:1280px;
  margin: auto;
  text-align: center;
  padding-top: 64px;
}

::v-deep(.v-field__input) {
  flex-wrap: nowrap;
  overflow: hidden;
}

</style>
