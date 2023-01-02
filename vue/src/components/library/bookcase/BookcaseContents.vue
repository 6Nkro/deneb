<template>
  <div
    class="library-container"
    :class="{extend: !sideBar}">
    <div class="title elevation-1">
      <div class="title-text">
        <input
          ref="editName"
          :value="bookcase.bookcase_name"
          @focusin="editable = true"
          @focusout="editName"
          @keydown.enter="editName"
          @keydown.esc="escapeEdit"
          maxlength="20">
      </div>

      <div class="title-button-group">
        <v-btn
          class="title-button elevation-3"
          color="red"
          @click="deleteBookcase">
          <v-icon>mdi-delete</v-icon>
          Delete
        </v-btn>
        <v-btn
          class="title-button elevation-3"
          color="blue"
          @click="openShare">
          <v-icon>mdi-share</v-icon>
          Share
        </v-btn>
      </div>
    </div>
    <hr>
    <div class="contents">
      <book-list
        :bookcase="bookcase"
        :bookcaseIndex="bookcaseIndex"
        @openModal="openModal"
        @setBook="setBook"
        @setBookmark="setBookmark"/>
    </div>
  </div>
  <create-book
    v-if="createBook"
    :bookcase="bookcase"
    :bookcaseIndex="bookcaseIndex"
    @close="closeModal"/>

  <edit-book
    v-if="editBook"
    :book="book"
    :bookIndex="bookIndex"
    :bookcaseIndex="bookcaseIndex"
    @close="closeModal"/>

  <move-book
    v-if="moveBook"
    :book="book"
    :bookcase="bookcase"
    :bookcaseIndex="bookcaseIndex"
    @close="closeModal"/>

  <create-bookmark
    v-if="createBookmark"
    :book="book"
    :bookIndex="bookIndex"
    :bookcaseIndex="bookcaseIndex"
    @close="closeModal"/>

  <edit-bookmark
    v-if="editBookmark"
    :bookmark="bookmark"
    :bookmarkIndex="bookmarkIndex"
    :bookIndex="bookIndex"
    :bookcaseIndex="bookcaseIndex"
    @close="closeModal"/>

  <share-bookcase
    v-if="shareBookcase"
    :bookcase="bookcase"
    :bookcaseIndex="bookcaseIndex"
    @close="closeModal"/>
</template>

<script>

import CreateBook from '@/components/library/bookcase/CreateBook'
import EditBook from '@/components/library/book/EditBook'
import MoveBook from '@/components/library/book/MoveBook'
import CreateBookmark from '@/components/library/book/CreateBookmark'
import BookList from '@/components/library/book/BookList'
import EditBookmark from '@/components/library/bookmark/EditBookmark'
import ShareBookcase from '@/components/library/bookcase/ShareBookcase'

export default {
  props: {
    sideBar: Boolean,
    bookcase: Object,
    bookcaseIndex: Number
  },
  components: { CreateBook, EditBook, MoveBook, CreateBookmark, EditBookmark, ShareBookcase, BookList },
  data () {
    return {
      createBook: false,
      editBook: false,
      moveBook: false,
      createBookmark: false,
      editBookmark: false,
      shareBookcase: false,
      editable: false,
      book: null,
      bookIndex: null,
      bookmark: null,
      bookmarkIndex: null
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
  methods: {
    openModal (modal) {
      if (modal === 'createBook') {
        this.createBook = true
      } else if (modal === 'editBook') {
        this.editBook = true
      } else if (modal === 'moveBook') {
        this.moveBook = true
      } else if (modal === 'createBookmark') {
        this.createBookmark = true
      } else if (modal === 'editBookmark') {
        this.editBookmark = true
      }
    },
    closeModal (modal) {
      if (modal === 'createBook') {
        this.createBook = false
      } else if (modal === 'editBook') {
        this.editBook = false
      } else if (modal === 'moveBook') {
        this.moveBook = false
      } else if (modal === 'createBookmark') {
        this.createBookmark = false
      } else if (modal === 'editBookmark') {
        this.editBookmark = false
      } else if (modal === 'shareBookcase') {
        this.shareBookcase = false
      }
    },
    setBook (book) {
      this.book = book.book
      this.bookIndex = book.bookIndex
    },
    setBookmark (bookmark) {
      this.bookmark = bookmark.bookmark
      this.bookmarkIndex = bookmark.bookmarkIndex
    },
    deleteBookcase () {
      if (this.$store.state.bookStore.library.length <= 1) {
        const data = {
          icon: 'error',
          html: '<strong>마지막 페이지는 삭제할 수 없어요.</strong>'
        }
        this.$swal.fire(data)
        return false
      }
      const data = {
        html: '<strong>\'' + this.bookcase.bookcase_name + '\'</strong><br>페이지를 삭제합니다.',
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
            const url = '/bookcase/delete'
            const params = {
              bookcase_seq: this.bookcase.bookcase_seq
            }
            const res = await this.$axios.post(url, null, { params })
            if (!res.data) {
              return false
            }
            this.$store.commit('delBookcase', {
              bookcaseIndex: this.bookcaseIndex
            })
            this.$emit('select', 0)
          }
        })
    },
    async editName () {
      if (!this.editable || this.$refs.editName.value === '' ||
        this.$refs.editName.value === this.bookcase.bookcase_name) {
        this.escapeEdit()
        return false
      }
      const url = '/bookcase/name'
      const params = {
        bookcase_seq: this.bookcase.bookcase_seq,
        bookcase_name: this.$refs.editName.value
      }
      const res = await this.$axios.post(url, null, { params })
      if (!res.data) {
        return false
      }
      await this.$store.commit('editBookcaseName', {
        bookcase: res.data,
        bookcaseIndex: this.bookcaseIndex
      })
      this.escapeEdit()
    },
    escapeEdit () {
      this.$refs.editName.value = this.bookcase.bookcase_name
      this.editable = false
      this.$refs.editName.blur()
    },
    openShare () {
      if (this.$store.state.bookStore.library[this.bookcaseIndex].bookList.length === 0) {
        const data = {
          icon: 'error',
          html: '<strong>빈 페이지는 공유할 수 없어요.</strong>'
        }
        this.$swal.fire(data)
        return false
      }
      this.shareBookcase = true
    }
  }
}
</script>

<style scoped>

.library-container {
  position: relative;
  width: 100vw;
  left: -20rem;
  padding-top: 64px;
  padding-left: 20rem;
  transition: 0.5s;
}

.title {
  position: relative;
  padding: 1.5rem;
}

.title-text {
  display: flex;
}

.title-text > input {
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
  border: none;
  font-size: xx-large;
  font-weight: bold;
  width: 40rem;
}

.title-text > input:focus {
  outline: none;
}

.title-button-group {
  white-space: nowrap;
  overflow: hidden;
  text-align: right;
  padding-right: 2rem;
}

.title-button {
  cursor: pointer;
  margin-right: 1rem;
}

.contents {
  position: relative;
  padding: 1.5rem;
  min-width: 11.5rem;
}

.extend {
  padding-left: 3rem;
}
</style>
