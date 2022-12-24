<template>
  <draggable
    tag="div"
    class="book-group"
    v-model="bookList"
    item-key="book.book_seq"
    handle=".handle">
    <template #item="{ element, index }">
      <div class="book">

        <div class="book-title handle elevation-3">
          {{ element.book.book_name }}

          <v-btn
            class="book-fold"
            variant="plain"
            :icon="element.book.book_open === 'Y' ? 'mdi-chevron-up' : 'mdi-chevron-down'"
            @click="setBookState(element.book, index)">
          </v-btn>

        </div>

        <div
          class="elevation-1"
          style="height:0.8rem;"
          :style="{
            'background': `linear-gradient( to right,
          ${element.book.book_color} 90%,
          ${element.book.book_color + '64' }`
          }">
        </div>

        <v-expand-transition>
          <div
            class="book-content elevation-3"
            v-show="element.book.book_open === 'Y'"
            @drop="targetBook=element.book; targetBookIndex=index">
            <bookmark-list
              :book="element.book"
              :bookcaseIndex="bookcaseIndex"
              :bookIndex="index"
              :targetBook="targetBook"
              :targetBookIndex="targetBookIndex"
              @openModal="openModal(element.book, index, 'editBookmark')"
              @setBookmark="setBookmark"
            />
            <v-btn
              block
              variant="text"
              class="bookmark-create"
              @click="openModal(element.book, index, 'createBookmark')">
              <v-icon
                icon="mdi-book-plus"
                color="blue lighten-3"/>
            </v-btn>
          </div>
        </v-expand-transition>

        <div class="book-tools elevation-3">
          <v-btn
            variant="plain"
            icon="mdi-pencil-box-multiple"
            @click="openModal(element.book, index, 'editBook')">
          </v-btn>
          <v-btn
            variant="plain"
            icon="mdi-folder-move"
            @click="moveBook(element.book, index)">
          </v-btn>
          <v-btn
            variant="plain"
            icon="mdi-delete"
            @click="deleteBook(element.book)">
          </v-btn>
        </div>
      </div>
    </template>
    <template #footer>
      <v-btn
        variant="text"
        class="book book-create elevation-3"
        @click="this.$emit('openModal', 'createBook')">
        <v-icon>mdi-view-grid-plus</v-icon>
      </v-btn>
    </template>
  </draggable>
</template>

<script>
import BookmarkList from '@/components/bookmark/BookmarkList'
import Draggable from 'vuedraggable/src/vuedraggable'

export default {
  props: {
    bookcase: Object,
    bookcaseIndex: Number
  },
  components: { BookmarkList, Draggable },
  data () {
    return {
      targetBook: null,
      targetBookIndex: null
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
    bookList: {
      get () {
        return this.$store.state.bookStore.library[this.bookcaseIndex].bookList
      },
      set (value) {
        const order = []
        value.forEach(item => {
          order.push(item.book.book_seq)
        })
        const url = '/book/order'
        const params = {
          bookcase_seq: this.bookcase.bookcase_seq,
          book_order: encodeURIComponent(order)
        }
        this.$axios.post(url, null, { params })
        this.$store.commit('editBookOrder', {
          book: value,
          bookcaseIndex: this.bookcaseIndex
        })
      }
    }
  },
  methods: {
    openModal (book, index, modal) {
      this.$emit('setBook', {
        book: book,
        bookIndex: index
      })
      this.$emit('openModal', modal)
    },
    setBookState (book, bookIndex) {
      const value = book.book_open === 'Y' ? 'N' : 'Y'
      const url = '/book/state'
      const params = {
        book_seq: book.book_seq,
        book_open: value
      }
      this.$axios.post(url, null, { params })
      this.$store.commit('editBookState', {
        value: value,
        bookIndex: bookIndex,
        bookcaseIndex: this.bookcaseIndex
      })
    },
    deleteBook (book) {
      const data = {
        html: '<strong>\'' + book.book_name + '\'</strong><br>폴더를 삭제합니다.',
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
            const url = '/book/delete'
            const params = {
              book_seq: book.book_seq
            }
            const res = await this.$axios.post(url, null, { params })
            if (!res.data) {
              return false
            }
            this.$store.commit('delBook', {
              book: book,
              bookcaseIndex: this.bookcaseIndex
            })
          }
        })
    },
    moveBook (book, index) {
      if (this.$store.state.bookStore.library.length <= 1) {
        const data = {
          icon: 'error',
          html: '<strong>이동할 상위 페이지가 존재하지 않아요.</strong>'
        }
        this.$swal.fire(data)
        return false
      }
      this.openModal(book, index, 'moveBook')
    },
    setBookmark (bookmark) {
      this.$emit('setBookmark', bookmark)
    }
  }
}
</script>

<style scoped>
.book-group {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
}

.book {
  position: relative;
  width: 18rem;
  margin: 1.5rem;
}

.book-title {
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
  position: relative;
  font-size: larger;
  padding: 0.5rem;
  width: inherit;
  height: 3rem;
  cursor: grab;
}

.book-fold {
  position: absolute;
  display: inline-block;
  height: 3rem;
  padding: 0;
  top: 0;
  right: 0;
}

.book-fold button {
  height: inherit;
  padding: 0;
}

.book-create {
  display: block;
  height: 7rem;
  font-size: x-large;
}

.book-tools {
  background-color: #E8E8E8;
  text-align: center;
}

.book-tools button {
  margin: 0 1.5rem;
}

.bookmark-create {
  border-radius: 0;
  font-size: large;
}

button {
  padding: 0.5rem 0;
}

</style>
