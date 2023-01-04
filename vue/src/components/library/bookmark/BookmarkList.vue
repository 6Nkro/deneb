<template>

  <draggable
    tag="div"
    :class="bookmarkList.length !== 0 ? 'bookmark-group' : ''"
    v-model="bookmarkList"
    item-key="bookmark.seq"
    :group="this.book.book_type === 'Bookmark' ? 'Book' : 'VideoBook'"
    @change="moveBookmark">
    <template #header>
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
        :src="'https://www.youtube.com/watch?v=' + this.bookmarkList[this.book.video_index].video_id"
        :vars="{start: this.bookmarkList[this.book.video_index].video_time}"
        :ref="`youtube${this.bookIndex}`"
        width="288"
        height="200"
        @state-change="onStateChange(bookmarkList[this.book.video_index].bookmark_seq)"/>
    </template>
    <template #item="{ element, index }">
      <div
        class="bookmark d-flex align-center"
        @mouseenter="showTools[index] = true"
        @mouseleave="showTools[index] = false">

        <div
          v-if="this.book.book_type === 'Bookmark'"
          class="d-flex align-center">
          <img
            :src="'https://www.google.com/s2/favicons?domain=' + element.bookmark_url"
            alt="favicon">
          <a
            class="px-1"
            :href="getUrlFormat(element.bookmark_url)"
            target="_blank">{{ element.bookmark_name }}
            <v-tooltip
              activator="parent"
              location="top"
              max-width="360">
              <strong>URL</strong> : {{ element.bookmark_url }}
              <hr>
              <strong>MEMO</strong> : {{ element.bookmark_memo }}
            </v-tooltip>
          </a>
        </div>

        <div
          v-if="this.book.book_type === 'Video'"
          class="d-flex align-center">
          <v-icon
            icon="mdi-youtube"
            color="red"/>
          <a
            class="px-1"
            href="javascript:void(0)"
            @click="loadVideo(element, this.book, index, this.bookIndex)">
            {{ element.bookmark_name }}
            <v-tooltip
              activator="parent"
              location="bottom"
              max-width="360">
              <strong>TITLE</strong> : {{ element.video_title }}
              <br>
              <strong>CHANNEL</strong> : {{ element.video_channel }}
              <hr>
              <strong>MEMO</strong> : {{ element.bookmark_memo }}
            </v-tooltip>
          </a>
        </div>

        <div class="ml-auto">
          <v-btn
            class="bookmark-tools"
            :class="{show: showTools[index]}"
            variant="plain"
            icon="mdi-book-edit"
            size="small"
            @click="editBookmark(element, index)"/>

          <v-btn
            class="bookmark-tools"
            :class="{show: showTools[index]}"
            variant="plain"
            icon="mdi-book-remove"
            color="red"
            size="small"
            @click="deleteBookmark(element, index)"/>
        </div>

      </div>
    </template>
  </draggable>

</template>

<script>

import Draggable from 'vuedraggable/src/vuedraggable'

export default {
  props: {
    book: Object,
    targetBook: Object,
    bookcaseIndex: Number,
    bookIndex: Number,
    targetBookIndex: Number
  },
  components: { Draggable },
  data () {
    return {
      showTools: [],
      setValue: null,
      bookmark: null
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
  computed: {
    bookmarkList: {
      get () {
        return this.$store.state.bookStore.library[this.bookcaseIndex].bookList[this.bookIndex].bookmarkList
      },
      set (value) {
        this.setValue = value
      }
    }
  },
  methods: {
    getUrlFormat (url) {
      const isValid = url.startsWith('http://') || url.startsWith('https://')
      return isValid ? url : 'http://' + url
    },
    editBookmark (bookmark, index) {
      this.$emit('openModal')
      this.$emit('setBookmark', {
        bookmark: bookmark,
        bookmarkIndex: index
      })
      this.$emit('setBook')
    },
    async deleteBookmark (bookmark, index) {
      const url = '/bookmark/delete'
      const params = {
        bookmark_seq: bookmark.bookmark_seq
      }
      const res = await this.$axios.post(url, null, { params })
      if (!res.data) {
        return false
      }
      this.$store.commit('delBookmark', {
        bookcaseIndex: this.bookcaseIndex,
        bookIndex: this.bookIndex,
        bookmarkIndex: index
      })
    },
    async moveBookmark (evt) {
      const value = this.setValue
      const order = []
      value.forEach(item => {
        order.push(item.bookmark_seq)
      })
      const url = '/bookmark/order'
      const params = {
        book_seq: this.book.book_seq,
        bookmark_order: encodeURIComponent(order)
      }
      await this.$axios.post(url, null, { params })
      const data = {
        bookmarkList: value,
        bookcaseIndex: this.bookcaseIndex,
        bookIndex: this.bookIndex
      }
      this.$store.commit('editBookmarkOrder', data)

      if (evt.removed !== undefined) {
        this.$store.commit('editVideoIndex', {
          value: 0,
          bookcaseIndex: this.bookcaseIndex,
          bookIndex: this.bookIndex
        })
        const bookmark = evt.removed.element
        const url = '/bookmark/move'
        const params = {
          bookmark_seq: bookmark.bookmark_seq,
          parent_book_seq: this.targetBook.book_seq
        }
        const res = await this.$axios.post(url, null, { params })
        this.$store.commit('moveBookmark', {
          bookmark: res.data,
          bookIndex: this.targetBookIndex,
          bookcaseIndex: this.bookcaseIndex
        })
      }
    },
    onStateChange (seq) {
      const state = JSON.parse(event.data).info
      if (state === 2) {
        const time = Math.floor(this.$refs[`youtube${this.bookIndex}`].getCurrentTime())
        const url = '/bookmark/time'
        const params = {
          bookmark_seq: seq,
          video_time: time
        }
        this.$axios.post(url, null, { params })

        this.$store.commit('editVideoTime', {
          value: time,
          bookcaseIndex: this.bookcaseIndex,
          bookIndex: this.bookIndex,
          bookmark_seq: seq
        })
      }
    },
    loadVideo (bookmark, book, index, bookIndex) {
      this.$refs[`youtube${bookIndex}`].loadVideoById(bookmark.video_id)
      const url = '/book/index'
      const params = {
        book_seq: book.book_seq,
        video_index: index
      }
      this.$axios.post(url, null, { params })
      this.$store.commit('editVideoIndex', {
        value: index,
        bookcaseIndex: this.bookcaseIndex,
        bookIndex: this.bookIndex
      })
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

.bookmark-tools {
  width: 2rem;
  height: 2rem;
  transition: 0.3s;
  opacity: 0;
}

.show {
  opacity: 1;
}

</style>
