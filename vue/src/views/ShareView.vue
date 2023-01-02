<template>
  <div id="community">
    <div class="container">

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
        @shareCount="shareCount"
        @likeCount="likeCount"
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
      page: Number(this.$route.params.page)
    }
  },
  setup () {
  },
  created () {
    this.getPostList(this.$route.params.page)
  },
  mounted () {
  },
  unmounted () {
  },
  methods: {
    async getPostList (page) {
      const url = '/share/list'
      const params = { page: page }
      const res = await this.$axios.get(url, { params })
      this.pageSize = Math.floor(res.data.count / 10) + 1
      this.postList = res.data.postList
      this.showContents = true
      console.log(res.data.postList)
    },
    movePage (page) {
      this.getPostList(page)
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
    }
  }
}
</script>

<style scoped>
#community {
  position: relative;
  min-width: inherit;
  /*min-height: 100vh;*/
  height: 1500px;
}

.container {
  text-align: center;
  padding-top: 64px;
}

::v-deep(.v-field__input) {
  flex-wrap: nowrap;
  overflow: hidden;
}

</style>
