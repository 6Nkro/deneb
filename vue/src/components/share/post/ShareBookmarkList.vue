<template>

  <div :class="bookmarkList.length !== 0 ? 'bookmark-group' : ''">
    <div
      class="d-flex align-center justify-center"
      v-if="this.book.book_type === 'Bookmark' && this.bookmarkList.length === 0"
      style="height: 40px; background-color: #808080; color: white;">
      <div>아직 등록된 북마크가 없어요.</div>
    </div>
    <div
      class="d-flex align-center justify-center"
      v-if="this.book.book_type === 'Video' && this.bookmarkList.length === 0"
      style="height: 200px; background-color: #808080; color: white;">
      <div>표시할 컨텐츠가 존재하지 않아요.</div>
    </div>
    <YouTube
      v-if="this.book.book_type === 'Video' && this.bookmarkList.length !== 0"
      :src="'https://www.youtube.com/watch?v=' + this.bookmarkList[0].video_id"
      :ref="`youtube${this.book.book_seq}`"
      width="288"
      height="200"/>

    <div
      v-for="item in bookmarkList"
      :key="item.bookmark_seq"
      class="bookmark d-flex align-center">

      <div
        v-if="this.book.book_type === 'Bookmark'"
        class="d-flex align-center py-1">
        <img
          :src="'https://www.google.com/s2/favicons?domain=' + item.bookmark_url"
          alt="favicon">
        <a
          class="px-1"
          :href="getUrlFormat(item.bookmark_url)"
          target="_blank">{{ item.bookmark_name }}
          <v-tooltip
            activator="parent"
            location="top"
            max-width="360">
            <strong>URL</strong> : {{ item.bookmark_url }}
            <hr>
            <strong>MEMO</strong> : {{ item.bookmark_memo }}
          </v-tooltip>
        </a>
      </div>

      <div
        v-if="this.book.book_type === 'Video'"
        class="d-flex align-center py-1">
        <v-icon
          icon="mdi-youtube"
          color="red"/>
        <a
          class="px-1"
          href="javascript:void(0)"
          @click="loadVideo(item, this.book)">
          {{ item.bookmark_name }}
          <v-tooltip
            activator="parent"
            location="bottom"
            max-width="360">
            <strong>TITLE</strong> : {{ item.video_title }}
            <br>
            <strong>CHANNEL</strong> : {{ item.video_channel }}
            <hr>
            <strong>MEMO</strong> : {{ item.bookmark_memo }}
          </v-tooltip>
        </a>
      </div>

    </div>
  </div>

</template>

<script>

export default {
  props: {
    book: Object,
    bookmarkList: Object
  },
  components: {},
  data () {
    return {}
  },
  setup () {
  },
  created () {
  },
  mounted () {
  },
  unmounted () {
  },
  methods: {
    getUrlFormat (url) {
      const isValid = url.startsWith('http://') || url.startsWith('https://')
      return isValid ? url : 'http://' + url
    },
    loadVideo (bookmark, book) {
      this.$refs[`youtube${book.book_seq}`].loadVideoById(bookmark.video_id)
    }
  }
}
</script>

<style scoped>

.bookmark-group {
  padding-bottom: 0.5rem
}

.bookmark {
  position: relative;
  white-space: nowrap;
  overflow: hidden;
  padding: 0 0.5rem
}
</style>
