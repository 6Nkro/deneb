<template>
  <div id="my-share" v-if="loadComplete">
    <div class="container">
      <div class="table text-start">

        <div class="title mb-3">나의 공유 목록</div>
        <hr>
        <div
          class="py-5 d-flex">
          <div style="width:12%" class="px-1">
            공유코드
          </div>
          <div style="width:24%" class="px-1">
            제목
          </div>
          <div style="width:24%" class="px-1">
            내용
          </div>
          <div style="width:16%" class="text-center px-1">
            날짜
          </div>
          <div style="width:7%" class="text-center px-1">
            좋아요
          </div>
          <div style="width:7%" class="text-center px-1">
            공유
          </div>
          <div style="width:5%" class="text-center px-1">
            공개
          </div>
          <div style="width:5%" class="text-center px-1">
            삭제
          </div>
        </div>
        <hr>

        <div
          v-for="(item, index) in postList"
          :key="index">
          <div
            class="py-4 d-flex post"
            @click="openDetail(item)">
            <div style="width:12%" class="px-1">
              {{ item.share_code }}
            </div>
            <div style="width:24%" class="text-truncate px-1">
              {{ item.bookcase_name }}
            </div>
            <div style="width:24%" class="text-truncate px-1">
              {{ item.share_contents }}
            </div>
            <div style="width:16%" class="text-center px-1">
              {{ getDateFormat(item.share_date) }}
            </div>
            <div style="width:7%" class="text-center px-1">
              <v-icon
                class="mr-1"
                icon="mdi-heart"
                size="x-small"
                color="red"/>
              {{ item.like_count }}
            </div>
            <div style="width:7%" class="text-center px-1">
              <v-icon
                class="mr-1"
                icon="mdi-share-variant"
                size="x-small"/>
              {{ item.share_count }}
            </div>
            <div style="width:5%" class="text-center px-1">
              {{ item.share_public }}
            </div>
            <div style="width:5%" class="text-center px-1">
              <v-icon
                icon="mdi-close"
                color="red-lighten-3"
                @click="deleteShare(item)"
              />
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
  async created () {
    if (!this.$store.state.accountStore.isLogin) {
      this.$emit('setLoginModal', true)
      return false
    }
    await this.getShareList()
    this.loadComplete = true
  },
  mounted () {
  },
  unmounted () {
  },
  methods: {
    async getShareList () {
      const url = '/share/myshare'
      const params = { account_seq: this.$store.state.accountStore.account.account_seq }
      const res = await this.$axios.get(url, { params })
      this.postList = res.data
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
      if (targetClass.includes('mdi-close')) {
        return false
      }
      this.post = item
      this.post_detail = true
    },
    getDateFormat (date) {
      return this.$getDateFormat(new Date(date).getTime() + 32400000)
    },
    deleteShare (item) {
      const data = {
        html: '<strong>\'' + item.bookcase_name + '\'</strong><br> 공유 목록에서 삭제합니다.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: '예',
        cancelButtonText: '아니오'
      }
      this.$swal.fire(data)
        .then(async (result) => {
          if (result.isConfirmed) {
            const url = '/share/delete'
            const params = {
              account_seq: this.$store.state.accountStore.account.account_seq,
              bookcase_seq: item.bookcase_seq
            }
            const res = await this.$axios.post(url, null, { params })
            if (!res.data) {
              return false
            }
            this.postList = res.data
            this.postList.splice(0, 0)
          }
        })
    }
  }
}
</script>

<style scoped>

#my-share {
  position: relative;
  min-height: 100vh;
}

.container {
  margin: auto;
  width: 1280px;
  text-align: center;
  padding-top: 64px;
}

.table {
  margin: 4rem 0;
}

.title {
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
