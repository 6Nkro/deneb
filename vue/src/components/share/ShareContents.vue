<template>
  <div class="search">

    <v-select
      variant="filled"
      class="d-inline-block"
      v-model="temp_type"
      :items="['내용', '태그', '작성자']"
      hide-details
      single-line/>

    <v-text-field
      variant="filled"
      class="d-inline-block"
      style="width:24rem"
      placeholder="검색어 입력"
      type="text"
      v-model="temp_text"
      :append-inner-icon="'mdi-magnify'"
      hide-details
      @click:append-inner="searchList"
      @keydown.enter="searchList"
      max-length="100"/>
  </div>

  <div
    v-if="postList.length === 0"
    class="ma-5">
    해당하는 데이터가 없어요.
  </div>

  <div class="post-group">
    <v-card
      class="post ma-5 px-3 py-3 elevation-3"
      width="360"
      @click="openDetail(item)"
      v-for="item in postList"
      :key="item.bookcase_seq">

      <v-card-title class="text-truncate">
        {{ item.bookcase_name }}
      </v-card-title>

      <v-card-text class="text-truncate py-1">
        {{ item.share_contents }}
      </v-card-text>

      <v-slide-group
        style="height:56px"
        class="py-3"
        show-arrows>
        <v-slide-group-item
          v-for="tag in JSON.parse(item.share_tag)"
          :key="tag">
          <v-chip
            class="mx-1 tag"
            @click="temp_type='태그'; temp_text=tag; this.searchList()">
            {{ tag }}
          </v-chip>
        </v-slide-group-item>
      </v-slide-group>

      <div class="d-flex pt-1 pb-3">

        <div class="text-start w-50 px-3 text-truncate">

          <v-list-item-title>{{ item.user_name }}</v-list-item-title>
          <v-list-item-subtitle>{{ getPostDateFormat(item.share_date) }} 저장</v-list-item-subtitle>

        </div>

        <div class="d-flex w-50 align-end">
          <div class="ml-auto px-3">
            <v-icon
              class="ml-3 mr-1"
              icon="mdi-heart"
              size="x-small"
              color="red"/>
            <span>{{ item.like_count }}</span>
            <v-icon
              class="ml-3 mr-1"
              icon="mdi-share-variant"
              size="x-small"/>
            <span>{{ item.share_count }}</span>
            <v-icon
              class="ml-3 mr-1"
              icon="mdi-message-reply-text"
              size="x-small"/>
            <span>{{ item.reply_count }}</span>
          </div>
        </div>

      </div>

    </v-card>
  </div>

  <v-pagination
    class="pagination mt-5 mb-10"
    v-model="currentPage"
    :length="pageSize"
    :total-visible="8"
    rounded="circle"/>

</template>

<script>
export default {
  props: {
    postList: Object,
    page: Number,
    pageSize: Number
  },
  components: {},
  data () {
    return {
      temp_type: '내용',
      temp_text: '',
      search_type: null,
      search_text: null,
      search: false,
      currentPage: this.page
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
  watch: {
    currentPage: function () {
      this.$router.push({ name: 'shareList', params: { page: this.currentPage } })
      this.$emit('movePage', {
        page: this.currentPage,
        search_type: this.search_type,
        search_text: this.search_text,
        search: this.search
      })
    }
  },
  methods: {
    getPostDateFormat (date) {
      const today = new Date()
      const shareDate = new Date(new Date(date).getTime() + 32400000)
      const timeGap = today.getTime() - (shareDate.getTime())
      return timeGap > 86400000 ? this.$getDateFormat(shareDate).slice(0, 10) : this.$getDateFormat(shareDate).slice(11, 16)
    },
    openDetail (item) {
      const targetClass = event.target.className
      if (targetClass.includes('chip') || targetClass.includes('chevron')) {
        return false
      }
      this.$emit('openDetail', item)
    },
    searchList () {
      this.search = this.search_text !== ''
      this.search_type = this.temp_type
      this.search_text = this.temp_text
      this.$router.push({ name: 'shareList', params: { page: 1 } })
      this.$emit('movePage', {
        page: 1,
        search_type: this.search_type,
        search_text: this.search_text,
        search: this.search
      })
    }
  }
}
</script>

<style scoped>

.search {
  top: 64px;
  padding: 4rem 0 0 0;
  margin: 0 0 3rem 0;
}

.post-group {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
}

.post {
  cursor: pointer;
  transition: 0.3s;
  margin-bottom: 2px;
}

.post:hover {
  background-color: #EEEEEE;
}

.tag {
  cursor: pointer;
}

.tag:hover {
  opacity: 0.7;
}

.tag:active {
  opacity: 1;
}
</style>
