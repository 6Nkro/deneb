<template>
    <div class="search">

      <v-select
        variant="filled"
        class="d-inline-block"
        v-model="searchType"
        :items="['내용', '태그', '작성자']"
        hide-details
        single-line/>

      <v-text-field
        variant="filled"
        class="d-inline-block"
        style="width:24rem"
        placeholder="검색어 입력"
        type="text"
        v-model="search"
        :append-inner-icon="'mdi-magnify'"
        hide-details
        @click:append-inner="log"
        @keydown.enter="log"
        max-length="100"/>
    </div>

    <v-card
      class="post mx-auto px-3 py-3 elevation-3"
      width="720"
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
        class="py-3"
        show-arrows>
        <v-slide-group-item
          v-for="tag in JSON.parse(item.share_tag)"
          :key="tag">
          <v-chip
            class="mx-1">
            {{ tag }}
          </v-chip>
        </v-slide-group-item>
      </v-slide-group>

      <v-row>

        <v-col cols="6" class="text-start px-5 text-truncate">

          <v-list-item-title>{{ item.user_name }}</v-list-item-title>
          <v-list-item-subtitle>{{ getPostDateFormat(item.share_date) }} 저장</v-list-item-subtitle>

        </v-col>

        <v-col cols="6" class="justify-end px-5 d-flex align-center text-truncate">
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
          <span>22</span>
        </v-col>

      </v-row>

    </v-card>

    <v-pagination
      class="pagination my-5"
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
      searchType: '내용',
      search: '',
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
      this.$emit('movePage', this.currentPage)
    }
  },
  methods: {
    log () {
      console.log(this.search)
    },
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
    }
  }
}
</script>

<style scoped>

.search {
  top: 64px;
  padding: 4rem 0 0 0;
  margin: 0 0 4rem 0;
}

.post {
  cursor: pointer;
  transition: 0.3s;
  margin-bottom: 2px;
}

.post:hover {
  background-color: #EEEEEE;
}
</style>
