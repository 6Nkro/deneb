<template>

  <div class="book-group">
    <div
      class="book"
      v-for="item in bookList"
      :key="item.book.book_seq">

      <div class="book-title d-flex align-center handle elevation-3">
        <v-icon
          :icon="item.book.book_icon"
          :color="item.book.book_icon_color"
          size="small"/>
        <div class="px-3">
          {{ item.book.book_name }}
        </div>

      </div>

      <div
        class="elevation-1"
        style="height:0.8rem;"
        :style="{
            'background': `linear-gradient( to right,
          ${item.book.book_color} 90%,
          ${item.book.book_color + '64' }`
          }">
      </div>

      <div
        class="book-content elevation-3">
        <share-bookmark-list
          :book="item.book"
          :bookmarkList="item.bookmarkList"/>

      </div>
    </div>
  </div>
</template>

<script>
import ShareBookmarkList from '@/components/share/post/ShareBookmarkList'

export default {
  props: {
    bookList: Object
  },
  components: { ShareBookmarkList },
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
  methods: {}
}
</script>

<style scoped>
.book-group {
  display: flex;
  justify-content: center;
  flex-direction: row;
  flex-wrap: wrap;
}

.book {
  position: relative;
  min-width: 18rem;
  margin: 1.5rem;
}

.book-title {
  overflow: hidden;
  position: relative;
  font-size: large;
  padding: 0.5rem;
  width: inherit;
  height: 3rem;
}

</style>
