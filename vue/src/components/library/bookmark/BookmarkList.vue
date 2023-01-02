<template>
  <draggable
    tag="div"
    class="bookmark-group"
    v-model="bookmarkList"
    item-key="bookmark.seq"
    group="bookmark"
    @change="moveBookmark">
    <template #item="{ element, index }">
      <div
        class="bookmark d-flex align-center"
        @mouseenter="showTools[index] = true"
        @mouseleave="showTools[index] = false">

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
            URL : {{ element.bookmark_url }}
            <br><br>
            MEMO : {{ element.bookmark_memo }}
          </v-tooltip>
        </a>

        <div class="ml-auto">
          <v-btn
            class="bookmark-tools"
            :class="{show: showTools[index]}"
            variant="plain"
            icon="mdi-book-remove"
            color="red"
            size="small"
            @click="deleteBookmark(element, index)"/>

          <v-btn
            class="bookmark-tools"
            :class="{show: showTools[index]}"
            variant="plain"
            icon="mdi-book-edit"
            size="small"
            @click="editBookmark(element, index)"/>
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
      setValue: null
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
    }
  }
}
</script>

<style scoped>

.bookmark-group {
  padding: 0.5rem
}

.bookmark {
  position: relative;
  white-space: nowrap;
  overflow: hidden;
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
